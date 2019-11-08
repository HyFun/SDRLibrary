package com.sdr.lib.util;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.sdr.lib.SDR_LIBRARY;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HyFun on 2019/11/8.
 * Email: 775183940@qq.com
 * Description:
 */
public class ImageUtil {
    private ImageUtil() {
    }

    /**
     * 生成文件名称： 20191108_110623
     *
     * @return
     */
    public static final String timeName() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }


    /**
     * 通知系统相册更新了
     */
    public static final void notifyAlbumDataChanged(File file) {
        //通知相册更新
        MediaStore.Images.Media.insertImage(SDR_LIBRARY.getInstance().getApplication().getContentResolver(), BitmapFactory.decodeFile(file.getAbsolutePath()), file.getName(), null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        SDR_LIBRARY.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }


}
