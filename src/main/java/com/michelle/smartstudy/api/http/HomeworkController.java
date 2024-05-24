package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.query.HWAssignQuery;
import com.michelle.smartstudy.model.query.HWSubmitQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.model.vo.HWInfo4TeachersVO;
import com.michelle.smartstudy.service.business.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 作业表 前端控制器
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    /**
     * 布置作业
     * @param id 课程id
     * @param hwAssignQuery 作业信息
     */
    @PostMapping("/assign/{id}")
    public BaseVO<Object> assign(
            @PathVariable(value = "id") Integer id,
            @RequestBody HWAssignQuery hwAssignQuery
    ) {
        return homeworkService.assign(id, hwAssignQuery);
    }

    /**
     * 学生提交作业
     * @param hwSubmitQuery 提交的作业信息
     */
    @PostMapping("/submit")
    public BaseVO<Object> hwSubmit(
            @RequestBody HWSubmitQuery hwSubmitQuery
    ) {
        return homeworkService.submit(hwSubmitQuery);
    }

    /**
     * 教师查看自己某门课的所有已布置作业
     * @param id 课程id
     */
    @GetMapping("/displayAll/{id}")
    public BaseVO<List<HWInfo4TeachersVO>> teacherGetAll(
            @PathVariable(value = "id") Integer id
    ) {
        return homeworkService.teacherGetAll(id);
    }

}
