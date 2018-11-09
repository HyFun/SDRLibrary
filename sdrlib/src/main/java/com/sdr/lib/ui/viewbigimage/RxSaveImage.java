/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sdr.lib.ui.viewbigimage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 保存图片，重复插入图片提示已存在
 *
 * @author jingbin
 */
public class RxSaveImage {

    /**
     * 使用Glide进行下载图片
     *
     * @param context
     * @param savePath
     * @param url
     */
    public static void saveImageToGallery(Context context, String savePath, Object url) {
        Observable.just(url)
                .flatMap(new Function<Object, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(Object u) throws Exception {
                        if (TextUtils.isEmpty(savePath) || TextUtils.isEmpty(url.toString())) {
                            throw new NullPointerException("请检查图片路径");
                        }
                        File file = Glide.with(context)
                                .download(url)
                                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();
                        if (file == null) {
                            throw new Exception("无法下载到图片");
                        }
                        return observer -> {
                            observer.onNext(file);
                            observer.onComplete();
                        };
                    }
                })
                .flatMap(new Function<File, ObservableSource<Uri>>() {
                    @Override
                    public ObservableSource<Uri> apply(File resource) throws Exception {
                        // 生成UUID
                        File file = new File(savePath, UUID.randomUUID().toString().replaceAll("-", "") + ".jpg");
                        if (!file.exists()) file.createNewFile();
                        copyFileUsingFileChannels(resource, file);
                        Uri uri = Uri.fromFile(file);
                        // 通知图库更新
                        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                        context.sendBroadcast(scannerIntent);
                        return Observable.just(uri);
                    }
                })
                .compose(RxUtils.io_main())
                .subscribe(uri -> {
                    ToastUtil.showCorrectMsg("图片已保存至" + savePath);
                }, error -> {
                    ToastUtil.showErrorMsg(error.getMessage());
                });

    }


    /**
     * 拷贝文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

}
