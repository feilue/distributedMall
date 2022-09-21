package cn.feilue.app.service.impl;


import cn.feilue.app.service.PersonnelService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonnelServiceImpl implements PersonnelService {

    public void test(){
        System.out.println("true = " + true);
    }
}
