package com.michelle.smartstudy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionInfo4StudentsVO {

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 提交内容
     */
    private String content;

    /**
     * 批改状态 1-已批改 2-未批改
     */
    private Integer status;

    /**
     * 批改状态描述
     */
    private String statusDesc;

    /**
     * 得分
     */
    private BigDecimal score;

    /**
     * 教师评语
     */
    private String comment;

}
