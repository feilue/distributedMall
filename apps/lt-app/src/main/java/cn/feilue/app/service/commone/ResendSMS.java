package cn.feilue.app.service.commone;

import lombok.Data;

@Data
public class ResendSMS {
    private  String phone;
    private SmsTemplateEnum smsTemplate;
    private  String templateParam;
}
