package com.sdr.lib.util;

import android.text.TextUtils;

/**
 * Created by HyFun on 2018/10/31.
 * Email:775183940@qq.com
 */

public class HttpUtil {
    private HttpUtil() {
    }

    /**
     * 去掉请求协议头和端口
     *
     * @param domainAddress
     * @return 纯IP
     */
    public static String clearDomainAddress(String domainAddress) {
        if (TextUtils.isEmpty(domainAddress)) {
            return null;
        }
        String ipAddress = "";
        //兼容http://开头的地址格式
        if (domainAddress.contains("http://") || domainAddress.contains("https://")) {
            String[] splits = domainAddress.split("//");
            if (splits.length >= 2) {
                domainAddress = splits[1];
            }
        }
        if (domainAddress.contains(":")) {//例：10.33.27.240:81或10.33.27.240:81/msp无法解析
            String[] str_address = domainAddress.split(":");
            ipAddress = str_address[0];
            return ipAddress;
        } else if (!domainAddress.contains(":") && domainAddress.contains("/")) {//例如10.33.27.240/msp无法解析
            String[] str_address = domainAddress.split("/");
            ipAddress = str_address[0];
            return ipAddress;
        }
        return domainAddress;
    }
}
