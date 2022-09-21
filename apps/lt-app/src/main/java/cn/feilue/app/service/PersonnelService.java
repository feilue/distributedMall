package cn.feilue.app.service;

import org.springframework.web.bind.annotation.GetMapping;


public interface PersonnelService {

    @GetMapping("test/test")
    public void test();
}
