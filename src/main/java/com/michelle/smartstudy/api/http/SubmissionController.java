package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.model.vo.SubmissionInfo4StudentsVO;
import com.michelle.smartstudy.model.vo.SubmittedHWInfo4TeachersVO;
import com.michelle.smartstudy.service.business.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 作业提交表 前端控制器
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@RestController
@RequestMapping("/submission")
public class SubmissionController {

    @Autowired
    private HomeworkService homeworkService;

    /**
     * 教师查看自己某门课某次作业的所有已提交记录
     * @param id 作业id
     */
    @GetMapping("/teacher/getAll/{id}")
    public BaseVO<List<SubmittedHWInfo4TeachersVO>> teacherGetSubmit(
            @PathVariable(value = "id") Integer id
    ) {
        return homeworkService.teacherGetSubmit(id);
    }

    /**
     * 学生查看自己某个已完成作业的提交记录
     * @param submitId 提交记录id
     */
    @GetMapping("/student/view/{submitId}")
    public BaseVO<SubmissionInfo4StudentsVO> studentGetSubmit(
            @PathVariable(value = "submitId") Integer submitId
    ) {
        return homeworkService.studentViewSubmit(submitId);
    }

}
