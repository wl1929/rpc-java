package wl1929.rpc.utils.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangli4773@163.com
 * @date 2020/6/28
 * @description
 */


@Slf4j
public class CuratorUtils {

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 5;
    private static final String CONNECT_STRING = "127.0.0.1:2181";
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    private static Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();
    private static Set<String> registeredPathSet = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;

    static {
        zkClient = getZkClient();
    }

    private CuratorUtils() {

    }

    /**
     * 创建持久化节点。不同于临时节点，持久化节点不会因为客户端断开连接而被删除
     *
     * @param path 节点路径
     */
    public static void createPersistentNode(String path) {
        try {
            if (registeredPathSet.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("节点已经存在，节点为:[{}]", path);
            } else {
                //eg: /my-rpc/github.javaguide.HelloService/127.0.0.1:9999
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("节点创建成功，节点为:[{}]", path);
            }
            registeredPathSet.add(path);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 获取某个字节下的子节点,也就是获取所有提供服务的生产者的地址
     *
     * @param serviceName 服务对象接口名 eg:github.javaguide.HelloService
     * @return 指定字节下的所有子节点
     */
    public static List<String> getChildrenNodes(String serviceName) {
        if (serviceAddressMap.containsKey(serviceName)) {
            return serviceAddressMap.get(serviceName);
        }
        
    }
}
