package wl1929.rpc.remoting.transport.socket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.exception.RpcException;
import wl1929.rpc.registry.ServiceDiscovery;
import wl1929.rpc.registry.ZkServiceDiscovery;
import wl1929.rpc.remoting.dto.RpcRequest;
import wl1929.rpc.remoting.transport.ClientTransport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description: 基于Socket传输 RpcRequest
 * @Author wangli4773@163.com
 * @Created: 2020/07/06 15:14
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements ClientTransport {

    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this.serviceDiscovery = new ZkServiceDiscovery();
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery
                .lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket();) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 通过输出流发送数据到服务端
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 从输入流中读取出 RpcResponse
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败:", e);
        }
    }
}