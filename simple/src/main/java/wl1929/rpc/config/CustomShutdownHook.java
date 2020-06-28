package wl1929.rpc.config;


import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.utils.concurrent.threadpool.ThreadPoolFactoryUtils;

/**
 * @author wangli4773@163.com
 * @date 2020/6/28
 * @description
 * 当服务端（provider）关闭的时候做一些事情比如取消注册所有服务
 */


@Slf4j
public class CustomShutdownHook {

    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CuratorUtils.clearRegistry();
            ThreadPoolFactoryUtils.shutDownAllThreadPool();
        }));
    }
}
