package ck.top.rpc;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private T result;

    public Response(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
