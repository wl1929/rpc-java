package wl1929.rpc.remoting.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.remoting.dto.RpcRequest;
import wl1929.rpc.remoting.dto.RpcResponse;
import wl1929.rpc.remoting.transport.netty.codec.kyro.NettyKryoDecoder;
import wl1929.rpc.remoting.transport.netty.codec.kyro.NettyKryoEncoder;
import wl1929.rpc.serialize.kyro.KryoSerializer;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 用于初始化和关闭 Bootstrap 对象
 */

@Slf4j
public final class NettyClient {

    private static Bootstrap bootstrap;
    private static EventLoopGroup eventLoopGroup;

    // 初始化相关资源比如 EventLoopGroup、Bootstrap
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，
                // 减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        /*自定义序列化编解码器*/
                        // RpcResponse -> ByteBuf
                        ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                        // ByteBuf -> RpcRequest
                        ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    @SneakyThrows
    // sneaky:偷偷的，暗中
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端连接成功!");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    public void close() {
        log.info("call close method");
        eventLoopGroup.shutdownGracefully();
    }
}
