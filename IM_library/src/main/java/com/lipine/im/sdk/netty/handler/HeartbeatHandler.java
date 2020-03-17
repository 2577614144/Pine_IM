package com.lipine.im.sdk.netty.handler;

import com.lipine.im.sdk.LPIMClient;
import com.lipine.im.sdk.event.CEventCenter;
import com.lipine.im.sdk.event.Events;
import com.lipine.im.sdk.netty.NettyTcpClient;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Time:  2020/3/16 18:02
 * Author: lipine
 * Email: liqingsongandroid@163.com
 * Description: 心跳任务处理器
 */

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;
    public HeartbeatHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            switch (state) {
                case READER_IDLE: {
                    // 规定时间内没收到服务端心跳包响应，进行重连操作
//                    imsClient.resetConnect(false);
                    break;
                }

                case WRITER_IDLE: {
                    // 规定时间内没向服务端发送心跳包，即发送一个心跳包
//                    if (heartbeatTask == null) {
//                        heartbeatTask = new HeartbeatTask(ctx);
//                    }
//                    imsClient.getLoopGroup().execWorkTask(heartbeatTask);
                    MessageProtobuf.Msg heartbeatRespMsg = imsClient.getHeartbeatMsg();
                    if (heartbeatRespMsg == null || ctx == null) {
                        return;
                    }
                    LPIMClient.getInstance().channel = ctx.channel();
                    CEventCenter.dispatchEvent(Events.CHAT_HEARTBEAT_SEND_MESSAGE, 0, 0, heartbeatRespMsg);
                    break;
                }
            }
        }
    }

//    private HeartbeatTask heartbeatTask;
//    private class HeartbeatTask implements Runnable {
//
//        private ChannelHandlerContext ctx;
//
//        public HeartbeatTask(ChannelHandlerContext ctx) {
//            this.ctx = ctx;
//        }
//
//        @Override
//        public void run() {
//
//
////            if (ctx.channel().isActive()) {
////                MessageProtobuf.Msg heartbeatMsg = imsClient.getHeartbeatMsg();
////                if (heartbeatMsg == null) {
////                    return;
////                }
////                System.out.println("发送心跳消息，message=" + heartbeatMsg + "当前心跳间隔为：" + imsClient.getHeartbeatInterval() + "ms\n");
////                imsClient.sendMsg(heartbeatMsg, false);
////            }
//        }
//    }
}
