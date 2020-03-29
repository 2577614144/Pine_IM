package com.lipine.im.sdk.netty;

import com.lipine.im.sdk.netty.handler.HeartbeatRespHandler;
import com.lipine.im.sdk.netty.handler.LoginAuthRespHandler;
import com.lipine.im.sdk.netty.handler.TCPMessageHandler;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Time:2020/3/11
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description: 用于初始化netty的tcp连接
 * */
public class TCPChannelInitializerHandler extends ChannelInitializer<Channel> {

    private NettyTcpClient imsClient;

    public TCPChannelInitializerHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // netty提供的自定义长度解码器，解决TCP拆包/粘包问题
//        pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
//        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,
//                0, 2, 0, 2));

        //Netty自带Protobuf编码、解码器处理半包、拆包/粘包
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufVarint32FrameDecoder());

        //protobuf解码器
        pipeline.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));
        //protobuf编码器
        pipeline.addLast(new ProtobufEncoder());

        // 握手认证消息响应处理handler
        pipeline.addLast(LoginAuthRespHandler.class.getSimpleName(), new LoginAuthRespHandler(imsClient));
        // 心跳消息响应处理handler
        pipeline.addLast(HeartbeatRespHandler.class.getSimpleName(), new HeartbeatRespHandler(imsClient));
//        // 接收消息处理handler
        pipeline.addLast(TCPMessageHandler.class.getSimpleName(), new TCPMessageHandler(imsClient));
    }
}
