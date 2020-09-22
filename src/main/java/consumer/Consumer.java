package consumer;

import framework.Invocation;
import framework.ProxyFactory;
import protocol.dubbo.NettyClient;
import protocol.http.HttpClient;
import provider.api.HelloService;

public class Consumer {
    public static void main(String[] args) {

        //动态代理
//        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
//        String result = helloService.sayHello("world!!!");
//        System.out.println(result);

        // 创建一个消费者
        NettyClient consumer = new NettyClient();

        // 创建代理对象
        HelloService service = (HelloService)consumer.getBean(HelloService.class);

        String result = service.sayHello("world!");
        System.out.println(result);


    }
}
