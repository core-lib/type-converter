package com.qfox.commons.converter;

/**
 * 转换异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-02-06 10:47
 **/
public class ConvertException extends RuntimeException {
    private static final long serialVersionUID = 4254075283616098302L;

    public ConvertException() {
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertException(Throwable cause) {
        super(cause);
    }
}
