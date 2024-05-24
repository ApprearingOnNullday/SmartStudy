package com.michelle.smartstudy.model.query;

import lombok.Data;

@Data
public class HWSubmitQuery {

    // 作业id
    private int homeworkId;

    // 提交内容
    private String content;

}
