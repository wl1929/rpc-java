package wl1929.rpc.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 获取单例对象的工厂类
 */
public final class SingletonFactory {

    private static Map<String, Object> objectMap = new HashMap<>();

    private SingletonFactory() {}

    public static <T> T getInstance(Class<T> c) {
        String key = c.toString();
        Object instance = objectMap.get(key);
        synchronized (c) {
            if (null == instance) {
                try {
                    instance = c.newInstance();
                    objectMap.put(key, instance);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return c.cast(instance);
    }
}
