package com.github.kr328.clash.common.network.http.converter;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Create by Carson on 2021/12/23.
 * 功能描述：json解析相关
 */
public class GsonConverterFactory extends Converter.Factory {

    public static GsonConverterFactory create() {
        return create(new Gson());
    }

    // Guarding public API nullability.
    public static GsonConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new GsonConverterFactory(gson);
    }

    private final Gson gson;

    private GsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type,
                                                            @NonNull Annotation[] annotations,
                                                            @NonNull Retrofit retrofit) {
        if (type != File.class) {
            return new GsonResponseBodyConverter<>(gson, type);
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(@NonNull Type type,
                                                          @NonNull Annotation[] parameterAnnotations,
                                                          @NonNull Annotation[] methodAnnotations,
                                                          @NonNull Retrofit retrofit) {
        if (type != File.class) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new GsonRequestBodyConverter<>(gson, adapter);
        }
        return null;
    }
}
