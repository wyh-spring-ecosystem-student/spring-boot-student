package com.xiaolyuh.wrapper;


public class WrapMapper {
    private WrapMapper() {
    }

    public static <E> Wrapper<E> wrap(int code, String message, E o) {
        return new Wrapper<E>(code, message, o);
    }

    public static <E> Wrapper<E> wrap(int code, String message) {
        return wrap(code, message, null);
    }

    public static <E> Wrapper<E> wrap(int code) {
        return wrap(code, null);
    }

    public static <E> E unWrap(Wrapper<E> wrapper) {
        return wrapper.getData();
    }

}
