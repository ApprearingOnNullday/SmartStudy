package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.vo.BasePageVO;
import com.michelle.smartstudy.model.vo.BaseVO;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@ConfigurationProperties(prefix = "test")
@Setter
public class TestController {

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

}
