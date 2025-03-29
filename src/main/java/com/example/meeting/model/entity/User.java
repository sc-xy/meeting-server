package com.example.meeting.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 *

 */
@TableName(value = "users")
@Data
public class User implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户账号
     */
    @TableField("username")
    private String userName;

    /**
     * 用户密码
     */
    @TableField("password")
    private String userPassword;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic()
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}