package protocol.dubbo;

import com.alibaba.fastjson.JSONObject;
import framework.Invocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import provider.LocalRegister;
import provider.impl.HelloServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  {

        Invocation invocation = (Invocation)msg;

        Class serviceImp = LocalRegister.get(invocation.getInterfaceName());

        Method method = null;
        try {
            method = serviceImp.getMethod(invocation.getMethodName(),invocation.getParamTypes());
            Object result = method.invoke(serviceImp.newInstance(),invocation.getParams());
            ctx.writeAndFlush("Netty:"+result);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
