package com.yibai.medicproc.system.form.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2023/11/6
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("form_metadata")
public class FormMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单路由地址
     */
    private String routeUrl;



    /**
     * 版本号
     */
    @Version
    private Integer version;


    /**
     * json数据
     */
    private String json;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

}
