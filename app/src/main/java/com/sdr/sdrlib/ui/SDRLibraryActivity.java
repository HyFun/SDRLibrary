package com.sdr.sdrlib.ui;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.support.fingerprint.BiometricPromptManager;
import com.sdr.lib.support.update.AppNeedUpdateListener;
import com.sdr.lib.support.weather.Weather;
import com.sdr.lib.ui.tree.TreeNode;
import com.sdr.lib.ui.tree.TreeNodeRecyclerAdapter;
import com.sdr.lib.util.AlertUtil;
import com.sdr.lib.util.CommonUtil;
import com.sdr.lib.util.NotificationUtils;
import com.sdr.lib.util.ToastTopUtil;
import com.sdr.lib.util.ToastUtil;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.common.AppItemRecyclerAdapter;
import com.sdr.sdrlib.common.MainItem;
import com.sdr.sdrlib.entity.Person;
import com.sdr.sdrlib.ui.basefragment.LazyBaseFragmentActivity;
import com.sdr.sdrlib.ui.lazyfragment.LazyFragmentActivity;
import com.sdr.sdrlib.ui.main.MainModeOneActivity;
import com.sdr.sdrlib.ui.main.MainModeTwoActivity;
import com.sdr.sdrlib.ui.marquee.MarqueeViewActivity;
import com.sdr.sdrlib.util.AppUtil;
import com.sdr.sdrlib.util.AssetsDataUtil;
import com.sdr.sdrlib.util.NotificationIdHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceObserver;

/**
 * Created by HyFun on 2019/05/16.
 * Email: 775183940@qq.com
 * Description:
 */

public class SDRLibraryActivity extends BaseActivity {
    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SDRLibraryActivity");
        setDisplayHomeAsUpEnabled();

        AppItemRecyclerAdapter adapter = AppItemRecyclerAdapter.setAdapter(recyclerView);

        // 检查更新
        adapter.addData(new MainItem("检查APP更新(bpm)", v -> {
            AppUtil.checkUpdate(getActivity(), true, new AppNeedUpdateListener() {
                @Override
                public void isNeedUpdate(boolean need) {
                    Logger.d("APP是否需要更新>>>>>>>>>" + need);
                }
            });
        }));

