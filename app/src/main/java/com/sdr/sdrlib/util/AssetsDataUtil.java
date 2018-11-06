package com.sdr.sdrlib.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.sdr.sdrlib.app.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by HyFun on 2018/10/25.
 * Email:775183940@qq.com
 */

public class AssetsDataUtil {

    public static String getAssetsData(String jsonName) {
        Context context = MyApplication.getApplication().getApplicationContext();
        try {
            AssetManager manager = context.getResources().getAssets();
            InputStream inputStream = manager.open("data/" + jsonName);
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    /**
     * 将inputstream 转成 string
     *
     * @param is
     * @return 转换后的字符串
     */
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
