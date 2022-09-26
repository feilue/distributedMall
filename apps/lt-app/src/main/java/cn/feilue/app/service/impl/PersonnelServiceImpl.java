package cn.feilue.app.service.impl;


import cn.feilue.app.service.PersonnelService;
import cn.feilue.app.service.commone.RecipientParam;
import cn.feilue.app.service.commone.SendOptEnum;
import cn.feilue.app.service.config.EmailConfig;
import cn.feilue.app.service.config.SMSConfig;
import cn.feilue.app.service.send.SendSMS;
import org.bouncycastle.pqc.crypto.newhope.NHOtherInfoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
public class PersonnelServiceImpl implements PersonnelService {
    @Autowired
    private SMSConfig smsConfig;
    @Autowired
    private EmailConfig emailConfig;

    public static Map<SendOptEnum, List<String>>  recipientMap = new HashMap<>();
    private static Logger log =   LoggerFactory.getLogger(SendSMS.class);

    public void test(){
        System.out.println("true = " + true);
    }

    @PostConstruct
    public void intit(){
        List<String> email = new ArrayList<>();
        List<String> phone = new ArrayList<>();
        email.add(emailConfig.getInitializer());
        recipientMap.put(SendOptEnum.EMAIL,email);

        phone.add(smsConfig.getInitializer());
        recipientMap.put(SendOptEnum.SMS, phone);
    }

    @Override
    public void addRecipient(RecipientParam param) throws Exception {
        SendOptEnum sendOpEnum = SendOptEnum.SMS.getSendOpEnum(param.getSendOpt());
        if(sendOpEnum == null){
            throw new Exception("类型错误");
        }
        param.setSendOptEnum(sendOpEnum);
        List<String> all = recipientMap.get(param.getSendOptEnum());

        if(all.contains(param.getPersonnel()))
            return;

        all.add(param.getPersonnel());
        recipientMap.put(param.getSendOptEnum(),all);
        log.info(param.getSendOptEnum().getOptName()+"队列："+all);
//        all.clear();
    }

    @Override
    public List<String> recipientList(SendOptEnum sendOptEnum) {
        return recipientMap.get(sendOptEnum);
    }
}
