package ck.top.rpc;

import lombok.Data;

import java.io.Serializable;

@Data
public class Request implements Serializable {
    public static final int INSERT = 1;
    public static final int SEARCH = 2;
    public static final int GET = 3;
    public static final int DELETE = 4;
    public static final int DISPLAY = 5;

    // 请求类型
    private int cmd;
    private String url;
    private String key;
    private String value;

    public Request(int cmd, String url) {
        this.cmd = cmd;
        this.url = url;
    }

    public Request(int cmd, String url, String key) {
        this.cmd = cmd;
        this.url = url;
        this.key = key;
    }

    public Request(int cmd, String url, String key, String value) {
        this.cmd = cmd;
        this.url = url;
        this.key = key;
        this.value = value;
    }
}