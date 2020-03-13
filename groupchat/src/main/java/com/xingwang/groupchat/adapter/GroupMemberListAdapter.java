package com.xingwang.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xingwang.groupchat.R;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.callback.OnItemClickListener;
import com.xingwang.groupchat.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.ViewHolder> implements View.OnClickListener{
    protected Context mContext;
    protected List<User> mDatas=new ArrayList<>();
    protected LayoutInflater mInflater;
    protected boolean leader;//默认为否

    public GroupMemberListAdapter(Context mContext, List<User> mDatas) {
        this.mContext = mContext;
        this.mDatas=mDatas;
        leader=false;
        mInflater = LayoutInflater.from(mContext);
    }

    private OnItemClickListener mOnItemClickListener = null;
    private OnCheckBoxClickListener boxClickListener = null;

    //模拟ListView的OnItemClickListener
    public interface OnCheckBoxClickListener{
        void onCheckClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.groupchat_item_group_select,parent,false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }
    public void setLeader(boolean leader) {
        this.leader = leader;
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick((int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setBoxClickListener(OnCheckBoxClickListener listener) {
        this.boxClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final User user = mDatas.get(position);

        viewHolder.check_select.setVisibility(leader?View.VISIBLE:View.GONE);

        viewHolder.check_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boxClickListener != null) {
                    user.setSelect(viewHolder.check_select.isChecked());
                    boxClickListener.onCheckClick(position);
                }
            }
        });

        viewHolder.line_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        GlideUtils.loadAvatar(user.getAvatar(),viewHolder.img_head,mContext);
        viewHolder.tv_name.setText(user.getNickname());
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private CheckBox check_select;
        private LinearLayout line_item;
        private ImageView img_head;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            check_select = itemView.findViewById(R.id.check_select);
            line_item =  itemView.findViewById(R.id.line_item);
            img_head =  itemView.findViewById(R.id.iv_pic);
        }
    }
}
