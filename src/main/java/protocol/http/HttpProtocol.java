package protocol.http;

import framework.Invocation;
import framework.Protocol;
import framework.Url;

public class HttpProtocol implements Protocol {
    @Override
    public void start(Url url) {
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostName(),url.getPort());
    }

    @Override
    public String send(Url url, Invocation invocation) {
        HttpClient httpClient = new HttpClient();
        return httpClient.send(url.getHostName(),url.getPort(),invocation);
    }
}
