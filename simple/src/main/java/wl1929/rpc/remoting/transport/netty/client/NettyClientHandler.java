package wl1929.rpc.remoting.transport.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.factory.SingletonFactory;
import wl1929.rpc.remoting.dto.RpcRequest;
import wl1929.rpc.remoting.dto.RpcResponse;

/**
 * 自定义客户端 ChannelHandler 来处理服务端发过来的数据
 *
 * 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * channelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。详见《Netty进阶之路 跟着案例学 Netty》
 * @author wangli4773@163.com
 * @date 2020/7/6
 * @time 11:34
 * @description
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private final UnprocessedRequests unprocessedRequests;

    public NettyClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    /**
     * 读取服务端传输的消息
     * @author : wangli4773@163.com
     * @date : 2020/7/6 13:58
     * @param ctx :
     * @param msg :
     * @return : void
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("client receive msg: [{}]", msg);
            RpcResponse rpcResponse = (RpcResponse) msg;
            unprocessedRequests.complete(rpcResponse);
        } catch (Exception e) {
            // 从InBound里读取的ByteBuf要手动释放，
            // 还有自己创建的ByteBuf要自己负责释放。这两处要调用这个release方法。
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 处理客户端消息发生异常的时候被调用
     * @author : wangli4773@163.com
     * @date : 2020/7/6 14:03
     * @param ctx :
     * @param cause :
     * @return : void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
