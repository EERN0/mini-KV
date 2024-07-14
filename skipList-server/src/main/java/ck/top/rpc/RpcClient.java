package ck.top.rpc;

import com.alipay.remoting.exception.RemotingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcClient {
    private final com.alipay.remoting.rpc.RpcClient client;

    public RpcClient() {
        client = new com.alipay.remoting.rpc.RpcClient();
        client.startup();
    }

    public <R> R send(Request request) throws RemotingException, InterruptedException {
        Response<R> result = (Response<R>) client.invokeSync(request.getUrl(), request, 200);
        return result.getResult();
    }

    public void destroy() {
        client.shutdown();
        log.info("RPC client destroyed.");
    }
}