        // tree  单选
        adapter.addData(new MainItem("tree结构列表(单选)", v -> {
            String json = AssetsDataUtil.getAssetsData("tree.json");
            List<Person> personList = HttpClient.gson.fromJson(json, new TypeToken<List<Person>>() {
            }.getType());
            List<TreeNode> treeNodeList = new ArrayList<>();
            for (Person per : personList) {
                treeNodeList.add(new TreeNode(per.getId(), per.getPId(), TextUtils.isEmpty(per.getName()) ? per.getRealname() : per.getName(), false, per));
            }
            try {
                TreeNodeRecyclerAdapter treeNodeRecyclerAdapter = new TreeNodeRecyclerAdapter(getContext(), treeNodeList, new TreeNodeRecyclerAdapter.OnTreeNodeSigleClickListener() {
                    @Override
                    public void onSigleClick(TreeNode treeNode, int visablePositon, int realDatasPositon, boolean isLeaf) {
                        if (!isLeaf) return;
                        ToastUtil.showNormalToast(treeNode.getLabel());
                    }
                });
                RecyclerView recyclerView = new RecyclerView(getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(treeNodeRecyclerAdapter);
                new MaterialDialog.Builder(getContext())
                        .title("选择人员")
                        .customView(recyclerView, false)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // tree  多选
        adapter.addData(new MainItem("tree结构列表(多选)", v -> {
            String json = AssetsDataUtil.getAssetsData("tree.json");
            List<Person> personList = HttpClient.gson.fromJson(json, new TypeToken<List<Person>>() {
            }.getType());
            List<TreeNode> treeNodeList = new ArrayList<>();
            for (Person per : personList) {
                treeNodeList.add(new TreeNode(per.getId(), per.getPId(), TextUtils.isEmpty(per.getName()) ? per.getRealname() : per.getName(), false, per));
            }
            try {
                TreeNodeRecyclerAdapter treeNodeRecyclerAdapter = new TreeNodeRecyclerAdapter(getContext(), treeNodeList, new TreeNodeRecyclerAdapter.OnTreeNodeMultiClickListener() {
                    @Override
                    public void onMultiClick(List<TreeNode> selectList, TreeNode treeNode, int visablePositon, int realDatasPositon, boolean isLeaf) {
                        if (!isLeaf) return;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectList.size(); i++) {
                            sb.append(selectList.get(i).getLabel());
                            if (i != selectList.size() - 1) {
                                sb.append(",");
                            }
                        }
                        ToastUtil.showNormalToast(sb.toString());
                    }
                });
                RecyclerView recyclerView = new RecyclerView(getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(treeNodeRecyclerAdapter);
                new MaterialDialog.Builder(getContext())
                        .title("选择人员")
                        .customView(recyclerView, false)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        adapter.addData(new MainItem("懒加载fragment", v -> {
            startActivity(new Intent(getContext(), LazyFragmentActivity.class));
        }));

        adapter.addData(new MainItem("懒加载basefragment", v -> {
            startActivity(new Intent(getContext(), LazyBaseFragmentActivity.class));
        }));

        // 下载图片
        adapter.addData(new MainItem("预览并下载图片", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List images = new ArrayList<>();
                images.add("http://b.hiphotos.baidu.com/imgad/pic/item/9f510fb30f2442a750999faed943ad4bd1130221.jpg");
                images.add("http://b.hiphotos.baidu.com/imgad/pic/item/80cb39dbb6fd5266ac09511fa318972bd407369e.jpg");
                images.add("http://b.hiphotos.baidu.com/imgad/pic/item/8644ebf81a4c510f372b80116859252dd52aa5ca.jpg");
                images.add("http://b.hiphotos.baidu.com/imgad/pic/item/500fd9f9d72a6059d4ff83112034349b023bbaca.jpg");
                images.add("http://b.hiphotos.baidu.com/imgad/pic/item/29381f30e924b89914e91b3366061d950b7bf6e0.jpg");
                images.add("http://b.hiphotos.baidu.com/imgad/pic/item/09fa513d269759eee9c702c0bafb43166d22df22.jpg");
                images.add("http://b.hiphotos.baidu.com/imgad/pic/item/2e2eb9389b504fc2f3b1d20eeddde71191ef6dca.jpg");
                images.add("https://img.zcool.cn/community/012bc0585250e8a801219c77cf3db4.jpg@1280w_1l_0o_100sh.jpg");
                images.add("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=e2b9f9478013632701e0ca61f0e6cb89/8644ebf81a4c510f2a9f87816a59252dd52aa5d6.jpg");
                images.add(R.mipmap.big_width);
                CommonUtil.viewImageList(getContext(), false, 2, images);
            }
        }));

        adapter.addData(new MainItem("Toast 正常", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showNormalToast("正常的toast");
            }
        }));

        adapter.addData(new MainItem("Toast 正确", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showPositiveToast("Toast 正确");
            }
        }));

        adapter.addData(new MainItem("Toast 错误", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showNegativeToast("Toast 错误");
            }
        }));

        adapter.addData(new MainItem("Toast 长内容", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showNegativeToast(getResources().getString(R.string.long_string));
            }
        }));

        adapter.addData(new MainItem("Alert 简单Dialog", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtil.showDialog(getActivity(), "提示", "登录失败，密码错误!!!");
            }
        }));

        adapter.addData(new MainItem("Alert 长Dialog", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtil.showDialog(getActivity(), "提示", getResources().getString(R.string.long_string));
            }
        }));


        adapter.addData(new MainItem("TopToast 正常", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastTopUtil.showNormalTopToast("正常的TopToast");
            }
        }));

        adapter.addData(new MainItem("TopToast 成功", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastTopUtil.showPositiveTopToast("成功的TopToast");
            }
        }));

        adapter.addData(new MainItem("TopToast 错误", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastTopUtil.showNegativeTopToast("错误的TopToast");
            }
        }));

        adapter.addData(new MainItem("跑马灯标题", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MarqueeViewActivity.class));
            }
        }));


        adapter.addData(new MainItem("通知栏提示", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NotificationUtils(getContext(), R.drawable.ic_notification)
                        .sendNotification(NotificationIdHelper.getInstance().getNotifyId(), "您有新的消息", "在火的一步之遥处吟唱,我一度清楚,有人知道你身在何处");
            }
        }));

        BiometricPromptManager manager = BiometricPromptManager.from(getActivity());

        adapter.addData(new MainItem("验证指纹", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.isHardwareDetected()) {
                    if (manager.hasEnrolledFingerprints()) {
                        manager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                            @Override
                            public void onUsePassword() {

                            }

                            @Override
                            public void onSucceeded() {
                                ToastUtil.showPositiveToast("验证通过");
                            }

                            @Override
                            public void onFailed() {

                            }

                            @Override
                            public void onError(int code, String reason) {

                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    } else {
                        ToastUtil.showNegativeToast("您的设备没有设置指纹，请先设置指纹");
                    }
                } else {
                    ToastUtil.showNegativeToast("您的设备不支持指纹");
                }
            }
        }));

        adapter.addData(new MainItem("获取天气数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.getWeather(getActivity(),new ResourceObserver<Weather>() {
                    @Override
                    public void onNext(Weather weather) {
                        AlertUtil.showDialog(getActivity(), "获取天气成功", HttpClient.gson.toJson(weather));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }));


        adapter.addData(new MainItem("主页面Mode One", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainModeOneActivity.class));
            }
        }));

        adapter.addData(new MainItem("主页面Mode Two", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainModeTwoActivity.class));
            }
        }));


        adapter.addData(new MainItem("原生定位", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxPermissions(getActivity())
                        .request(
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        .flatMap(new Function<Boolean, ObservableSource<Location>>() {
                            @Override
                            public ObservableSource<Location> apply(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    return RxUtils.createData(CommonUtil.getLocation(getActivity()));
                                } else {
                                    return Observable.error(new Exception("获取定位权限失败"));
                                }
                            }
                        })
                        .subscribe(new Consumer<Location>() {
                            @Override
                            public void accept(Location location) throws Exception {
                                String json = HttpClient.gson.toJson(location);
                                Logger.json(json);
                                AlertUtil.showPositiveToast(json);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AlertUtil.showNegativeToastTop(throwable.getMessage());
                            }
                        });
            }
        }));

        adapter.addData(new MainItem("系统Toast", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SDRLibraryActivity.this, "这是系统自带的toast", Toast.LENGTH_SHORT).show();
            }
        }));

    }
}
