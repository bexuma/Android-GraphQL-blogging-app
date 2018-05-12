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
import okhttp3.Request;

public class MyApolloClient {
    private static final String BASE_URL = "https://graphql-example-blog.herokuapp.com/graphql";
    public static final String TAG = "MyServer";

    private static ApolloClient apolloClient;


    public static ApolloClient getMyApolloClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        NormalizedCacheFactory cacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024).build());
        CacheKeyResolver cacheKeyResolver = new CacheKeyResolver() {
            @Nonnull
            @Override
            public CacheKey fromFieldRecordSet(@Nonnull ResponseField field, @Nonnull Map<String, Object> recordSet) {
                if (recordSet.containsKey("id")) {
                    String id = (String) recordSet.get("id");
                    return CacheKey.from(id);
                }
                return CacheKey.NO_KEY;
            }

            @Nonnull @Override
            public CacheKey fromFieldArguments(@Nonnull ResponseField field, @Nonnull Operation.Variables variables) {
                return CacheKey.NO_KEY;
            }
        };

        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
//                .normalizedCache(cacheFactory, cacheKeyResolver)
                .okHttpClient(okHttpClient)
                .build();

        return apolloClient;
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

        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

        return apolloClient;

    }
}
