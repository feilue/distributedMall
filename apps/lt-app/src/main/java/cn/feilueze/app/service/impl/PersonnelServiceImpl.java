package cn.feilueze.app.service.impl;


import cn.feilueze.app.service.PersonnelService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonnelServiceImpl implements PersonnelService {

    public void test(){
        System.out.println("true = " + true);
    }
}
