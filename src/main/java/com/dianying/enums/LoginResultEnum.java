package com.dianying.enums;

/**
 * author: winsky
 * date: 2017/2/22
 * description:
 */
public enum LoginResultEnum {
    INVALID_PWD(0, "密码错误"), SUCCESS(1, "登录成功"), INVALID_NAME(2, "用户名不存在");

    private int value;
    private String type;

    private LoginResultEnum(int value, String type) {
        this.type = type;
        this.value = value;
    }

    public static LoginResultEnum fromValue(int value) {
        for (LoginResultEnum e : LoginResultEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }

    public static LoginResultEnum fromType(String type) {
        for (LoginResultEnum e : LoginResultEnum.values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }

    public int value() {
        return value;
    }

    public String type() {
        return type;
    }
}
