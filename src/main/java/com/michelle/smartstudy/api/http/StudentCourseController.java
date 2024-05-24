package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.model.vo.ChosenCourseInfo4StudentsVO;
import com.michelle.smartstudy.model.vo.StudentInfo4TeachersVO;
import com.michelle.smartstudy.service.business.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 学生课程表 前端控制器
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-22
 */
@RestController
@RequestMapping("/studentCourse")
public class StudentCourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 学生选课
     * @param id 课程id
     * @return
     */
    @PostMapping("/choose/{id}")
    public BaseVO<Object> chooseCourse(
            @PathVariable(value = "id") Integer id
    ) {
        return courseService.chooseCourse(id);
    }

    /**
     * 查找某学生已选的所有课程
     */
    @GetMapping("/display/chosen")
    public BaseVO<List<ChosenCourseInfo4StudentsVO>> getChosen() {
        return courseService.getChosen();
    }

    /**
     * 教师获取某课程的学生列表
     * @param courseId 课程id
     * @return
     */
    @GetMapping("/studentList/{courseId}")
    public BaseVO<List<StudentInfo4TeachersVO>> getStudentList(
            @PathVariable(value = "courseId") Integer courseId
    ) {
        return courseService.getStudentList(courseId);
    }

}
