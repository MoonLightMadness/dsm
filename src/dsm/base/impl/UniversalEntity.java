package dsm.base.impl;


import dsm.base.BaseEntity;

import java.util.Base64;

/**
 * @ClassName : utils.base.entity.impl.UniversalEntity
 * @Description :
 * @Date 2021-04-03 16:56:58
 * @Author ZhangHL
 */
public class UniversalEntity implements BaseEntity {
    private String guid=null;
    private String authLevel=null;
    private String src=null;
    private String to=null;
    private String level=null;
    private String message=null;
    private String messageType=null;
    private String compressCode=null;
    private String timestamp=null;
    private String hashCode=null;

    @Override
    public String toString() {
        return "UniversalEntity{" +
                "guid='" + guid + '\'' +
                ", authLevel='" + authLevel + '\'' +
                ", src='" + src + '\'' +
                ", to='" + to + '\'' +
                ", level='" + level + '\'' +
                ", message='" + message + '\'' +
                ", messageType='" + messageType + '\'' +
                ", compressCode='" + compressCode + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", hashCode='" + hashCode + '\'' +
                '}';
    }

    /**
     * 为构造hash函数构造的字符串
     * @class
     * @Param
     * @return  str-字符串
     * @Author Zhang huai lan
     * @Date 17:01 2021/4/3
     * @Version V1.0
     **/
    public String str4Hash(){
        return guid+authLevel+src+to+level+message+messageType+compressCode+timestamp;
    }
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCompressCode() {
        return compressCode;
    }

    public void setCompressCode(String compressCode) {
        this.compressCode = compressCode;
    }

    @Override
    public String getHashCode() {
        return hashCode;
    }

    @Override
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(String authLevel) {
        this.authLevel = authLevel;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
