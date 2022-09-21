package cn.feilue.app.service.impl;


import cn.feilue.app.service.PersonnelService;
import cn.feilue.app.service.commone.RecipientParam;
import cn.feilue.app.service.commone.SendOpt;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class PersonnelServiceImpl implements PersonnelService {
    public static Map<SendOpt, List<String>>  recipientMap = new HashMap<>();
    public void test(){
        System.out.println("true = " + true);
    }

    @Override
    public void addRecipient(RecipientParam param) {
        List<String> all = recipientMap.get(param.getSendOpt());
        if(all.isEmpty()){
            all = new ArrayList<>();
        }
        all.add(param.getEmial());
        recipientMap.put(param.getSendOpt(),all);
    }

    @Override
    public List<String> recipientList(SendOpt sendOpt) {
        return recipientMap.get(sendOpt);
    }
}
