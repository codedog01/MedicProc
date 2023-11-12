package com.yibai.medicproc.system.form.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

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
@TableName("form_function")
public class FormFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 表单id
     */
    private String formId;


    /**
     * 方法名称
     */
    private String funcName;

    /**
     * 方法体
     */
    private String funcBody;

    /**
     * 是否为全局方法
     */
    private String isGlobal;


    /**
     * 备注
     */
    private String remark;


    /**
     * 表单名称
     */
    @TableField(exist = false)
    private String formName;
}
