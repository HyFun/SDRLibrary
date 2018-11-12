package com.sdr.sdrlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.support.update.AppNeedUpdateListener;
import com.sdr.lib.ui.tree.TreeNode;
import com.sdr.lib.ui.tree.TreeNodeRecyclerAdapter;
import com.sdr.lib.util.CommonUtil;
import com.sdr.lib.util.ToastTopUtil;
import com.sdr.lib.util.ToastUtil;
import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.entity.Person;
import com.sdr.sdrlib.ui.lazyfragment.LazyFragmentActivity;
import com.sdr.sdrlib.util.AppUtil;
import com.sdr.sdrlib.util.AssetsDataUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);


        MainRecycleAdapter adapter = new MainRecycleAdapter(R.layout.layout_main_recycler_item, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


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
                        ToastUtil.showNormalMsg(treeNode.getLabel());
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
                        ToastUtil.showNormalMsg(sb.toString());
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
                ToastUtil.showNormalMsg("正常的toast");
            }
        }));

        adapter.addData(new MainItem("Toast 正确", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showCorrectMsg("Toast 正确");
            }
        }));

        adapter.addData(new MainItem("Toast 错误", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showErrorMsg("Toast 错误");
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
                ToastTopUtil.showCorrectTopToast("成功的TopToast");
            }
        }));

        adapter.addData(new MainItem("TopToast 错误", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastTopUtil.showErrorTopToast("错误的TopToast");
            }
        }));
    }


    private class MainRecycleAdapter extends BaseQuickAdapter<MainItem, BaseViewHolder> {

        public MainRecycleAdapter(int layoutResId, @Nullable List<MainItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MainItem item) {
            TextView textView = (TextView) helper.itemView;
            textView.setText(item.getTitle());
            textView.setOnClickListener(item.getOnClickListener());
        }
    }


    private class MainItem {
        private String title;
        private View.OnClickListener onClickListener;

        public MainItem(String title, View.OnClickListener onClickListener) {
            this.title = title;
            this.onClickListener = onClickListener;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public View.OnClickListener getOnClickListener() {
            return onClickListener;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
    }
}
