package com.lipine.im.sdk.netty;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.lipine.im.sdk.LPIMClient;
import com.lipine.im.sdk.LPStatusDefine;
import com.lipine.im.sdk.listener.ConnectServerStatusListener;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

import java.util.Vector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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
            System.err.println("关闭channel出错，reason:" + e.getMessage());
        } finally {
            channel = null;
        }
    }
}
