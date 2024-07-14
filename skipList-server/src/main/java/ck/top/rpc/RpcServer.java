package ck.top.rpc;

import ck.top.skipList.SkipList;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcServer {

    private final SkipList<String, String> skipList;
    private final com.alipay.remoting.rpc.RpcServer server;


    public RpcServer(int port) {
        // 初始化跳表实例
        this.skipList = SkipList.getInstance();

        // 初始化rpc服务端
        server = new com.alipay.remoting.rpc.RpcServer(port, false, false);

        // 实现用户请求处理器
        server.registerUserProcessor(new SyncUserProcessor<Request>() {

            @Override
            public Object handleRequest(BizContext bizContext, Request request) throws Exception {
                return handlerRequest(request);
            }

            @Override
            public String interest() {
                return Request.class.getName();
            }
        });

        server.startup();
    }

    public Response<?> handlerRequest(Request request) {
        switch (request.getCmd()) {
            case Request.INSERT:
                boolean insertResult = skipList.insert(request.getKey(), request.getValue());
                return new Response<>(insertResult);
            case Request.SEARCH:
                boolean exist = skipList.isExist(request.getKey());
                return new Response<>(exist);
            case Request.GET:
                String value = skipList.get(request.getKey());
                return new Response<>(value);
            case Request.DELETE:
                boolean deleteResult = skipList.remove(request.getKey());
                return new Response<>(deleteResult);
            case Request.DISPLAY:
                return new Response<>(skipList.show());
            default:
                return new Response<>(false);
        }
    }

    public void destroy() {
        server.shutdown();
        log.info("RPC server destroyed.");
    }
}
