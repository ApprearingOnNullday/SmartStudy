package com.michelle.smartstudy.model.enums;

public enum CorrectingStatusEnum {

    CORRECTED(1, "已批改"),

    UNCORRECTED(2, "未批改");

    private Integer code;

    private String desc;

    CorrectingStatusEnum(int code, String desc) {
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
        CorrectingStatusEnum[] values = CorrectingStatusEnum.values();
        for (CorrectingStatusEnum status : values) {
            if (status.code.equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}
