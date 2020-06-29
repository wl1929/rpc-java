package wl1929.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 服务注册接口
 */

public interface ServiceRegistry {
    /**
     * 注册服务
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
