package com.sdr.identification;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.orhanobut.logger.Logger;
import com.sdr.lib.http.HttpClient;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CodeUtils;

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


    public static String bluetooth(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Logger.e("此设备不支持蓝牙传输功能");
        } else {
            Logger.d("此设备支持蓝牙传输功能");
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                context.startActivity(enableIntent);
            }
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : pairedDevices) {
                Logger.d(bluetoothDevice.getName()+">>>>>>>>>>>"+bluetoothDevice.getAddress()+">>>>>>>"+bluetoothDevice.getBondState());
            }
        }

        return null;
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
