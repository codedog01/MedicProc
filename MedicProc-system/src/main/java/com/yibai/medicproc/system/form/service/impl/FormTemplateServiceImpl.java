package com.yibai.medicproc.system.form.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibai.medicproc.system.form.domain.FormMetadata;
import com.yibai.medicproc.system.form.domain.vo.DomainVO;
import com.yibai.medicproc.system.form.mapper.FormGeneratorMapper;
import com.yibai.medicproc.system.form.service.FormMetadataService;
import com.yibai.medicproc.system.form.service.FormTemplateService;
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
public class FormTemplateServiceImpl extends ServiceImpl<FormGeneratorMapper, FormMetadata> implements FormTemplateService {
    @Override
    public Map<String, String> dbTableList() {
        String basePackage = "com.yibai";
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> metadataReader.getResource().getFile().getPath().contains("domain"));

        Map<String, String> classes = new HashMap<>();
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                classes.put(clazz.getSimpleName(),clazz.getName());
            } catch (ClassNotFoundException e) {
                // 处理异常
                e.printStackTrace();
            }
        }
        return classes;
    }

    @Override
    public List<DomainVO> domainDetail(String domain) {
        ArrayList<DomainVO> domainVOS = new ArrayList<>();
        try {
            Class<?> aClass = Class.forName(domain);
            Field[] fields = aClass.getDeclaredFields();

            for (Field field : fields) {
                TableField declaredAnnotation = field.getDeclaredAnnotation(TableField.class);
                String fieldName = field.getName();
                String columnName = "";

                if (declaredAnnotation != null) {
                    columnName = declaredAnnotation.value();
                } else {
                    columnName = StrUtil.toUnderlineCase(fieldName);
                }
                DomainVO domainVO = new DomainVO();
                domainVO.setFieldName(fieldName);
                domainVO.setColumnName(columnName);
                domainVOS.add(domainVO);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return domainVOS;
    }
}
