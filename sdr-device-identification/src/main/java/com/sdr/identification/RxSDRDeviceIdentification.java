package com.sdr.identification;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import rx_activity_result2.Result;
import rx_activity_result2.RxActivityResult;

/**
 * Created by HyFun on 2019/05/16.
 * Email: 775183940@qq.com
 * Description:
 */

public class RxSDRDeviceIdentification {
    private RxSDRDeviceIdentification() {
    }

    //    private FragmentActivity activity;
//    private Fragment fragment;
//
//    public RxSDRDeviceIdentification(FragmentActivity activity) {
//        this.activity = activity;
//    }
//
//    public RxSDRDeviceIdentification(Fragment fragment) {
//        this.fragment = fragment;
//    }
//
//    /**
//     * 调用扫描二维码
//     */
//    public Observable<String> scan() {
//        // 授权
//        return getRxPermissions()
//                .request(Manifest.permission.CAMERA)
//                .flatMap(new Function<Boolean, ObservableSource<String>>() {
//                    @Override
//                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//                            if (activity != null) {
//                                return getRxActivityResult(activity)
//                                        .startIntent(new Intent(activity, SDRScanQRCodeActivity.class))
//                                        .flatMap(new Function<Result<FragmentActivity>, ObservableSource<String>>() {
//                                            @Override
//                                            public ObservableSource<String> apply(Result<FragmentActivity> fragmentActivityResult) throws Exception {
//                                                int resultCode = fragmentActivityResult.resultCode();
//                                                if (resultCode == Activity.RESULT_OK) {
//                                                    Bundle bundle = fragmentActivityResult.data().getExtras();
//                                                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                                                    return RxUtils.createData(result);
//                                                } else if (resultCode == Activity.RESULT_CANCELED) {
//                                                    return Observable.error(new Exception("扫描取消"));
//                                                } else {
//                                                    return Observable.error(new Exception("扫描失败"));
//                                                }
//                                            }
//                                        });
//                            } else {
//                                return getRxActivityResult(fragment)
//                                        .startIntent(new Intent(fragment.getContext(), SDRScanQRCodeActivity.class))
//                                        .flatMap(new Function<Result<Fragment>, ObservableSource<String>>() {
//                                            @Override
//                                            public ObservableSource<String> apply(Result<Fragment> fragmentResult) throws Exception {
//                                                int resultCode = fragmentResult.resultCode();
//                                                if (resultCode == Activity.RESULT_OK) {
//                                                    Bundle bundle = fragmentResult.data().getExtras();
//                                                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                                                    return RxUtils.createData(result);
//                                                } else if (resultCode == Activity.RESULT_CANCELED) {
//                                                    return Observable.error(new Exception("扫描取消"));
//                                                } else {
//                                                    return Observable.error(new Exception("扫描失败"));
//                                                }
//                                            }
//                                        });
//                            }
//                        } else {
//                            return Observable.error(new Exception("获取摄像头权限失败"));
//                        }
//                    }
//                });
//    }
//
//    public Observable<String> nfc() {
//        if (activity != null) {
//            return getRxActivityResult(activity)
//                    .startIntent(new Intent(activity, SDRIdentifyNFCActivity.class))
//                    .flatMap(new Function<Result<FragmentActivity>, ObservableSource<String>>() {
//                        @Override
//                        public ObservableSource<String> apply(Result<FragmentActivity> fragmentActivityResult) throws Exception {
//                            int code = fragmentActivityResult.resultCode();
//                            if (code == Activity.RESULT_OK) {
//                                String c = fragmentActivityResult.data().getStringExtra("NFC");
//                                return RxUtils.createData(c);
//                            } else if (code == Activity.RESULT_CANCELED) {
//                                return Observable.error(new Exception("扫描取消"));
//                            } else {
//                                return Observable.error(new Exception("扫描失败"));
//                            }
//                        }
//                    });
//        } else {
//            return getRxActivityResult(fragment)
//                    .startIntent(new Intent(fragment.getContext(), SDRIdentifyNFCActivity.class))
//                    .flatMap(new Function<Result<Fragment>, ObservableSource<String>>() {
//                        @Override
//                        public ObservableSource<String> apply(Result<Fragment> fragmentResult) throws Exception {
//                            int code = fragmentResult.resultCode();
//                            if (code == Activity.RESULT_OK) {
//                                String c = fragmentResult.data().getStringExtra("NFC");
//                                return RxUtils.createData(c);
//                            } else if (code == Activity.RESULT_CANCELED) {
//                                return Observable.error(new Exception("扫描取消"));
//                            } else {
//                                return Observable.error(new Exception("扫描失败"));
//                            }
//                        }
//                    });
//        }
//    }
//
//    public <T extends Object> Observable<Result<T>> n() {
//        if (activity != null) {
//            return getRxActivityResult(activity)
//                    .startIntent(new Intent(activity, SDRIdentifyNFCActivity.class))
//                    .flatMap(new Function<Result<FragmentActivity>, ObservableSource<Result<T>>>() {
//                        @Override
//                        public ObservableSource<Result<T>> apply(Result<FragmentActivity> fragmentActivityResult) throws Exception {
//                            return RxUtils.createData(fragmentActivityResult);
//                        }
//                    });
//        } else {
//            return getRxActivityResult(fragment)
//                    .startIntent(new Intent(fragment.getContext(), SDRIdentifyNFCActivity.class));
//        }
//    }
//
//    // ———————————————————————————私有方法———————————————————————————
//    private RxPermissions getRxPermissions() {
//        if (activity != null) {
//            return new RxPermissions(activity);
//        } else {
//            return new RxPermissions(fragment);
//        }
//    }
//
//
//    private RxActivityResult.Builder<FragmentActivity> getRxActivityResult(FragmentActivity activity) {
//        return RxActivityResult.on(activity);
//    }
//
//    private RxActivityResult.Builder<Fragment> getRxActivityResult(Fragment fragment) {
//        return RxActivityResult.on(fragment);
//    }


