package com.michelle.smartstudy.service.business;

import com.michelle.smartstudy.config.JavaMailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailConfig javaMailConfig;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String courseName, Integer HWnum, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        // 邮件标题
        String subject = "批改作业提醒";
        // 邮件正文
        String text =
                "您好：\n" +
                        "您的 " + courseName + " 课程已收到 " + HWnum + " 份作业。\n" +
                        "请及时到智课平台进行批改。";
        // 发件人
        message.setFrom(javaMailConfig.getFrom());
        // 收件人
        message.setTo(email);
        // 标题
        message.setSubject(subject);
        // 正文
        message.setText(text);
        // 发送邮件
        javaMailSender.send(message);
    }

}
