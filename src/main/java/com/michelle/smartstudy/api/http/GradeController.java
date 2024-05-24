package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.query.HWCorrectionQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.service.business.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 分数表 前端控制器
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    /**
     * 教师批改作业
     * @param hwCorrectionQuery 教师批改的相关内容
     */
    @PostMapping("/teacher/correct")
    public BaseVO<Object> hwCorrection(
            @RequestBody HWCorrectionQuery hwCorrectionQuery
    ) {
        return gradeService.hwCorrection(hwCorrectionQuery);
    }

}