    /**
     * 在activity中扫描
     *
     * @param activity
     * @return
     */
    public static Observable<Result<FragmentActivity>> scan(final FragmentActivity activity) {
        return new RxPermissions(activity)
                .request(Manifest.permission.CAMERA)
                .flatMap(new Function<Boolean, ObservableSource<Result<FragmentActivity>>>() {
                    @Override
                    public ObservableSource<Result<FragmentActivity>> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return RxActivityResult.on(activity)
                                    .startIntent(new Intent(activity, SDRScanQRCodeActivity.class));
                        } else {
                            return Observable.error(new Exception("获取摄像头权限失败"));
                        }
                    }
                });
    }

    /**
     * 在fragment中扫描
     *
     * @param fragment
     * @return
     */
    public static Observable<Result<Fragment>> scan(final Fragment fragment) {
        return new RxPermissions(fragment)
                .request(Manifest.permission.CAMERA)
                .flatMap(new Function<Boolean, ObservableSource<Result<Fragment>>>() {
                    @Override
                    public ObservableSource<Result<Fragment>> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return RxActivityResult.on(fragment)
                                    .startIntent(new Intent(fragment.getContext(), SDRScanQRCodeActivity.class));
                        } else {
                            return Observable.error(new Exception("获取摄像头权限失败"));
                        }
                    }
                });
    }

    /**
     * 扫描nfc activity
     *
     * @param activity
     * @return
     */
    public static Observable<Result<FragmentActivity>> nfc(FragmentActivity activity) {
        return RxActivityResult.on(activity)
                .startIntent(new Intent(activity, SDRIdentifyNFCActivity.class));
    }

    /**
     * 扫描nfc fragment
     *
     * @param fragment
     * @return
     */
    public static Observable<Result<Fragment>> nfc(Fragment fragment) {
        return RxActivityResult.on(fragment)
                .startIntent(new Intent(fragment.getContext(), SDRIdentifyNFCActivity.class));
    }


    /**
     * 获取蓝牙设备列表
     *
     * @return
     */
    public static Observable<List<BluetoothDevice>> bluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return Observable.error(new Exception("当前设备不支持蓝牙功能"));
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                return Observable.error(new Exception("请先打开您的蓝牙"));
            } else {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
                    bluetoothDeviceList.addAll(pairedDevices);
                    return Observable.just(bluetoothDeviceList);
                } else {
                    return Observable.error(new Exception("您至少需要连接一台蓝牙设备"));
                }
            }
        }
    }


    /**
     * 获取已连接的Wifi路由器的Mac地址
     */
    public static Observable<String> wifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (wifiManager == null) {
            return Observable.error(new Exception("当前设备不支持WIFI功能"));
        }

        if (!wifiManager.isWifiEnabled()) {
            return Observable.error(new Exception("请先打开WIFI"));
        }
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mWifiInfo.isConnected()) {
            return Observable.error(new Exception("至少需要连接一个WIFI热点"));
        }

        WifiInfo info = wifiManager.getConnectionInfo();
        List<ScanResult> wifiList = wifiManager.getScanResults();
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult result = wifiList.get(i);
            if (info.getBSSID().equals(result.BSSID)) {
                return Observable.just(result.BSSID);
            }
        }
        return Observable.error(new Exception("获取WIFI MAC地址失败"));
    }


    /**
     * 生成二维码
     */
    public static Bitmap createQRImage(String text, int width, Bitmap logo) {
        return CodeUtils.createImage(text, width, width, logo);
    }

    public static Bitmap createQRImage(String text, int width) {
        return CodeUtils.createImage(text, width, width, null);
    }


    public static class Helper {
        public static String getScanResult(Result result) {
            int resultCode = result.resultCode();
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = result.data().getExtras();
                String re = bundle.getString(CodeUtils.RESULT_STRING);
                return re;
            }
            return null;
        }


        public static String getNfcResult(Result result) {
            int resultCode = result.resultCode();
            if (resultCode == Activity.RESULT_OK) {
                return result.data().getStringExtra("NFC");
            }
            return null;
        }
    }
}
