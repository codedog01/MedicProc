package com.yibai.medicproc.web.controller.form;

import com.yibai.medicproc.common.core.domain.AjaxResult;
import com.yibai.medicproc.system.form.service.FormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yibai.medicproc.common.core.domain.AjaxResult.success;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2023/11/7
 */
@RestController
@RequestMapping("/form/template")
public class FormTemplateController {

    @Autowired
    FormTemplateService formTemplateService;

    @GetMapping("/getEntityList")
    public AjaxResult getEntityList() {
        return success(formTemplateService.getEntityList());
    }

    @GetMapping("/entityDetail")
    public AjaxResult entityDetail(String entity) {
        return success(formTemplateService.entityDetail(entity));
    }


}
