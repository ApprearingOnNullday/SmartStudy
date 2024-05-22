package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.entity.TbHomework;
import com.michelle.smartstudy.model.vo.BasePageVO;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.mq.producer.HomeworkProducer;
import com.michelle.smartstudy.service.base.ITbHomeworkService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@ConfigurationProperties(prefix = "test")
@Setter
public class TestController {

    @Autowired
    private ITbHomeworkService tbHomeworkService;

    @Autowired
    private HomeworkProducer homeworkProducer;

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

//    @PostMapping("/hwProducer")
//    public BaseVO testProducer(
//            @RequestParam(value="queueName") String queueName
//    ) {
//        TbHomework tbHomework = new TbHomework();
//        tbHomework.setTitle("SA hw1");
//        tbHomework.setCourseId(8);
//        tbHomework.setDescription("finish Ontime");
//        tbHomeworkService.save(tbHomework);
//
//        TbHomework newHW = tbHomeworkService.getById(tbHomework.getId());
//        homeworkProducer.sendMsg(queueName, newHW);
//        return new BaseVO().success();
//    }

}
