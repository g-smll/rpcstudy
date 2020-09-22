package register;

import framework.Url;

import java.io.*;
import java.util.*;

/**
 * 定义远程注册中心
 */
public class RemoteMapRegister {

    private static Map<String, List<Url>> REGISTER = new HashMap<String, List<Url>>();

    public static void register(String interfaceName,Url url){

        List<Url> list = Collections.singletonList(url);

        REGISTER.put(interfaceName,list);
        saveFile();

    }

    public static Url random(String interfaceName){
         REGISTER = getFile();

         List<Url> listUrl = REGISTER.get(interfaceName);

        return listUrl.get(new Random().nextInt(listUrl.size()));
    }

    public static void saveFile(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("temp.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(REGISTER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String,List<Url>> getFile(){

        Map<String,List<Url>> map = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("temp.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            map = (Map<String,List<Url>>)objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;

    }
}
