package com.yibai.medicproc.web.controller.form;

import com.yibai.medicproc.system.form.service.FormGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/form/generator")
public class FormGeneratorController {

    @Autowired
    FormGeneratorService formGeneratorService;



}

