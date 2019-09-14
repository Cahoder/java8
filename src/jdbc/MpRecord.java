package jdbc;

import java.lang.String;

/**
 * 将数据库结构和类对应起来这种方式存储操作数据库最常用
 *
 * MYSQL mini_program 数据库 mp_record 数据表
 */
@MpRecordTableAnnotation(TABLE = "mp_record",COMMENT = "聊天表",SUPPLIERS = {"AUTO_INCREMENT=60001"})
public class MpRecord {
    @MpRecordFieldAnnotation(AUTO_INCREMENT = true,FIELD_NAME = "id",FIELD_TYPE = "int",FIELD_LENGTH = 11,FIELD_COMMENT = "ID")
    private Integer id;
    @MpRecordFieldAnnotation(FIELD_NAME = "openid",FIELD_TYPE = "varchar",FIELD_LENGTH = 50,IS_NULL = false,IS_DEFAULT = true, FIELD_COMMENT = "粉丝openid")
    private String openid;
    @MpRecordFieldAnnotation(FIELD_NAME = "service_id",FIELD_TYPE = "int",FIELD_LENGTH = 11,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "服务号ID")
    private Integer service_id;
    @MpRecordFieldAnnotation(FIELD_NAME = "admin_user_id",FIELD_TYPE = "int",FIELD_LENGTH = 11,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "客服ID")
    private Integer admin_user_id;
    @MpRecordFieldAnnotation(FIELD_NAME = "content",FIELD_TYPE = "text",FIELD_LENGTH = 255,FIELD_COMMENT = "内容，用json存储")
    private String content;
    @MpRecordFieldAnnotation(FIELD_NAME = "is_read",FIELD_TYPE = "tinyint",FIELD_LENGTH = 1,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "是否已读(0/否,1/是)")
    private Integer is_read;
    @MpRecordFieldAnnotation(FIELD_NAME = "is_download",FIELD_TYPE = "tinyint",FIELD_LENGTH = 1,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "是否已下载(0/否,1/是)")
    private Integer is_download;
    @MpRecordFieldAnnotation(FIELD_NAME = "type",FIELD_TYPE = "smallint",FIELD_LENGTH = 6,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "消息类型(0/粉丝发送,1/客服发送,2/系统发送,3/关键词)")
    private Integer type;
    @MpRecordFieldAnnotation(FIELD_NAME = "created_at",FIELD_TYPE = "int",FIELD_LENGTH = 11,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "创建时间")
    private Integer created_at;

    //空参构造器必须要有
    public MpRecord(){}

    public MpRecord(String openid, Integer service_id, Integer admin_user_id, String content, Integer is_read, Integer is_download, Integer type, Integer created_at) {
        this.openid = openid;
        this.service_id = service_id;
        this.admin_user_id = admin_user_id;
        this.content = content;
        this.is_read = is_read;
        this.is_download = is_download;
        this.type = type;
        this.created_at = created_at;
    }

    public MpRecord(Integer id, String openid, Integer service_id, Integer admin_user_id, String content, Integer is_read, Integer is_download, Integer type, Integer created_at) {
        this.id = id;
        this.openid = openid;
        this.service_id = service_id;
        this.admin_user_id = admin_user_id;
        this.content = content;
        this.is_read = is_read;
        this.is_download = is_download;
        this.type = type;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getService_id() {
        return service_id;
    }

    public void setService_id(Integer service_id) {
        this.service_id = service_id;
    }

    public Integer getAdmin_user_id() {
        return admin_user_id;
    }

    public void setAdmin_user_id(Integer admin_user_id) {
        this.admin_user_id = admin_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIs_read() {
        return is_read;
    }

    public void setIs_read(Integer is_read) {
        this.is_read = is_read;
    }

    public Integer getIs_download() {
        return is_download;
    }

    public void setIs_download(Integer is_download) {
        this.is_download = is_download;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Integer created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "MpRecord{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", service_id=" + service_id +
                ", admin_user_id=" + admin_user_id +
                ", content='" + content + '\'' +
                ", is_read=" + is_read +
                ", is_download=" + is_download +
                ", type=" + type +
                ", created_at=" + created_at +
                '}';
    }
}
