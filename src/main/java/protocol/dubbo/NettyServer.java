package protocol.dubbo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {



    public static void startServer (String hostName, Integer port) {
        run(hostName,port);
    }


    /**
     * netty服务初始化和启动
     */
     public static void run(String hostName,Integer port){

         EventLoopGroup bossGroup = new NioEventLoopGroup(1);

         EventLoopGroup workGroup = new NioEventLoopGroup();
         try {

             ServerBootstrap bootstrap = new ServerBootstrap();

             bootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG,128)
             .childOption(ChannelOption.SO_KEEPALIVE,true)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel socketChannel) throws Exception {
                     ChannelPipeline pipeline = socketChannel.pipeline();
                     pipeline.addLast(
                             new ObjectDecoder(1024 * 1024, ClassResolvers
                                     .weakCachingConcurrentResolver(
                                             this.getClass().getClassLoader())));

                      pipeline.addLast(new ObjectEncoder());

                     pipeline.addLast(new NettyServerHandler());
                 }
             });

             ChannelFuture channelFuture = bootstrap.bind(hostName,port).sync();

             System.out.println("服务提供方开始提供服务......");

             channelFuture.channel().closeFuture().sync();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }finally {
             bossGroup.shutdownGracefully();
             workGroup.shutdownGracefully();
         }

     }

}
