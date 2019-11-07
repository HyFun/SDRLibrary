package com.sdr.sdrlib.ui.main;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.sdr.lib.support.menu.MenuItem;
import com.sdr.lib.support.menu.OnClickMenuItemListener;
import com.sdr.lib.util.AlertUtil;
import com.sdr.sdrlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HyFun on 2019/04/30.
 * Email: 775183940@qq.com
 * Description:
 */

public class AppMenu {
    private static final int PAGE_SIZE = 8;
    private Activity activity;

    public AppMenu(Activity activity) {
        this.activity = activity;
    }


    /**
     * 获取副菜单列表
     *
     * @return
     */
    public final List<List<MenuItem>> getSecondMenuList() {
        final List<List<MenuItem>> menuList = new ArrayList<>();
        List<MenuItem> secondMenus = new ArrayList<>();
        int titleColor = activity.getResources().getColor(R.color.colorBlack);
        // ————————————————————————菜单分割线 start————————————————————————————————

        // 全景显示
        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#E1B183"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#CCCA22"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#22CD29"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#22CDA7"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));


        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#22AACD"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));


        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#2247CD"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#6522CD"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#9D22CD"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));


        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#CD2299"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#CD2261"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));


        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#EE9D71"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#BEA654"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#94BE54"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#4CB445"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#45B49B"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));

        secondMenus.add(new MenuItem(R.mipmap.main_home_second_menu_qjt, Color.parseColor("#4E6E96"), "全景显示", titleColor, new OnClickMenuItemListener() {
            @Override
            public void onClick(View view, MenuItem menuItem) {
                AlertUtil.showPositiveToastTop(menuItem.getTitle(), "");
            }
        }));


        // ————————————————————————菜单分割线 end————————————————————————————————
        int pageNum = secondMenus.size() % PAGE_SIZE > 0 ? (secondMenus.size() / PAGE_SIZE) + 1 : secondMenus.size() / PAGE_SIZE;
        for (int i = 0; i < pageNum; i++) {
            List<MenuItem> newList = new ArrayList<>();
            for (int j = i * PAGE_SIZE; j < (i != pageNum - 1 ? (i + 1) * PAGE_SIZE : i * PAGE_SIZE + (secondMenus.size() % PAGE_SIZE == 0 ? PAGE_SIZE : secondMenus.size() % PAGE_SIZE)); j++) {
                newList.add(secondMenus.get(j));
            }
            menuList.add(newList);
        }
        return menuList;
    }


    // ——————————————————————PRIVATE——————————————————————————

    public Activity getActivity() {
        return activity;
    }
}
