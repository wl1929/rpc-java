package wl1929.rpc.exception;

import wl1929.rpc.enumeration.RpcErrorMessageEnum;

/**
 * @author wangli4773@163.com
 * @date 2020/6/28
 * @description
 */

public class RpcException extends RuntimeException{

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
