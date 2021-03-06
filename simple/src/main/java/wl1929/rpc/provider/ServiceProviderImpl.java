package wl1929.rpc.provider;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.enumeration.RpcErrorMessageEnum;
import wl1929.rpc.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 实现了 ServiceProvider 接口，可以将其看做是一个保存和提供服务实例对象的示例
 */

@Slf4j
public class ServiceProviderImpl implements ServiceProvider{

    /**
     * 接口名和服务的对应关系
     * note:处理一个接口被两个实现类实现的情况如何处理？（通过 group 分组）
     * key:service/interface name
     * value:service
     */
    private static Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * note:可以修改为扫描注解注册
     */
    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        serviceMap.put(serviceName, service);
        log.info("Add service: {} and interfaces:{}",
                serviceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (null == service){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}
