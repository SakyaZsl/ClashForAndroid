package com.github.kr328.clash.common.network.http.utils;

import java.lang.annotation.Annotation;

/**
 * Create by Carson on 2021/10/18.
 * 网络相关检查工具类
 */
public class NetWorkUtil {

    /**
     * Returns true if {@code annotations} contains an instance of {@code cls}.
     */
    public static boolean isAnnotationPresent(Annotation[] annotations,
                                              Class<? extends Annotation> cls) {
        for (Annotation annotation : annotations) {
            if (cls.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }
}
