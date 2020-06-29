package wl1929.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 服务发现接口
 */
public interface ServiceDiscovery {
    /**
     * 查找服务
     *
     * @param serviceName 服务名称
     * @return 提供服务的地址
     */
    InetSocketAddress lookupService(String serviceName);
}
