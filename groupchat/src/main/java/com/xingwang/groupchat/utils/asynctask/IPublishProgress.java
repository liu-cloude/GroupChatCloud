package com.xingwang.groupchat.utils.asynctask;

public interface IPublishProgress<Progress>{
    void showProgress(Progress... values);

}