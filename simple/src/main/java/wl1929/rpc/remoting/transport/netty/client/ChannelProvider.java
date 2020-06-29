package wl1929.rpc.remoting.transport.netty.client;

import lombok.extern.slf4j.Slf4j;

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 用于获取 Channel 对象
 */

@Slf4j
public final class ChannelProvider {

    private static Map<String, Channel> channels = new ConcurrentHashMap<>();
    private static NettyClient nettyClient;

}
