package com.sdr.lib.support.menu;

/**
 * Created by Administrator on 2018/3/2.
 */

public class MenuItem {
    private int imageRes;
    private int imgColor;
    private String title;
    private int titleColor;
    private OnClickMenuItemListener onClickMenuItemListener;
    private String badge;

    public MenuItem(int imageRes, int imgColor, String title, int titleColor, OnClickMenuItemListener onClickMenuItemListener) {
        this.imageRes = imageRes;
        this.imgColor = imgColor;
        this.title = title;
        this.titleColor = titleColor;
        this.onClickMenuItemListener = onClickMenuItemListener;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public int getImgColor() {
        return imgColor;
    }

    public void setImgColor(int imgColor) {
        this.imgColor = imgColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public OnClickMenuItemListener getOnClickMenuItemListener() {
        return onClickMenuItemListener;
    }

    public void setOnClickMenuItemListener(OnClickMenuItemListener onClickMenuItemListener) {
        this.onClickMenuItemListener = onClickMenuItemListener;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
