package com.yibai.medicproc.web.controller.form;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yibai.medicproc.common.annotation.Log;
import com.yibai.medicproc.common.core.controller.BaseController;
import com.yibai.medicproc.common.core.domain.AjaxResult;
import com.yibai.medicproc.common.core.page.TableDataInfo;
import com.yibai.medicproc.common.enums.BusinessType;
import com.yibai.medicproc.system.form.domain.FormMetadata;
import com.yibai.medicproc.system.form.service.FormMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.yibai.medicproc.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/form/generator")
public class FormGeneratorController extends BaseController {

    @Autowired
    FormMetadataService formMetadataService;


    //    @PreAuthorize("@ss.hasPermi('form:list')")
    @GetMapping("/list")
    public TableDataInfo list(FormMetadata formMetadata) {
        startPage();
        List<FormMetadata> list = formMetadataService.list(new LambdaQueryWrapper<>(formMetadata));
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('form:list:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody FormMetadata formMetadata) {
        formMetadataService.save(formMetadata);
        return success();
    }


    //    @PreAuthorize("@ss.hasPermi('form:list:edit')")
    @Log(title = "表单设计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody FormMetadata formMetadata) {
        formMetadata.setUpdateBy(getUserId());
        return toAjax(formMetadataService.updateById(formMetadata));
    }

    //    @PreAuthorize("@ss.hasPermi('form:list:remove')")
    @Log(title = "表单设计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{formIds}")
    public AjaxResult remove(@PathVariable Long[] formIds) {
        formMetadataService.removeBatchByIds(Arrays.asList(formIds));
        return success();
    }



}
