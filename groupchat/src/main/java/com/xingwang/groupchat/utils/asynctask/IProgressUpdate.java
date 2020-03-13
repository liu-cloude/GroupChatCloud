package com.xingwang.groupchat.utils.asynctask;

public interface IProgressUpdate<Progress>{
    void onProgressUpdate(Progress... values);

}
