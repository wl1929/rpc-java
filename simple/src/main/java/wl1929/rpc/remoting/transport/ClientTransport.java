package wl1929.rpc.remoting.transport;

import wl1929.rpc.remoting.dto.RpcRequest;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 传输 RpcRequest。
 */

public interface ClientTransport {
    /**
     * 发送消息到服务端
     *
     * @param rpcRequest 消息体
     * @return 服务端返回的数据
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
