package com.yibai.medicproc.web.controller.form;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yibai.medicproc.common.annotation.Log;
import com.yibai.medicproc.common.core.controller.BaseController;
import com.yibai.medicproc.common.core.domain.AjaxResult;
import com.yibai.medicproc.common.core.page.TableDataInfo;
import com.yibai.medicproc.common.enums.BusinessType;
import com.yibai.medicproc.system.form.domain.FormFunction;
import com.yibai.medicproc.system.form.service.FormFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2023/11/10
 */
@RestController
@RequestMapping("/form/func")
public class FormFuncController extends BaseController {
    @Autowired
    FormFuncService formFuncService;


    //    @PreAuthorize("@ss.hasPermi('form:list')")
    @GetMapping("/list")
    public TableDataInfo list(FormFunction formFunction) {
        startPage();
        List<FormFunction> list = formFuncService.pageList(formFunction);
        return getDataTable(list);
    }
    @GetMapping("/listByForm")
    public AjaxResult listByForm(Long formId) {
        QueryWrapper<FormFunction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("form_id", formId).or().eq("is_global", 1);
        List<FormFunction> list = formFuncService.list(queryWrapper);
        return AjaxResult.success(list);
    }
    //    @PreAuthorize("@ss.hasPermi('form:list:add')")
    @Log(title = "表单函数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody FormFunction formFunction) {
        formFuncService.save(formFunction);
        return success();
    }


    //    @PreAuthorize("@ss.hasPermi('form:list:edit')")
    @Log(title = "表单函数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody FormFunction formFunction) {
        return toAjax(formFuncService.updateById(formFunction));
    }

    //    @PreAuthorize("@ss.hasPermi('form:list:remove')")
    @Log(title = "表单设计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{funcIds}")
    public AjaxResult remove(@PathVariable Long[] funcIds) {
        formFuncService.removeBatchByIds(Arrays.asList(funcIds));
        return success();
    }


}
