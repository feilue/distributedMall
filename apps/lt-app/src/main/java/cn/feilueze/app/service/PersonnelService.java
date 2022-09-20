package cn.feilueze.app.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


public interface PersonnelService {

    @GetMapping("test/test")
    public void test();
}
