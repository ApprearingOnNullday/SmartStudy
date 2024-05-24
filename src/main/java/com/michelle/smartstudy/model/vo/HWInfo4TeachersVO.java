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
public class HWInfo4TeachersVO {

    /**
     * 作业id
     */
    private Integer id;

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
     * 已提交人数
     */
    private Integer submitted;

    /**
     * 总人数
     */
    private Integer total;

}
