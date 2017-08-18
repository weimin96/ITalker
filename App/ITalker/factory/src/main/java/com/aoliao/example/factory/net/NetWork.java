package com.aoliao.example.factory.net;

import android.text.TextUtils;

import com.aoliao.example.common.Common;
import com.aoliao.example.factory.Factory;
import com.aoliao.example.factory.persistence.Account;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Build.VERSION_CODES.N;

/**
 * @author 你的奥利奥
 * @version 2017/8/17
 */

public class NetWork {

    private static NetWork instance;
    private Retrofit retrofit;

    static {
        instance = new NetWork();
    }

    private NetWork() {
    }

    //构建一个retrofit
    public static Retrofit getRetrofit() {
        if (instance.retrofit != null) {
            return instance.retrofit;
        }
        //得到一个OK Client
        OkHttpClient client = new OkHttpClient.Builder()
                //给所有请求添加一个拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //拿到我们的请求
                        Request original = chain.request();
                        //重新进行build
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            //注入一个token
                            builder.addHeader("token", Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        //
        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)//设置电脑链接
                .client(client)//设置client
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))//设置Json解析器
                .build();
        return instance.retrofit;
    }

    /**
     * 返回一个请求代理
     *
     * @return RemoteService
     */
    public static RemoteService remote() {
        return NetWork.getRetrofit().create(RemoteService.class);
    }
}
