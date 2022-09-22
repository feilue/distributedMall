package cn.feilue.app.service.impl;


import cn.feilue.app.service.PersonnelService;
import cn.feilue.app.service.commone.RecipientParam;
import cn.feilue.app.service.commone.SendOptEnum;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class PersonnelServiceImpl implements PersonnelService {
    public static Map<SendOptEnum, List<String>>  recipientMap = new HashMap<>();
    public void test(){
        System.out.println("true = " + true);
    }

    @Override
    public void addRecipient(RecipientParam param) {
        List<String> all = recipientMap.get(param.getSendOptEnum());
        if(all.isEmpty()){
            all = new ArrayList<>();
        }
        all.add(param.getEmial());
        recipientMap.put(param.getSendOptEnum(),all);
    }

    @Override
    public List<String> recipientList(SendOptEnum sendOptEnum) {
        return recipientMap.get(sendOptEnum);
    }
}
