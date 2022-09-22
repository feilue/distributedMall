package cn.feilue.app.service.commone;

import lombok.Data;

/**
 * 接收收件人数据
 */
@Data
public class RecipientParam {
    private SendOptEnum sendOptEnum;
    private String phone;
    private String emial;
}
