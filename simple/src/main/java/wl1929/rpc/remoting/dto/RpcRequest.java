package wl1929.rpc.remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author wangli4773@163.com
 * @date 2020/6/29
 * @description
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
