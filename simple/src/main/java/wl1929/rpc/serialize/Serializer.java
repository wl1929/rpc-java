package wl1929.rpc.serialize;

/**
 * 序列化接口，所有序列化类都要实现这个接口
 * @author wangli4773@163.com
 * @date 2020/7/6
 * @time 11:00
 * @description
 */
public interface Serializer {

    /**
     * 序列化
     * @author : wangli4773@163.com
     * @date : 2020/7/6 11:01
     * @param obj : 要序列化的对象
     * @return : byte[]: 字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     * @author : wangli4773@163.com
     * @date : 2020/7/6 11:02
     * @param bytes : 序列化后的字节数组
     * @param clazz : 目标类
     * @param <T>   类的类型。举个例子,  {@code String.class} 的类型是 {@code Class<String>}.
     *              如果不知道类的类型的话，使用 {@code Class<?>}
     * @return : T: 反序列化的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
