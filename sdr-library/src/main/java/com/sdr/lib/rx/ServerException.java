package com.sdr.lib.rx;

/**
 * @author quchao
 * @date 2017/11/27
 */

public class ServerException extends Exception {

    private String code;

    public ServerException(String message, String code) {
        super(message);
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
