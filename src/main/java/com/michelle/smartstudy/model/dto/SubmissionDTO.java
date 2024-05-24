package com.michelle.smartstudy.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubmissionDTO {

    // 学生id
    private int studentId;

    // 作业id
    private int homeworkId;

    // 提交时间
    private LocalDateTime submitTime;

    // 提交内容
    private String content;

}
