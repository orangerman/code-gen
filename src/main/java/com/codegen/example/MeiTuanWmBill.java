package com.codegen.example;

import com.codegen.annotation.CodeType;
import com.codegen.annotation.GenerateCode;

import lombok.Data;

@Data
@GenerateCode(
        value = {CodeType.JOB, CodeType.MAPPER, CodeType.SERVICE},
        basePackage = "com.codegen.example",
        author = "Fanzhiqiang",
        tableName = "t_meituan_wm_bill",
        billType = "MeiTuanWm")
public class MeiTuanWmBill {

    private Long id;

    private String brand_no;

}

