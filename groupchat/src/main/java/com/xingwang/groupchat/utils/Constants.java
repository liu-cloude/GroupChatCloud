package com.xingwang.groupchat.utils;

public class Constants {

    public static String IP = "http://zyapp.test.xw518.com/";

    public final static String INTENT_DATA="data";
    public final static String INTENT_DATA1="data1";

    /**********群聊模块***********/
    public static String HTTP_GROUP=IP+"group/user/group/";
    //其他
    public static String HTTP_GENERAL=IP+"user/general/user/";
    /**图片选择最大张数*/
    public final static int MAX_COUNT=9;

    //获取我加入的群
    public final static String GROUP_MY=HTTP_GROUP+"my";
    //获取群成员
    public final static String GROUP_MEMBER=HTTP_GROUP+"user";
    //获取群信息
    public final static String GROUP_INFO=HTTP_GROUP+"info";
    //修改群信息
    public final static String GROUP_EDIT=HTTP_GROUP+"edit";
    //创建群
    public final static String GROUP_CREATE=HTTP_GROUP+"create";
    //退出群聊
    public final static String GROUP_LEAVE=HTTP_GROUP+"leave";
    //解散群聊
    public final static String GROUP_DISMISS=HTTP_GROUP+"dismiss";
    //拉人进群
    public final static String GROUP_ADD=HTTP_GROUP+"laren";
    //踢出群聊
    public final static String GROUP_MEMBER_REMOVE=HTTP_GROUP+"tiren";
    //转移群
    public final static String GROUP_TRANSFER=HTTP_GROUP+"zengsong";

    //根据电话号码搜索用户
    public final static String SEARCH_USER=HTTP_GENERAL+"search";

}
