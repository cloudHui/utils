package utils.ding;

public class OapiRobotSendResponse {
    private Long errcode;
    private String errmsg;

    public OapiRobotSendResponse() {
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public Long getErrcode() {
        return this.errcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getErrmsg() {
        return this.errmsg;
    }

    public boolean isSuccess() {
        return this.getErrcode() == null || this.getErrcode().equals(0L);
    }
}
