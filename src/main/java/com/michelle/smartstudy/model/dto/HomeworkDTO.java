package com.michelle.smartstudy.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HomeworkDTO {

    // 作业标题
    private String title;

    // 课程id
    private int courseId;

    // 作业内容
    private String description;

    // 作业开始时间
    private LocalDateTime start;

    // 作业截止时间
    private LocalDateTime end;

}
