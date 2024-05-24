package com.michelle.smartstudy.service.business;

import com.michelle.smartstudy.model.entity.TbGrade;
import com.michelle.smartstudy.model.entity.TbSubmission;
import com.michelle.smartstudy.model.enums.CorrectingStatusEnum;
import com.michelle.smartstudy.model.query.HWCorrectionQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.service.base.ITbGradeService;
import com.michelle.smartstudy.service.base.ITbSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GradeService {

    @Autowired
    private ITbGradeService tbGradeService;

    @Autowired
    private ITbSubmissionService tbSubmissionService;

    // 教师批改作业
    public BaseVO<Object> hwCorrection(HWCorrectionQuery hwCorrectionQuery) {
        // 1. 向tb_grade表中插入一条记录
        TbGrade tbGrade = TbGrade.builder()
                .submissionId(hwCorrectionQuery.getSubmissionId())
                .score(hwCorrectionQuery.getScore())
                .comment(hwCorrectionQuery.getComment())
                .build();
        tbGradeService.save(tbGrade);
        // 2. 根据提交记录id，到tb_submission表中更新批改状态status：2->1
        Integer submitId = hwCorrectionQuery.getSubmissionId();
        TbSubmission tbSubmission = tbSubmissionService.getById(submitId);
        // 将批改状态set为“已批改”
        tbSubmission.setStatus(CorrectingStatusEnum.CORRECTED.getCode());
        tbSubmissionService.updateById(tbSubmission);

        return new BaseVO<>().success().setData("您已成功批阅一份作业！");
    }

}
