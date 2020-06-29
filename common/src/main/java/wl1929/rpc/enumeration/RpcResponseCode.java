package wl1929.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 */

@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCode {

    SUCESS(200, "调用方法成功"),
    FAIL(500, "调用方法失败"),
    NOT_FOUND_METHOD(500, "未找到指定方法"),
    NOT_FOUND_CLASS(500, "未找到指定类");

    private final int code;
    private final String message;
}
