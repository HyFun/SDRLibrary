package com.sdr.lib.ui.tree;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdr.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/13 0013.
 */

public class TreeNodeRecyclerAdapter extends RecyclerView.Adapter<TreeNodeRecyclerAdapter.ViewHolder> {

    // 默认打开的级别  0 为顶级
    public static final int DEFAULT_LEVEL = 0;

    private List<Node> allNodes;
    private List<Node> visibleNodes;
    private List<TreeNode> realDatas;

    private LayoutInflater mLayoutInflater;
    private OnTreeNodeSigleClickListener onTreeNodeSigleClickListener;
    private OnTreeNodeMultiClickListener onTreeNodeMultiClickListener;

    public TreeNodeRecyclerAdapter(Context context, List<TreeNode> datas, OnTreeNodeSigleClickListener onTreeNodeSigleClickListener) throws Exception {
        this(context, datas, onTreeNodeSigleClickListener, DEFAULT_LEVEL);
    }

    public TreeNodeRecyclerAdapter(Context context, List<TreeNode> datas, OnTreeNodeMultiClickListener onTreeNodeMultiClickListener) throws Exception {
        this(context, datas, onTreeNodeMultiClickListener, DEFAULT_LEVEL);
    }

    public TreeNodeRecyclerAdapter(Context context, List<TreeNode> datas, OnTreeNodeSigleClickListener onTreeNodeSigleClickListener, int defaultLevel) throws Exception {
        mLayoutInflater = LayoutInflater.from(context);
        realDatas = datas;
        this.onTreeNodeSigleClickListener = onTreeNodeSigleClickListener;
        allNodes = TreeHelper.getSortedNodes(datas, defaultLevel);
        visibleNodes = TreeHelper.filterVisibleNodes(allNodes);
    }

    public TreeNodeRecyclerAdapter(Context context, List<TreeNode> datas, OnTreeNodeMultiClickListener onTreeNodeMultiClickListener, int defaultLevel) throws Exception {
        mLayoutInflater = LayoutInflater.from(context);
        realDatas = datas;
        this.onTreeNodeMultiClickListener = onTreeNodeMultiClickListener;
        allNodes = TreeHelper.getSortedNodes(datas, defaultLevel);
        visibleNodes = TreeHelper.filterVisibleNodes(allNodes);
    }

    private void expandOrCollapse(int position) {
        Node node = visibleNodes.get(position);
        if (node != null) {
            if (node.isLeaf()) return;
            node.setExpand(!node.isExpand());
            visibleNodes = TreeHelper.filterVisibleNodes(allNodes);
            notifyDataSetChanged();
        }
    }

    /**
     * 通过id 在真实数据中找到TreeNode
     *
     * @param id
     * @return
     */
    private TreeNode getTreeNodeById(String id) {
        for (TreeNode treeNode : realDatas) {
            if (treeNode.getId().equals(id))
                return treeNode;
        }
        return null;
    }

    /**
     * 通过treenode在真实数据中找到真实位置
     *
     * @param treeNode
     * @return
     */
    private int getRealDatasPosition(TreeNode treeNode) {
        if (treeNode == null) return 0;
        for (int i = 0; i < realDatas.size(); i++) {
            if (treeNode.getId().equals(realDatas.get(i).getId())) {
                return i;
            }
        }
        return 0;
    }


    private List<TreeNode> getCheckList() {
        List<TreeNode> list = new ArrayList<>();
        for (Node node : allNodes) {
            if (node.isChecked() && node.isLeaf()) {
                list.add(getTreeNodeById(node.getId()));
            }
        }
        return list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.sdr_layout_public_tree_view_node_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final int currentPosition = position;
        final Node node = visibleNodes.get(currentPosition);
        final TreeNode treeNode = getTreeNodeById(node.getId());
        // 设置图标的显示与隐藏
        if (node.getIcon() == TreeHelper.DEFAULT_EXPAND_ICON) {
            holder.mIcon.setVisibility(View.GONE);
        } else {
            holder.mIcon.setVisibility(View.VISIBLE);
            holder.mIcon.setImageResource(node.getIcon());
        }
        holder.itemView.setPadding(node.getLevel() * 50, 0, 0, 0);
        String label = node.getName();
        holder.mTextView.setText(TextUtils.isEmpty(label) ? "" : label);

        // 设置checkbox的显示与隐藏
        holder.mCheckBox.setVisibility(onTreeNodeMultiClickListener != null && node.isLeaf() ? View.VISIBLE : View.GONE);
        // 设置checkbox是否选中
        holder.mCheckBox.setChecked(node.isChecked());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 被点击选中的treenode
                TreeNode treeNode = getTreeNodeById(visibleNodes.get(currentPosition).getId());
                int realDatasPosition = getRealDatasPosition(treeNode);
                if (onTreeNodeSigleClickListener != null) {
                    onTreeNodeSigleClickListener.onSigleClick(treeNode, currentPosition, realDatasPosition, node.isLeaf());
                }
                if (onTreeNodeMultiClickListener != null) {
                    // 点击选中
                    node.setChecked(!node.isChecked());
                    treeNode.setChecked(!treeNode.isChecked());
                    holder.mCheckBox.setChecked(node.isChecked());
                    onTreeNodeMultiClickListener.onMultiClick(getCheckList(), treeNode, currentPosition, realDatasPosition, node.isLeaf());
                }
                expandOrCollapse(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return visibleNodes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon;
        TextView mTextView;
        CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.hyf_tree_view_item_iv_expand);
            mTextView = (TextView) itemView.findViewById(R.id.hyf_tree_view_item_tv_title);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.hyf_tree_view_item_cb);
        }
    }

    public interface OnTreeNodeSigleClickListener {
        void onSigleClick(TreeNode treeNode, int visablePositon, int realDatasPositon, boolean isLeaf);
    }

    public interface OnTreeNodeMultiClickListener {
        void onMultiClick(List<TreeNode> selectList, TreeNode treeNode, int visablePositon, int realDatasPositon, boolean isLeaf);
    }
}
