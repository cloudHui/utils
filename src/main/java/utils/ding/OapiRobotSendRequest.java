package utils.ding;

import java.util.List;
import java.util.Map;

public class OapiRobotSendRequest{
    private String actionCard;
    private String at;
    private String feedCard;
    private String link;
    private String markdown;
    private String msgtype;
    private String text;
    private String topResponseType = "dingtalk";
    private String topHttpMethod = "POST";

    public OapiRobotSendRequest() {
    }

    public String getActionCard() {
        return actionCard;
    }

    public void setActionCard(String actionCard) {
        this.actionCard = actionCard;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getFeedCard() {
        return feedCard;
    }

    public void setFeedCard(String feedCard) {
        this.feedCard = feedCard;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopResponseType() {
        return topResponseType;
    }

    public void setTopResponseType(String topResponseType) {
        this.topResponseType = topResponseType;
    }

    public String getTopHttpMethod() {
        return topHttpMethod;
    }

    public void setTopHttpMethod(String topHttpMethod) {
        this.topHttpMethod = topHttpMethod;
    }
}





