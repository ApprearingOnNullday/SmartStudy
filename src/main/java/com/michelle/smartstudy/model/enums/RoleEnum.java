package com.michelle.smartstudy.model.enums;

public enum RoleEnum {

    STUDENT(1, "学生"),

    TEACHER(2, "教师"),

    ADMIN(3, "管理员");

    private Integer code;

    private String desc;

    RoleEnum(int code, String desc) {
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
        RoleEnum[] values = RoleEnum.values();
        for (RoleEnum role : values) {
            if (role.code.equals(code)) {
                return role.getDesc();
            }
        }
        return null;
    }
}
