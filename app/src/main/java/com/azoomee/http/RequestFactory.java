package com.azoomee.http;

import org.androidannotations.annotations.EBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@EBean
public class RequestFactory extends HttpComponentsClientHttpRequestFactory {
    private static final int TIME_OUT = 3000;

    public RequestFactory() {
        setConnectTimeout(TIME_OUT);
        setReadTimeout(TIME_OUT);
    }
}
