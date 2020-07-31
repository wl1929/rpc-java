package wl1929.rpc.remoting.transport.netty.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.factory.SingletonFactory;
import wl1929.rpc.handler.RpcRequestHandler;
import wl1929.rpc.remoting.dto.RpcRequest;
import wl1929.rpc.remoting.dto.RpcResponse;
import wl1929.rpc.utils.concurrent.threadpool.CustomThreadPoolConfig;
import wl1929.rpc.utils.concurrent.threadpool.ThreadPoolFactoryUtils;

import java.util.concurrent.ExecutorService;

/**
 * @Description: 自定义服务端的 ChannelHandler 来处理客户端发过来的数据。
 * 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * hannelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。详见《Netty进阶之路 跟着案例学 Netty》
 * @Author wangli4773@163.com
 * @Created: 2020/07/06 16:53
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final String THREAD_NAME_PREFIX = "netty-server-handler-rpc-pool";
    private final RpcRequestHandler rpcRequestHandler;
    private final ExecutorService threadPool;

    public NettyServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
        CustomThreadPoolConfig customThreadPoolConfig = new CustomThreadPoolConfig();
        customThreadPoolConfig.setCorePoolSize(6);
        this.threadPool = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent(THREAD_NAME_PREFIX, customThreadPoolConfig);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        threadPool.execute(() -> {
            try {
                log.info("server receive msg: [{}] ", msg);
                RpcRequest rpcRequest = (RpcRequest) msg;
                // 执行目标方法（客户端需要执行的方法）并且返回方法结果
                Object result = rpcRequestHandler.handle(rpcRequest);
                log.info(String.format("server get result: %s", result.toString()));
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    //返回方法执行结果给客户端
                    RpcResponse<Object> rpcResponse = RpcResponse.sucess(result, rpcRequest.getRequestId());
                    ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    log.error("not writable now, message dropped");
                }
            } finally {
                //确保 ByteBuf 被释放，不然可能会有内存泄露问题
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}




