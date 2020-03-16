package com.xingwang.groupchat.utils;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.EmptyUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHeaderInterceptor implements Interceptor {
    private String mAuth= BeautyDefine.getAccountDefine().getAuthStr();
    //private String mAuth="FQIGBQcPFQkCV1hSDAJcAlMBVFFXA1BQUQwAVQdUW1JcAhACXVJbJBIVfiU1CmolY3F/aEVpIHQJWGo+bjg3JCQDcnVFDX9gcEF3cV90Ny5hQWciN0YyYid0JyMaV1M1AEFmJVANBSt2BwRvJxY2bjAbA2oKeFpxTll0IgN5engndjgiJx1+aV9GMnpxWVBjZQ19MyRmalM2JFc7dAVQIRUkVWJSCFFzIgNxIDVEKSVfIxMheVxKRwM=";

    public HttpHeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder=chain.request().newBuilder();
        if (EmptyUtils.isNotEmpty(mAuth))
            builder.addHeader("Authorization",mAuth);
        return chain.proceed(builder.build());
    }
}
