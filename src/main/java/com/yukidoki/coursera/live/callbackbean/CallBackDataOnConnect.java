package com.yukidoki.coursera.live.callbackbean;

import lombok.Data;

@Data
public class CallBackDataOnConnect {
    private String action;
    private String client_id;
    private String ip;
    private String vhost;
    private String app;
    private String stream;
    private String tcUrl;
    private String pageUrl;
    private String param;
}
