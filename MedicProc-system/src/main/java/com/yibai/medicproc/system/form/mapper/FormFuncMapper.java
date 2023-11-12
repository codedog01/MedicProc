package com.yibai.medicproc.system.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yibai.medicproc.system.form.domain.FormFunction;
import com.yibai.medicproc.system.form.domain.FormMetadata;

import java.util.List;

/**
 * 参数配置 数据层
 *
 * @author ruoyi
 */
public interface FormFuncMapper extends BaseMapper<FormFunction>
{

    List<FormFunction> pageList(FormFunction formFunction);
}
