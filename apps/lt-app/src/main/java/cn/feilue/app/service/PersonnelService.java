package cn.feilue.app.service;

import cn.feilue.app.service.commone.RecipientParam;
import cn.feilue.app.service.commone.SendOptEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface PersonnelService {

    @GetMapping("test/test")
    public void test();

    @PostMapping("/add")
    public void addRecipient(@RequestBody RecipientParam param);

    public List<String> recipientList(SendOptEnum sendOptEnum);


}
