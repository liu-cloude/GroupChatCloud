package com.xingwang.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingwang.groupchat.R;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.utils.GlideUtils;
import com.xingwang.groupchat.view.CircularImage;

import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class GroupTransferAdapter extends BaseAdapter {
    private List<User> list;
    private Context context;
    private LayoutInflater linearLayout;

    public GroupTransferAdapter(Context context, List<User> teammates) {
        this.list=teammates;
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public User getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = linearLayout.inflate(R.layout.groupchat_item_transfer_group, null);
            viewHolder = new ViewHolder();
            viewHolder.img_head =convertView.findViewById(R.id.img_head);
            viewHolder.tv_group_name = convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user=getItem(position);

        GlideUtils.loadAvatar(user.getAvatar(),viewHolder.img_head,context);
        viewHolder.tv_group_name.setText(user.getNickname());

        return convertView;
    }

    class ViewHolder {
        private CircularImage img_head;
        private TextView tv_group_name;
    }
}
