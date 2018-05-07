package com.example.bexuma.apollographqlrailsapponheroku;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.cache.normalized.CacheKey;
import com.apollographql.apollo.cache.normalized.CacheKeyResolver;
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy;
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory;

import java.util.Map;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;

public class MyApolloClient {
    private static final String BASE_URL = "http://graphql-example-blog.herokuapp.com/graphql";
    private static ApolloClient myApolloClient;

    public static ApolloClient getMyApolloClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        myApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

        return myApolloClient;
    }
}
