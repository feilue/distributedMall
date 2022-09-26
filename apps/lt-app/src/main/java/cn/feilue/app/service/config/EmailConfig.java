package cn.feilue.app.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfig {
    private String emailHost ;       //发送邮件的主机
    private String transportType ;           //邮件发送的协议
    private String fromUser ;           //发件人名称
    private String fromEmail;  //发件人邮箱
    private String authCode ;    //发件人邮箱授权码
    private Long delay;      //群发邮件延时
    private String initializer;  //初始化人员
}
