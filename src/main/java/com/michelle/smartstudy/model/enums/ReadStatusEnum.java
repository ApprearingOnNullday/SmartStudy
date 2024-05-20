package com.michelle.smartstudy.model.enums;

public enum HomeworkReadEnum {

    READ(1, "已读"),

    UNREAD(2, "未读");

    private Integer code;

    private String desc;

    HomeworkReadEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        HomeworkReadEnum[] values = HomeworkReadEnum.values();
        for (HomeworkReadEnum status : values) {
            if (status.code.equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}
