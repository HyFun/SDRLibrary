package com.sdr.lib.ui.tree;


/**
 * Created by Administrator on 2018/5/13 0013.
 */

public class TreeNode {
    @TreeNodeId
    private String id;

    @TreeNodeParentId
    private String parentId;

    @TreeNodeLabel
    private String label;

    @TreeNodeCheckBox
    private boolean isChecked;

    @TreeNodeIsLeaf
    private boolean isLeaf = true;

    private Object object;

    public TreeNode(String id, String parentId, String label, boolean isChecked, Object object) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
        this.isChecked = isChecked;
        this.object = object;
    }

    public TreeNode(String id, String parentId, String label, boolean isChecked, boolean isLeaf, Object object) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
        this.isChecked = isChecked;
        this.isLeaf = isLeaf;
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", label='" + label + '\'' +
                ", isChecked=" + isChecked +
                ", isLeaf=" + isLeaf +
                ", object=" + object +
                '}';
    }
}
