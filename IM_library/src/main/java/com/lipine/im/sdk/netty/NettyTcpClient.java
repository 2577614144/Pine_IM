package com.lipine.im.sdk.netty;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.lipine.im.sdk.LPIMClient;
import com.lipine.im.sdk.LPStatusDefine;
import com.lipine.im.sdk.bean.MessageType;
import com.lipine.im.sdk.listener.ConnectServerStatusListener;
import com.lipine.im.sdk.netty.handler.HeartbeatHandler;
import com.lipine.im.sdk.netty.handler.TCPMessageHandler;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Time:2020/3/11
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description: 用于初始化netty的tcp连接
 * */
public class NettyTcpClient {
    private static final String TAG = "NettyTcpClient";
    private static volatile NettyTcpClient instance;

    private NettyTcpClient (){}

    public static NettyTcpClient getInstance() {
        if(instance == null){
            synchronized (NettyTcpClient.class){
                if (instance == null){
                    instance = new NettyTcpClient();
                }
            }
        }
        return instance;
    }
    private Bootstrap bootstrap;
    private Channel channel;
    private boolean isClosed = false;// 标识ims是否已关闭
    // 心跳间隔时间
    private int heartbeatInterval = IMSConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND;

    private ConnectServerStatusListener mConnectServerStatusListener;

    private ExecutorServiceFactory loopGroup;

