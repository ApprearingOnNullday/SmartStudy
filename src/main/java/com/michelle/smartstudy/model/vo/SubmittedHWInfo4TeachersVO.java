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
public class SubmittedHWInfo4TeachersVO {

    /**
     * 提交记录id
     */
    private Integer submitId;

    /**
     * 学生id
     */
    private Integer studentId;

    /**
     * 学生姓名
     */
    private String studentName;

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
     * 批改状态描述（已批改/未批改）
     */
    private String statusDesc;

    /**
     * 分数（若已批改则有）
     */
    private BigDecimal score;

    /**
     * 评语（若已批改则有）
     */
    private String comment;

}
