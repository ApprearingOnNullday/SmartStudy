package com.michelle.smartstudy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo4TeachersVO {

    /**
     * 学生id
     */
    private Integer studentId;

    /**
     * 学生姓名
     */
    private String studentName;

}
