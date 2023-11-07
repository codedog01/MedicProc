package com.yibai.medicproc.system.form.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibai.medicproc.common.utils.bean.BeanUtils;
import com.yibai.medicproc.system.form.domain.FormMetadata;
import com.yibai.medicproc.system.form.domain.vo.DomainVO;
import com.yibai.medicproc.system.form.mapper.FormGeneratorMapper;
import com.yibai.medicproc.system.form.service.FormMetadataService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2023/11/6
 */
@Service
public class FormMetadataServiceImpl extends ServiceImpl<FormGeneratorMapper, FormMetadata> implements FormMetadataService {

}
