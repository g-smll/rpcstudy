package provider;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {

    //
    private static Map<String,Class> MAP = new HashMap<String, Class>();

    /**
     * @param interfaceName
     * @param implClass
     */
    public static void register(String interfaceName,Class implClass){
        MAP.put(interfaceName,implClass);
    }

    //获取实现类

    public static Class get(String interfaceName){
        return MAP.get(interfaceName);
    }
}
