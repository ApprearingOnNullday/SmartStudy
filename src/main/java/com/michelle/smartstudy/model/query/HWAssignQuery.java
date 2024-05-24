package com.michelle.smartstudy.model.query;

import lombok.Data;

@Data
public class HWAssignQuery {

    // 作业标题
    private String title;

    // 作业内容
    private String description;

    // 作业开始时间（以String的方式从前端接收，在后端转为LocalDateTime）
    private String start;

    // 作业结束时间
    private String end;

}
