package wl1929.rpc.handler;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.provider.ServiceProvider;
import wl1929.rpc.provider.ServiceProviderImpl;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * RpcRequest的处理器
 */

@Slf4j
public class RpcRequestHandler {

    private static ServiceProvider serviceProvider = new ServiceProviderImpl();

    /**
     * 处理 rpcRequest ：调用对应的方法，然后返回方法执行结果
     */
    public Object handle(RpcRequest rpcRequest) {
        //通过注册中心获取到目标类（客户端需要调用类）
    }
}
