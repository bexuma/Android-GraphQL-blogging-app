package com.example.bexuma.apollographqlrailsapponheroku;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MyApolloClient {
    private static final String BASE_URL = "https://graphql-example-blog.herokuapp.com/graphql";
    private static ApolloClient myApolloClient;

    public static ApolloClient getMyApolloClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        myApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

        return myApolloClient;
    }

    public static ApolloClient getAuthorizedApolloClient(String token) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder().method(original.method(), original.body());
                    builder.addHeader("Authorization", "bearer " + token);
                    return chain.proceed(builder.build());
                })
                .build();

        myApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

        return myApolloClient;

    }
}
