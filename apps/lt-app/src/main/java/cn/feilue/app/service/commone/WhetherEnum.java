package cn.feilue.app.service.commone;

/**
 * 发送选项
 */
public enum WhetherEnum {
    YES(1,"是"),
    NO(2,"否")
    ;

    WhetherEnum() {
    }

    private Integer code;
    private String name;
    WhetherEnum(int code, String optName) {
        this.code = code;
        this.name = optName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getOptName() {
        return name;
    }

    public void setOptName(String optName) {
        this.name = optName;
    }

    public WhetherEnum getSendOpEnum(Integer code){
        WhetherEnum[] values = WhetherEnum.values();
        for (WhetherEnum value : values) {
            if(value.code == code){
                return value;
            }
        }
        return null;
    }



}
