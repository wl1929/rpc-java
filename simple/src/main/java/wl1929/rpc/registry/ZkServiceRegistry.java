package wl1929.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.utils.zk.CuratorUtils;

import java.net.InetSocketAddress;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 基于 zookeeper 实现服务注册
 */

@Slf4j
public class ZkServiceRegistry implements ServiceRegistry{

    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        //根节点下注册子节点：服务
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/"
                + serviceName + inetSocketAddress.toString();
        CuratorUtils.createPersistentNode(servicePath);
    }
}
