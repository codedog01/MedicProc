package com.yibai.medicproc.web.controller.form;

import com.yibai.medicproc.common.core.domain.AjaxResult;
import com.yibai.medicproc.system.form.service.FormMetadataService;
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

    @GetMapping("/dbTableList")
    public AjaxResult dbTableList() {
        return success(formTemplateService.dbTableList());
    }

    @GetMapping("/domainDetail")
    public AjaxResult domainDetail(String domain) {
        return success(formTemplateService.domainDetail(domain));
    }


}
