package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.query.CourseAddQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.model.vo.CourseInfo4StudentsVO;
import com.michelle.smartstudy.service.business.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程表 前端控制器
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 添加（创建）课程
     */
    @PostMapping("/add")
    public BaseVO<Object> addCourse(
            @RequestBody CourseAddQuery courseAddQuery
    ) {
        return courseService.addCourse(courseAddQuery);
    }

    /**
     * 删除课程
     * @param id 课程id
     * @return
     */
    @PostMapping("/del/{id}")
    public BaseVO<Object> deleteCourse(
            @PathVariable(value = "id") Integer id
    ) {
        return courseService.deleteCourse(id);
    }

    /**
     * 学生查看系统中所有已有课程
     */
    @GetMapping("/student/display/all")
    public BaseVO<List<CourseInfo4StudentsVO>> getAllForStudent() {
        return courseService.getAllForStudent();
    }

}
