package protocol.dubbo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import provider.impl.HelloServiceImpl;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端(消费者)的消息，并调用服务
        System.out.println("msg:"+msg);
        // 客户端在调用服务端api时，需要定义一个协议
        // 比如我们要求 每次发消息都必须以某个字符串开头 "helloService#hello#"
//        if(msg.toString().startsWith("helloService#hello#")) {
        String s = new HelloServiceImpl().sayHello(msg.toString());
            ctx.writeAndFlush(s);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
