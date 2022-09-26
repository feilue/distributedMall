package cn.feilue.app.service.commone;

import lombok.Data;

/**
 * 接收收件人数据
 */
@Data
public class RecipientParam {
    private Integer sendOpt;
    private SendOptEnum sendOptEnum;
    private String personnel;
}
