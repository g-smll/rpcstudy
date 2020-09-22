package protocol.http;

import framework.Invocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient {

    public String send(String hostName, Integer port, Invocation invocation)  {

        URL url;
        String result= null;
        try {
            url = new URL("http",hostName,port,"/");

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            //数据外输out
            OutputStream outputStream = httpURLConnection.getOutputStream();

            ObjectOutputStream oos = new ObjectOutputStream(outputStream);

            oos.writeObject(invocation);

            InputStream inputStream = httpURLConnection.getInputStream();

            result = IOUtils.toString(inputStream);

            oos.flush();

            oos.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }
}
