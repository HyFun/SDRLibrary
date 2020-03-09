package com.sdr.lib.support.update;

/**
 * Created by Administrator on 2017/8/21.
 */

@Deprecated
public class UpdateInfo {

    /**
     * MD5 : d7dcc54b7cb7dc9c961e497b2213c9a8
     * appAddr : http://58.240.174.254:8070/app/appVersion/downLoadAppApk/binjiang_4.apk
     * appName : binjiang
     * appSize : 5675722
     * code : 200
     * msg : success
     * updateDetail : 01.新增工程监测<br>02.新增水质分析<br>03.修复Android 7.0 更新安装程序崩溃
     * versionCode : 4
     * versionName : 1.0.3
     */

    private String MD5;
    private String appAddr;
    private String appName;
    private String appSize;
    private int code;
    private String msg;
    private String updateDetail;
    private int versionCode;
    private String versionName;

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getAppAddr() {
        return appAddr;
    }

    public void setAppAddr(String appAddr) {
        this.appAddr = appAddr;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUpdateDetail() {
        return updateDetail;
    }

    public void setUpdateDetail(String updateDetail) {
        this.updateDetail = updateDetail;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
