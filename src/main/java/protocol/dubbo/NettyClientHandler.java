package protocol.dubbo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    /**
     * 声明数据上下文
     */
    private ChannelHandlerContext context;

    /**
     * 声明返回结果
     */
    private String result;


    /**
     * 声明传输参数
     */
    private String param;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 在其他方法会使用带ctx
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }


    /**
     * 被代理对象调用，发送数据给服务器 -> wait -> 等待被唤醒(channelRead) -> 返回结果
     */
    @Override
    public synchronized Object call() throws Exception {

        context.writeAndFlush(param);
        // 进行 wait,等待channelRead获取到服务器的结果
        wait();
        // 返回结果
        return result;
    }

}