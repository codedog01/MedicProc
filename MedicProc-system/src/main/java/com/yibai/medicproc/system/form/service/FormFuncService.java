package com.yibai.medicproc.system.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yibai.medicproc.system.form.domain.FormFunction;

import java.util.List;

public interface FormFuncService extends IService<FormFunction> {
    List<FormFunction> pageList(FormFunction formFunction);
}
