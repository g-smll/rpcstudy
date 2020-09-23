package framework;

import protocol.dubbo.NettyClient;
import protocol.dubbo.NettyClientHandler;
import protocol.http.HttpClient;
import provider.api.HelloService;
import register.RemoteMapRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ProxyFactory {

    /**
     * args 实际为方法入参
     *
     */
    public static <T> T getProxy(final Class interfaceClazz) {

        return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class[]{interfaceClazz}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

//                        HttpClient httpClient = new HttpClient();
                        NettyClient httpClient = new NettyClient();
                        Invocation invocation = new Invocation(interfaceClazz.getName(),
                                method.getName(), method.getParameterTypes(), args);

                        Url url = RemoteMapRegister.random(interfaceClazz.getName());

                        String result = httpClient.send(url.getHostName(), url.getPort(), invocation);

                        return result;
                    }
                });
    }

}
