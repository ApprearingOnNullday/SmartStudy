package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.query.HWAssignQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.service.business.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
