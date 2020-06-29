package wl1929.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.utils.zk.CuratorUtils;

import java.net.InetSocketAddress;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 基于 zookeeper 实现服务发现
 */

@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery{

    @Override
    public InetSocketAddress lookupService(String ServiceName) {
        // TODO(shuang.kou):feat: 负载均衡
        // 这里直接去了第一个找到的服务地址,eg:127.0.0.1:9999
        String serviceAddress = CuratorUtils.getChildrenNodes(ServiceName).get(0);
        log.info("成功找到服务地址:[{}]", serviceAddress);
        String[] socketAddressArray = serviceAddress.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
