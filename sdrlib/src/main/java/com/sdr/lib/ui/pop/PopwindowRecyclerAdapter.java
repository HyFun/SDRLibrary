package com.sdr.lib.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sdr.lib.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14 0014.
 */

class PopwindowRecyclerAdapter extends RecyclerView.Adapter<PopwindowRecyclerAdapter.PopwindowViewHolder> {
    private Context context;
    private List<PopupWindowMenu> list;
    private PopupWindow popupWindow;
    private LayoutInflater inflater;

    public PopwindowRecyclerAdapter(Context context, List<PopupWindowMenu> list, PopupWindow window) {
        this.context = context;
        this.list = list;
        this.popupWindow = window;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PopwindowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sdr_layout_public_popwindow_recycler_item, parent, false);
        PopwindowViewHolder viewHolder = new PopwindowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PopwindowViewHolder holder, final int position) {
        final PopupWindowMenu menu = list.get(position);
        int res = menu.getIconRes();
        int color = Color.parseColor("#737373");
        String title = menu.getTitle();
        if (res != 0) {
            holder.imageView.setImageResource(res);
            holder.imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.textView.setText(title);
        holder.textView.setTextColor(color);
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        // 设置点击事件
        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.getOnItemClickListener() != null) {
                    menu.getOnItemClickListener().onClickPopwindowMenuItem(v, menu, popupWindow, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PopwindowViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout containerView;

        public PopwindowViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.hyf_popwindow_recyclerview_item_iv_icon);
            textView = (TextView) itemView.findViewById(R.id.hyf_popwindow_recyclerview_item_tv_title);
            containerView = (LinearLayout) itemView.findViewById(R.id.hyf_popwindow_recyclerview_item_container);
        }
    }
}
