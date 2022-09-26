package cn.feilue.app.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SMSConfig {
    private String accessKeyId ;
    private String accessKeySecret ;
    private Long delay;
    private String initializer;  //初始化人员
    private Long resendSendTime;  //平台限制时间

}
