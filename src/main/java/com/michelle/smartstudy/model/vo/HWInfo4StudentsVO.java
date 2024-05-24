package com.michelle.smartstudy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HWInfo4StudentsVO {

    /**
     * 作业id
     */
    private Integer homeworkId;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 作业内容（详情）
     */
    private String description;

    /**
     * 作业开始时间
     */
    private LocalDateTime start;

    /**
     * 作业截止时间
     */
    private LocalDateTime end;

    /**
     * 作业完成状态 1-已完成 2-未完成
     */
    private Integer status;

    /**
     * 作业完成状态描述
     */
    private String statusDesc;

    /**
     * 提交记录id 若未完成则此处值为0
     */
    private Integer submitId;

}