    /**
     * 初始化
     * @param serverUrl     服务器地址
     * @param port          端口
     * @param callback      ims连接状态回调
     */
    public void init(String serverUrl,int port, ConnectServerStatusListener callback) {
        closeAndRelease();
        isClosed = false;
        this.mConnectServerStatusListener = callback;
//        msgDispatcher = new MsgDispatcher();
//        msgDispatcher.setOnEventListener(listener);
        loopGroup = new ExecutorServiceFactory();
        loopGroup.initBossLoopGroup();// 初始化重连线程组
//        msgTimeoutTimerManager = new MsgTimeoutTimerManager(this);

        initBootstrap();
        connectServer(serverUrl,port);
//        resetConnect(true);// 进行第一次连接
    }
    /**
     * 初始化bootstrap
     */
    private void initBootstrap() {
        EventLoopGroup loopGroup = new NioEventLoopGroup(4);
        bootstrap = new Bootstrap();
        bootstrap.group(loopGroup).channel(NioSocketChannel.class);
        // 设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // 设置禁用nagle算法
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 设置连接超时时长
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, IMSConfig.DEFAULT_CONNECT_TIMEOUT);
        // 设置初始化Channel
        bootstrap.handler(new TCPChannelInitializerHandler(this));
    }
    /**
     * 连接服务器
     */
    private void connectServer(final String currentHost, final int currentPort){
        if (NetworkUtils.isConnected()) {
            try {
                channel = bootstrap.connect(currentHost, currentPort).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            if(mConnectServerStatusListener !=null){
                                LogUtils.eTag(TAG, String.format("连接Server(ip[%s], port[%s])成功", currentHost, currentPort));
                                LPIMClient.getInstance().channel = channel;
                                mConnectServerStatusListener.onConnectionStatus(LPStatusDefine.LPConnectionStatus.LPCONNECTIONSTATUS_CONNECT_SUCCEED);
                            }
                        } else {
                            if(mConnectServerStatusListener !=null) {
                                mConnectServerStatusListener.onConnectionStatus(LPStatusDefine.LPConnectionStatus.LPCONNECTIONSTATUS_CONNECT_ERROR);
                            }
                            LogUtils.eTag(TAG, String.format("连接Server(ip[%s], port[%s])失败", currentHost, currentPort));
                        }
                    }
                }).sync().channel();
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                LogUtils.eTag(TAG, String.format("连接Server(ip[%s], port[%s])失败", currentHost, currentPort)+e.toString());
                closeChannel();
                mConnectServerStatusListener.onConnectionStatus(LPStatusDefine.LPConnectionStatus.LPCONNECTIONSTATUS_CONNECT_ERROR);
            }
        }else{
            mConnectServerStatusListener.onConnectionStatus(LPStatusDefine.LPConnectionStatus.LPCONNECTIONSTATUS_NETWORK_ERROR);
        }
    }

    /**
     * 获取线程池
     *
     * @return
     */
    public ExecutorServiceFactory getLoopGroup() {
        return loopGroup;
    }

    /**
     * 关闭连接，同时释放资源
     */
    public void closeAndRelease() {
        if (isClosed) {
            return;
        }
        isClosed = true;
        // 关闭channel
        try {
            closeChannel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 关闭bootstrap
        try {
            if (bootstrap != null) {
                bootstrap.group().shutdownGracefully();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            // 释放线程池
            if (loopGroup != null) {
                loopGroup.destroy();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            isReconnecting = false;
            channel = null;
            bootstrap = null;
        }
    }

    /**
     * 关闭channel
     */
    private void closeChannel() {
        try {
            if (channel != null) {
                channel.close();
                channel.eventLoop().shutdownGracefully();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.eTag(TAG,"关闭channel出错，reason:" + e.getMessage());
        } finally {
            channel = null;
        }
    }

    /**
     * 添加心跳消息管理handler
     */
    public void addHeartbeatHandler() {
        if (channel == null || !channel.isActive() || channel.pipeline() == null) {
            return;
        }

        try {
            // 之前存在的读写超时handler，先移除掉，再重新添加
            if (channel.pipeline().get(IdleStateHandler.class.getSimpleName()) != null) {
                channel.pipeline().remove(IdleStateHandler.class.getSimpleName());
            }
            // 3次心跳没响应，代表连接已断开
            channel.pipeline().addFirst(IdleStateHandler.class.getSimpleName(), new IdleStateHandler(
                    heartbeatInterval * 3, heartbeatInterval, 0, TimeUnit.MILLISECONDS));
            // 重新添加HeartbeatHandler
            if (channel.pipeline().get(HeartbeatHandler.class.getSimpleName()) != null) {
                channel.pipeline().remove(HeartbeatHandler.class.getSimpleName());
            }
            if (channel.pipeline().get(TCPMessageHandler.class.getSimpleName()) != null) {
                channel.pipeline().addBefore(TCPMessageHandler.class.getSimpleName(), HeartbeatHandler.class.getSimpleName(),
                        new HeartbeatHandler(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.eTag(TAG,"添加心跳消息管理handler失败，reason：" + e.getMessage());
        }
    }


    /**
     * 构建心跳消息
     *
     * @return
     */
    //TODO userId 暂时写成默认
    private String  userId = "111";
    public MessageProtobuf.Msg getHeartbeatMsg() {
        MessageProtobuf.Msg.Builder builder = MessageProtobuf.Msg.newBuilder();
        MessageProtobuf.CommonMsg.Builder headBuilder = MessageProtobuf.CommonMsg.newBuilder();
        headBuilder.setMsgId(UUID.randomUUID().toString());
        headBuilder.setMsgType(MessageType.HEARTBEAT.getMsgType());
        headBuilder.setFromId(userId);
        headBuilder.setTimestamp(System.currentTimeMillis());
        builder.setCommonMsg(headBuilder.build());
        return builder.build();
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(MessageProtobuf.Msg msg) {
        this.sendMsg(msg, true);
    }

    /**
     * 发送消息
     * 重载
     * @param msg
     * @param isJoinTimeoutManager 是否加入发送超时管理器
     */
    public void sendMsg(MessageProtobuf.Msg msg, boolean isJoinTimeoutManager) {
        if (msg == null || msg.getCommonMsg() == null) {
            LogUtils.eTag(TAG,"发送消息失败，消息为空\tmessage=" + msg);
            return;
        }

//        if(!StringUtil.isNullOrEmpty(msg.getHead().getMsgId())) {
//            if(isJoinTimeoutManager) {
//                msgTimeoutTimerManager.add(msg);
//            }
//        }

        if (channel == null) {
            LogUtils.eTag(TAG,"发送消息失败，channel为空\tmessage=" + msg);
            return;
        }

        try {
            if(channel.isActive() && channel.isOpen()){
                channel.writeAndFlush(msg);
            }
        } catch (Exception ex) {
            LogUtils.eTag(TAG,"发送消息失败，reason:" + ex.getMessage() + "\tmessage=" + msg);
        }
    }

    /**
     * 获取心跳间隔时间
     *
     * @return
     */
    public int getHeartbeatInterval() {
        return this.heartbeatInterval;
    }

}
