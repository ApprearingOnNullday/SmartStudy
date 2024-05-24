package com.michelle.smartstudy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChosenCourseInfo4StudentsVO {

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
     * 教师id
     */
    private Integer teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 选课人数
     */
    private Integer enrollment;

    /**
     * 待完成作业份数
     */
    private Integer toFinish;

}
