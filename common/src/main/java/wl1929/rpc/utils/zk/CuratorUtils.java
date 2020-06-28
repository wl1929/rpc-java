package wl1929.rpc.utils.zk;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangli4773@163.com
 * @date 2020/6/28
 * @description
 */

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

}
