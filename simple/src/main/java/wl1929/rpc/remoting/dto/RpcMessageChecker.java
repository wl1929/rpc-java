package wl1929.rpc.remoting.dto;

import lombok.extern.slf4j.Slf4j;
import wl1929.rpc.enumeration.RpcErrorMessageEnum;
import wl1929.rpc.enumeration.RpcResponseCode;
import wl1929.rpc.exception.RpcException;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 * 校验 RpcRequest 和 RpcRequest
 */

@Slf4j
public final class RpcMessageChecker {

    private static final String INTERFACE_NAME = "interfaceName";

    private RpcMessageChecker() {}

    public static void check(RpcResponse rpcResponse, RpcRequest rpcRequest) {

        if (null == rpcResponse) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (null == rpcResponse.getCode() || !rpcResponse.getCode().equals(RpcResponseCode.SUCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
