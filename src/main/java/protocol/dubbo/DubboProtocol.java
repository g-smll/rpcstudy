package protocol.dubbo;

import framework.Invocation;
import framework.Protocol;
import framework.Url;

public class DubboProtocol implements Protocol {
    @Override
    public void start(Url url) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.startServer(url.getHostName(),url.getPort());
    }

    @Override
    public String send(Url url, Invocation invocation) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.send(url.getHostName(),url.getPort(),invocation);
        return null;
    }
}
