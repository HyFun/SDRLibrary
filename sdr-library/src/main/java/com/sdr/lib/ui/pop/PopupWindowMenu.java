package com.sdr.lib.ui.pop;

/**
 * Created by Administrator on 2018/4/13.
 */

public class PopupWindowMenu {
    private int id;
    private int iconRes;
    private String title;
    private PopWindowMenuHelper.OnItemClickListener onItemClickListener;

    public PopupWindowMenu(int id, String title, PopWindowMenuHelper.OnItemClickListener onItemClickListener) {
        this.id = id;
        this.title = title;
        this.onItemClickListener = onItemClickListener;
    }

    public PopupWindowMenu(int id, int iconRes, String title, PopWindowMenuHelper.OnItemClickListener onItemClickListener) {
        this.id = id;
        this.iconRes = iconRes;
        this.title = title;
        this.onItemClickListener = onItemClickListener;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PopWindowMenuHelper.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(PopWindowMenuHelper.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
