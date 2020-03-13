package com.cdqf.dire_okhttp;

public interface OnHttpRequest {

    void onOkHttpResponse(String response, int id);

    void onOkHttpError(String error);
}
