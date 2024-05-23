package com.michelle.smartstudy.model.enums;

public enum ReadStatusEnum {

    FINISTHED(1, "已读"),

    UNFINISHED(2, "未读");

    private Integer code;

    private String desc;

    ReadStatusEnum(int code, String desc) {
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
        ReadStatusEnum[] values = ReadStatusEnum.values();
        for (ReadStatusEnum status : values) {
            if (status.code.equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}
