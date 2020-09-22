package protocol.dubbo;

import framework.Url;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {

    // 创建线程池
    private static ExecutorService executorService = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());



    private static NettyClientHandler nettyClientHandler;



    /**
     * 初始化客户端
     */
    public static void initClient() {
        Url url = new Url("localhost",8081);
        nettyClientHandler = new NettyClientHandler();

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(nettyClientHandler);
                    }
                });
        try {
            ChannelFuture sync = bootstrap.connect(url.getHostName(),url.getPort()).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 编写方法，使用代理模式，获取一个代理对象
     */
    public Object getBean(final Class<?> serviceClass) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {serviceClass},(proxy,method,args) ->{
                    if(nettyClientHandler == null){
                        initClient();
                    }
                    // 设置参数，要发给服务区端的信息 providerName就是协议头
                    nettyClientHandler.setParam(""+args[0]);
                    return executorService.submit(nettyClientHandler).get();
                });
    }





    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static void setExecutorService(ExecutorService executorService) {
        NettyClient.executorService = executorService;
    }

    public static NettyClientHandler getNettyClientHandler() {
        return nettyClientHandler;
    }

    public static void setNettyClientHandler(NettyClientHandler nettyClientHandler) {
        NettyClient.nettyClientHandler = nettyClientHandler;
    }
}
