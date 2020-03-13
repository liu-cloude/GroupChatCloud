package com.xingwang.groupchat;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.adapter.GroupMemberListAdapter;
import com.xingwang.groupchat.adapter.SearchUserAdapter;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.callback.DialogAlertCallback;
import com.xingwang.groupchat.callback.OnItemClickListener;
import com.xingwang.groupchat.title.TopTitleView;
import com.xingwang.groupchat.utils.ActivityManager;
import com.xingwang.groupchat.utils.AsyncTaskUtils;
import com.xingwang.groupchat.utils.Constants;
import com.xingwang.groupchat.utils.DialogUtils;
import com.xingwang.groupchat.utils.HttpUtil;
import com.xingwang.groupchat.utils.JsonUtils;
import com.xingwang.groupchat.utils.asynctask.IDoInBackground;
import com.xingwang.groupchat.utils.asynctask.IPublishProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchUserActivity extends BaseActivity implements View.OnClickListener {

    protected TopTitleView title;
    protected EditText et_search;
    protected ImageView img_empty;
    protected RecyclerView lv_list;
    protected TextView tv_search;
    protected Button bt_add;
    protected TextView tv_empty;

    private SearchUserAdapter searchUserAdapter;

    //群成员集合
    private List<User> groupUserList = new ArrayList<>();
    //搜索结果集
    private List<User> searchUserList = new ArrayList<>();
    //最近联系人id集合
    private List<String> nearestUserIdList = new ArrayList<>();
    //添加成员id集
    private List<String> addUserList = new ArrayList<>();

    private String searchContent;
    private String groupId;//群id
    private int count=0;//计 添加的个数

    private AsyncTaskUtils.Builder<Void, Void, Void> mAsyncTask;

    private HashMap<String,String> params=new HashMap<>();

    public static void getIntent(Context context,String groupId) {
        Intent intent = new Intent(context, SearchUserActivity.class);
         intent.putExtra(Constants.INTENT_DATA,groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_search_user;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        et_search = findViewById(R.id.et_search);
        img_empty = findViewById(R.id.img_empty);
        lv_list = findViewById(R.id.lv_list);
        tv_search = findViewById(R.id.tv_search);
        tv_empty = findViewById(R.id.tv_empty);
        bt_add = findViewById(R.id.bt_add);

        tv_search.setOnClickListener(this);
        img_empty.setOnClickListener(this);
        bt_add.setOnClickListener(this);

        lv_list.setLayoutManager( new LinearLayoutManager(this));

        searchUserAdapter = new SearchUserAdapter(this, searchUserList);
        lv_list.setAdapter(searchUserAdapter);

    }

    @Override
    public void initData() {

        groupId=getIntent().getStringExtra(Constants.INTENT_DATA);

        nearestUserIdList.clear();
        nearestUserIdList.addAll(BeautyDefine.getMsgBoxDefine().queryDiffUserIds());

        getGroupMember();

        Log.i("Search","size"+nearestUserIdList.size());

        searchUserAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(int position) {

                ToastUtils.showShortSafe("click"+position);

                if (searchUserList.get(position).isSelect()){
                    addUserList.add(String.valueOf(searchUserList.get(position).getId()));
                }else {
                    addUserList.remove(String.valueOf(searchUserList.get(position).getId()));
                }

                if (EmptyUtils.isEmpty(addUserList)){
                    bt_add.setText("立即添加");
                }else {
                    bt_add.setText("立即添加("+addUserList.size()+")");
                }
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchContent = s.toString();
                if (StringUtils.isEmpty(searchContent)) {
                    searchUserList.clear();
                    searchUserAdapter.notifyDataSetChanged();
                    img_empty.setVisibility(View.GONE);
                } else {
                    img_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (StringUtils.isEmpty(searchContent)){
                        ToastUtils.showShortSafe("请填写搜索内容!");
                    }else {
                        searchUser();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    //获取群成员
    private void getGroupMember(){
        HttpUtil.get(Constants.GROUP_MEMBER +"?group_id="+groupId, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                groupUserList.clear();
                groupUserList.addAll(JsonUtils.jsonToList(json,User.class));
            }
        });
    }

    //搜索用户
    private void searchUser(){
        params.clear();
        params.put("q",searchContent);

        showLoadingDialog();
        HttpUtil.get(Constants.SEARCH_USER,params,new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                searchUserList.clear();
                searchUserList.addAll(JsonUtils.jsonToList(json, User.class));
                if (EmptyUtils.isEmpty(searchUserList)){
                    tv_empty.setVisibility(View.VISIBLE);
                    lv_list.setVisibility(View.GONE);
                    return;
                }

                matchGroupMember();
            }
        });
    }

    //添加成员
    private void addGroupMember(String id){
        params.clear();
        params.put("group_id",groupId);
        params.put("user_id",id);
        HttpUtil.post(Constants.GROUP_ADD, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json, String tag) {
                if (++count==addUserList.size()){
                    hideLoadingDialog();
                    ToastUtils.showShortSafe("添加成功！");
                    ActivityManager.getInstance().finishActivity();
                }
            }
        });
    }

    //匹配群成员
    private void matchGroupMember(){
        mAsyncTask = AsyncTaskUtils.<Void, Void, Void>newBuilder().setDoInBackground(new IDoInBackground<Void, Void, Void>() {
            @Override
            public Void doInBackground(IPublishProgress<Void> publishProgress, Void... booleans) {

                for (User groupUser : groupUserList) {
                    for (User searchUser : searchUserList) {
                        if (groupUser.getId()==searchUser.getId()) {//该用户在群内
                            searchUser.setGroup(true);
                            break;
                        }
                    }
                }
                return null;
            }
        }).setPostExecute(aBoolean -> {
            hideLoadingDialog();
            tv_empty.setVisibility(View.GONE);
            lv_list.setVisibility(View.VISIBLE);

            searchUserAdapter.notifyDataSetChanged();
        });
        mAsyncTask.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_search) {//搜索
            if (StringUtils.isEmpty(searchContent)) {
                ToastUtils.showShortSafe("请填写搜索内容!");
            } else {
                searchUser();
            }
        } else if (id == R.id.img_empty) {//清空搜索内容
            et_search.setText("");
        } else if (id == R.id.bt_add) {
            if (EmptyUtils.isEmpty(addUserList)) {
                ToastUtils.showShortSafe("请选择要添加的用户!");
            } else {
                for (String userId : addUserList) {
                    addGroupMember(userId);
                }
            }
        }
    }
}
