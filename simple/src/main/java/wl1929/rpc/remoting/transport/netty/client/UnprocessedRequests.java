package wl1929.rpc.remoting.transport.netty.client;

import wl1929.rpc.remoting.dto.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 未处理的请求
 */

public class UnprocessedRequests {

    private static Map<String, CompletableFuture<RpcResponse>>
            unprocessedResponseFutures = new ConcurrentHashMap<>();

    public void put(String requsetId, CompletableFuture<RpcResponse> future) {
        unprocessedResponseFutures.put(requsetId, future);
    }

    public void complete(RpcResponse rpcResponse) {
        CompletableFuture<RpcResponse> future =
                unprocessedResponseFutures.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }
}
