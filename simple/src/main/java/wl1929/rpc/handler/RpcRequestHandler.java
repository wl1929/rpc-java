package wl1929.rpc.handler;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.enumeration.RpcResponseCode;
import wl1929.rpc.exception.RpcException;
import wl1929.rpc.provider.ServiceProvider;
import wl1929.rpc.provider.ServiceProviderImpl;
import wl1929.rpc.remoting.dto.RpcRequest;
import wl1929.rpc.remoting.dto.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 根据 rpcRequest 和 service 对象特定的方法并返回结果
     *
     * @param rpcRequest 客户端请求
     * @param service    提供服务的对象
     * @return 目标方法执行的结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),
                    rpcRequest.getParamTypes());
            if (null == method) {
                return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
            }
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]",
                    rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException
                | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
