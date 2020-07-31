package wl1929.rpc.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.factory.SingletonFactory;
import wl1929.rpc.handler.RpcRequestHandler;
import wl1929.rpc.remoting.dto.RpcRequest;
import wl1929.rpc.remoting.dto.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Description:
 * @Author wangli4773@163.com
 * @Created: 2020/07/09 14:31
 */
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable{

    private Socket socket;
    private RpcRequestHandler rpcRequestHandler;

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.sucess(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        }
    }
}
