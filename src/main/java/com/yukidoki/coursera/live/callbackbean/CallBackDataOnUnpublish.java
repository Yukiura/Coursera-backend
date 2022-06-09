package com.yukidoki.coursera.live.callbackbean;

import lombok.Data;

@Data
public class CallBackDataOnUnpublish {
    private String server_id;
    private String action;
    private String client_id;
    private String ip;
    private String vhost;
    private String app;
    private String stream;
    private String param;
}
