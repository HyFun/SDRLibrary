package com.sdr.lib.ui.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

class Node {
    // 该node的id
    private String id;
    // 该node 的 parent id
    private String pId;
    // 标题
    private String name;
    // checkbox是否选中
    private boolean isChecked;
    // 是否是叶子节点
    private boolean isLeaf;

    public Node(String id, String pId, String name, boolean isChecked, boolean isLeaf) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.isChecked = isChecked;
        this.isLeaf = isLeaf;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    /**
     * 树 的层级
     */
    private int level;

    /**
     * 是否是展开状态
     */
    private boolean isExpand;

    private int icon;

    private Node parent;

    private List<Node> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
        if (!expand) {
            for (Node node : children) {
                node.setExpand(false);
            }
        }
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public boolean isRoot() {
        return parent == null;
    }


    public boolean isParentExpand() {
        if (parent == null) return false;
        return parent.isExpand();
    }

    public boolean isLeaf() {
        return children.size() == 0 && isLeaf;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", pId=" + pId +
                ", name='" + name + '\'' +
                ", isChecked=" + isChecked +
                ", isLeaf=" + isLeaf +
                '}';
    }
}
