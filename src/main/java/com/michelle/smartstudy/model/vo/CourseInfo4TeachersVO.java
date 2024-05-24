package com.michelle.smartstudy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo4TeachersVO {

    /**
     * 课程id
     */
    private Integer id;

    /**
     * 课程名称
     */
    private String title;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 选课人数
     */
    private Integer enrollment;

}
