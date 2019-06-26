package com.clx.luhuichen.clx.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.clx.luhuichen.clx.MainActivity;

import java.net.SocketAddress;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.sctp.nio.NioSctpChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient extends Thread{



    String ip_;
    int port_;
    boolean exit_;

    public NettyClient(String ip, int port){
        ip_ = ip;
        port_ = port;
        exit_ = false;
    }




//    Handler mhandler = new Handler();





    @Override
    public void run() {
        super.run();




        while(true) {

            exit_ = false;


            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new StringEncoder());


                    pipeline.addLast(new LineBasedFrameDecoder(1024));
                    pipeline.addLast(new ClientHandler());

                }
            });

            //绑定端口  同步等待成功
            ChannelFuture future = null;

            Log.e("clx_netty", "ip = " + ip_);
            Log.e("clx_netty", "port = " + port_);

            try {
                future = bootstrap.connect(ip_, port_).sync();


                Log.e("clx_netty", "connect success");

//            while(!exit_){
//                sleep(100000);
//            }

//            future.await();

                //发送订阅

                String msg = "v1/sub/8312f9d4-e428-4924-a627-81ea9f0cf16a\r\n";

                future.channel().writeAndFlush(msg);




                future.channel().closeFuture().sync();
                group.shutdownGracefully();


                Log.e("clx_netty", "thread exit...");


            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("clx_netty", "-------------error ------");

                Log.e("clx_netty", e.toString());
            }


            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }



    public class ClientHandler extends ChannelHandlerAdapter{
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            super.handlerAdded(ctx);

            Log.e("clx_netty","handlerAdded");
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            super.handlerRemoved(ctx);

            Log.e("clx_netty","handlerRemoved");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);

            Log.e("clx_netty","exceptionCaught");
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            super.channelRegistered(ctx);

            Log.e("clx_netty","channelRegistered");
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            super.channelUnregistered(ctx);

            Log.e("clx_netty","channelUnregistered");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);

            Log.e("clx_netty","channelActive");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);

            Log.e("clx_netty","channelInactive");
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            super.channelReadComplete(ctx);

            Log.e("clx_netty","channelReadComplete");
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            super.userEventTriggered(ctx, evt);

            Log.e("clx_netty"," userEventTriggered");
        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
            super.channelWritabilityChanged(ctx);

            Log.e("clx_netty"," channelWritabilityChanged");
        }

        @Override
        public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            super.bind(ctx, localAddress, promise);

            Log.e("clx_netty"," bind");

        }

        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            super.connect(ctx, remoteAddress, localAddress, promise);

            Log.e("clx_netty"," connect");
        }

        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            super.disconnect(ctx, promise);

            Log.e("clx_netty"," disconnect");
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            super.close(ctx, promise);

            Log.e("clx_netty"," close");
        }

        @Override
        public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            super.deregister(ctx, promise);

            Log.e("clx_netty"," deregister");
        }

        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            super.read(ctx);

            Log.e("clx_netty"," read");
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            super.write(ctx, msg, promise);

            Log.e("clx_netty"," write");
        }

        @Override
        public void flush(ChannelHandlerContext ctx) throws Exception {
            super.flush(ctx);

            Log.e("clx_netty"," flush");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);



            Log.e("clx_netty","msg:"+msg);


            Message message = new Message();
            message.obj = msg;
            MainActivity.mhandler.sendMessage(message);



        }
    }


}


