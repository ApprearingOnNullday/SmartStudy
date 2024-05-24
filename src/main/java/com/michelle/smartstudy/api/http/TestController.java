package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.dto.HomeworkDTO;
import com.michelle.smartstudy.model.dto.SubmissionDTO;
import com.michelle.smartstudy.model.vo.BasePageVO;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.mq.producer.HomeworkProducer;
import com.michelle.smartstudy.mq.producer.SubmissionProducer;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/test")
@ConfigurationProperties(prefix = "test")
@Setter
public class TestController {

    @Autowired
    private HomeworkProducer homeworkProducer;

    @Autowired
    private SubmissionProducer submissionProducer;

    private int version;

    @GetMapping("init")
    public BaseVO testInit() {
        return new BaseVO().success().setData("src: 20240520-1757, ver: " + version);
    }

    @GetMapping("initpage")
    public BasePageVO testPageInit() {
        BasePageVO basePageVO = new BasePageVO();
        basePageVO.success();
        return basePageVO;
    }

    @PostMapping("/hwProducer")
    public BaseVO testProducer(
            @RequestParam(value="queueName") String queueName
    ) {
        String startTime = "2024-05-15T18:00:00";
        String endTime = "2024-05-29T00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);

        HomeworkDTO homeworkDTO = HomeworkDTO.builder()
                .title("SA第2次小组作业")
                .courseId(16)
                .description("请为A、B、C三个数据库服务器设计几种冗余方案。")
                .start(start)
                .end(end)
                .build();

        homeworkProducer.sendMsg(queueName, homeworkDTO);
        return new BaseVO().success();
    }

    @PostMapping("/subProducer")
    public BaseVO testSubProducer(
            @RequestParam(value="queueName") String queueName
    ) {

        LocalDateTime subTime = LocalDateTime.now();

        SubmissionDTO submissionDTO = SubmissionDTO.builder()
                .studentId(1)
                .homeworkId(24)
                .submitTime(subTime)
                .content("设计方案见附件PPT。")
                .build();

        submissionProducer.sendMsg(queueName, submissionDTO);
        return new BaseVO().success();
    }

}
