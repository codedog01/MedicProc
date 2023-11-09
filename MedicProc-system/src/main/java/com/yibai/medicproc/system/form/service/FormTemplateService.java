package com.yibai.medicproc.system.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yibai.medicproc.system.form.domain.FormMetadata;
import com.yibai.medicproc.system.form.domain.vo.DomainVO;

import java.util.List;
import java.util.Map;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2023/11/6
 */
public interface FormTemplateService extends IService<FormMetadata> {
    Map<String, String> getEntityList();

    List<DomainVO> entityDetail(String entity);
}
