package wl1929.rpc.remoting.transport.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.factory.SingletonFactory;
import wl1929.rpc.registry.ServiceDiscovery;
import wl1929.rpc.registry.ZkServiceDiscovery;
import wl1929.rpc.remoting.dto.RpcRequest;
import wl1929.rpc.remoting.dto.RpcResponse;
import wl1929.rpc.remoting.transport.ClientTransport;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

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

    public NettyClientTransport() {
        this.serviceDiscovery = new ZkServiceDiscovery();
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse> sendRpcRequest(RpcRequest rpcRequest) {
        // 构建返回值
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        Channel channel = ChannelProvider.get(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            // 放入未处理的请求
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcRequest);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }
}
