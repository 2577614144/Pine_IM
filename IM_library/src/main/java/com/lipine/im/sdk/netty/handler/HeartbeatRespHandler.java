package com.lipine.im.sdk.netty.handler;

import com.blankj.utilcode.util.LogUtils;
import com.lipine.im.sdk.event.CEventCenter;
import com.lipine.im.sdk.event.Events;
import com.lipine.im.sdk.netty.NettyTcpClient;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Time:  2020/3/17 11:11
 * Author: lipine
 * Email: liqingsongandroid@163.com
 * Description:心跳消息响应处理
 */

public class HeartbeatRespHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = "HeartbeatRespHandler";

    private NettyTcpClient imsClient;

    public HeartbeatRespHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.Msg heartbeatRespMsg = (MessageProtobuf.Msg) msg;
        if (heartbeatRespMsg == null || heartbeatRespMsg.getHead() == null) {
            return;
        }

        MessageProtobuf.Msg heartbeatMsg = imsClient.getHeartbeatMsg();
        if (heartbeatMsg == null || heartbeatMsg.getHead() == null) {
            return;
        }

        int heartbeatMsgType = heartbeatMsg.getHead().getMsgType();
        if (heartbeatMsgType == heartbeatRespMsg.getHead().getMsgType()) {
//            LogUtils.eTag(TAG,"收到服务端心跳响应消息，message=" + heartbeatRespMsg);
            CEventCenter.dispatchEvent(Events.CHAT_HEARTBEAT_RECEIVED_MESSAGE, 0, 0, heartbeatRespMsg);
        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }
}
