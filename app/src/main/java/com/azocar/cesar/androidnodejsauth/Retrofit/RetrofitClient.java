package com.azocar.cesar.androidnodejsauth.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null)
            instance = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.87:3000/") // in emulator, 127.0.0.1 will changed to 10.0.2.2
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return instance;

    }
}
