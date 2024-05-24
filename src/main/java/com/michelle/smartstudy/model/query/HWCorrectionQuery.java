package com.michelle.smartstudy.model.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HWCorrectionQuery {

    // 提交记录id
    private int submissionId;

    // 分数
    private BigDecimal score;

    // 教师评语
    private String comment;

}
