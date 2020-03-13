package com.xingwang.groupchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.adapter.GroupTransferAdapter;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.callback.DialogAlertCallback;
import com.xingwang.groupchat.title.TopTitleView;
import com.xingwang.groupchat.utils.ActivityManager;
import com.xingwang.groupchat.utils.Constants;
import com.xingwang.groupchat.utils.DialogUtils;
import com.xingwang.groupchat.utils.GlideUtils;
import com.xingwang.groupchat.utils.HttpUtil;
import com.xingwang.groupchat.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransferActivity extends BaseActivity {

    protected TopTitleView title;
    protected TextView tv_empty;
    protected ListView list_group;

    private GroupTransferAdapter transferAdapter;
    private List<User> memberList = new ArrayList<>();

    private Group group;
    private HashMap<String,String> params=new HashMap<>();

    public static void getIntent(Context context, Group group) {
        Intent intent = new Intent(context, TransferActivity.class);
        intent.putExtra(Constants.INTENT_DATA,group);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_transfer;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        tv_empty = findViewById(R.id.tv_empty);
        list_group = findViewById(R.id.list_group);
    }

    @Override
    public void initData() {

        group=(Group)getIntent().getSerializableExtra(Constants.INTENT_DATA);

        transferAdapter=new GroupTransferAdapter(this,memberList);
        list_group.setAdapter(transferAdapter);

        list_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                DialogUtils.obtainCommonDialog("转让给"+memberList.get(i).getNickname()+"后，你将失去群主身份", new DialogAlertCallback() {
                    @Override
                    public void sure() {
                        transferGroup(memberList.get(i).getStrId());
                    }
                }).show(getSupportFragmentManager());
            }});
        getGroupMember();
    }

    //获取群成员
    private void getGroupMember(){
        HttpUtil.get(Constants.GROUP_MEMBER +"?group_id="+ group.getId(), new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                memberList.clear();
                memberList.addAll(JsonUtils.jsonToList(json, User.class));

                for (User user:memberList){
                    if (user.getId()==group.getUser_id()){//群主匹配
                        memberList.remove(user);
                        break;
                    }
                }

                if (EmptyUtils.isEmpty(memberList)){
                    list_group.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.VISIBLE);
                }else {
                    list_group.setVisibility(View.VISIBLE);
                    tv_empty.setVisibility(View.GONE);
                }
                transferAdapter.notifyDataSetChanged();
            }
        });
    }

    //群转移
    private void transferGroup(String userId){
        showLoadingDialog();
        params.clear();
        params.put("group_id",group.getStrId());
        params.put("user_id",userId);
        HttpUtil.post(Constants.GROUP_TRANSFER, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json, String tag) {
                hideLoadingDialog();
                ToastUtils.showShortSafe("操作成功!");
                ActivityManager.getInstance().finishActivity();
            }
        });
    }
}
