package cn.feilue.app.service.commone;

import lombok.Data;

/**
 * 接收收件人数据
 */
@Data
public class RecipientParam {
    private SendOpt sendOpt;
    private String phone;
    private String emial;
}
