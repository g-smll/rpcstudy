package provider;

import framework.Url;
import protocol.dubbo.NettyServer;
import protocol.http.HttpServer;
import provider.api.HelloService;
import provider.impl.HelloServiceImpl;
import register.RemoteMapRegister;

import javax.servlet.http.HttpServlet;

public class Provider {

    public static void main(String[] args) {

        Url url = new Url("localhost",8081);
        //1.本地注册
        //接口实现类
        LocalRegister.register(HelloService.class.getName(), HelloServiceImpl.class);

        //2.注册中心注册
        RemoteMapRegister.register(HelloService.class.getName(),url);

        //启动Tomcat
//        HttpServer httpServer = new HttpServer();

//        httpServer.start("localhost",8080);
        NettyServer.startServer(url.getHostName(),url.getPort());
    }
}
