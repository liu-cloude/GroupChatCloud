package com.xingwang.groupchat.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.xingwang.groupchat.R;

public class GlideUtils {
    public static void loadPic(String url, ImageView imageView, Context context, int size){
        Glide.with(context).load(url)
                           .placeholder(R.drawable.groupchat_pp_ic_holder_light)
                           .error(R.drawable.groupchat_err_img)
                           .override(size,size).dontAnimate().into(imageView);
    }

    public static void loadPic(int id, ImageView imageView, Context context){
        Glide.with(context).load(id)
                .placeholder(R.drawable.groupchat_pp_ic_holder_light)
                .error(R.drawable.groupchat_err_img)
                .dontAnimate().into(imageView);
    }

    public static void loadPic(String url, ImageView imageView, Context context, int width, int height){
        Glide.with(context).load(url)
                .placeholder(R.drawable.groupchat_pp_ic_holder_light)
                .error(R.drawable.groupchat_err_img)
                .override(width,height).dontAnimate().into(imageView);
    }

    public static void loadPic(String url, ImageView imageView, Context context){
        if (EmptyUtils.isEmpty(url)){
            loadPic(imageView,context);
            return;
        }
        Glide.with(context).load(url)
                .placeholder(R.drawable.groupchat_pp_ic_holder_light)
                .error(R.drawable.groupchat_err_img)
                .dontAnimate().into(imageView);
    }

    public static void loadPic(Bitmap bitmap, ImageView imageView, Context context){
        Glide.with(context).load(bitmap)
                .placeholder(R.drawable.groupchat_pp_ic_holder_light)
                .error(R.drawable.groupchat_err_img)
                .dontAnimate().into(imageView);
    }

    //加载头像
    public static void loadAvatar(String url, ImageView imageView, Context context){
        if (EmptyUtils.isEmpty(url)){
            Glide.with(context).load(R.drawable.groupchat_default_avatar)
                    .into(imageView);
            return;
        }
        Glide.with(context).load(url)
                .placeholder(R.drawable.groupchat_pp_ic_holder_light)
                .error(R.drawable.groupchat_err_img)
                .dontAnimate().into(imageView);
    }

    public static void loadPic(ImageView imageView, Context context){
        Glide.with(context).load(R.drawable.groupchat_pp_ic_holder_light)
                .into(imageView);
    }

}
