package wl1929.rpc.remoting.transport.netty.client;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.registry.ServiceDiscovery;
import wl1929.rpc.remoting.transport.ClientTransport;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 基于 Netty 传输 RpcRequest。
 */

@Slf4j
public class NettyClientTransport implements ClientTransport {

    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;
}
