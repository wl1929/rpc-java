package wl1929.rpc.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.config.CustomShutdownHook;
import wl1929.rpc.provider.ServiceProvider;
import wl1929.rpc.provider.ServiceProviderImpl;
import wl1929.rpc.registry.ServiceRegistry;
import wl1929.rpc.registry.ZkServiceRegistry;
import wl1929.rpc.utils.concurrent.threadpool.ThreadPoolFactoryUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @Description:
 * @Author wangli4773@163.com
 * @Created: 2020/07/06 17:16
 */
@Slf4j
public class SocketRpcServer {

    private final ExecutorService threadPool;
    private final String host;
    private final int port;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer(String host, int port) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceRegistry = new ZkServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
    }

    public <T> void publishService(T service, Class<T> serviceClass) {
        serviceProvider.addServiceProvider(service, serviceClass);
        serviceRegistry.registerService(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while (null != (socket = serverSocket.accept())) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }
}
