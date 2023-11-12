package com.yibai.medicproc.system.form.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibai.medicproc.system.form.domain.FormFunction;
import com.yibai.medicproc.system.form.domain.FormMetadata;
import com.yibai.medicproc.system.form.mapper.FormFuncMapper;
import com.yibai.medicproc.system.form.mapper.FormGeneratorMapper;
import com.yibai.medicproc.system.form.service.FormFuncService;
import com.yibai.medicproc.system.form.service.FormMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2023/11/6
 */
@Service
public class FormFuncServiceImpl extends ServiceImpl<FormFuncMapper, FormFunction> implements FormFuncService {

    @Autowired
    FormFuncMapper formFuncMapper;


    @Override
    public List<FormFunction> pageList(FormFunction formFunction) {
        return formFuncMapper.pageList(formFunction);
    }
}
