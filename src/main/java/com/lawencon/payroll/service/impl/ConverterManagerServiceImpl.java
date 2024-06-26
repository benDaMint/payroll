package com.lawencon.payroll.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lawencon.payroll.converter.Converter;
import com.lawencon.payroll.service.ConverterManagerService;

@Service(value = "converter")
public class ConverterManagerServiceImpl implements ConverterManagerService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public  Map<String, Converter> pool = new HashMap<>();

    public void register(String suffix, Converter converter) {
        pool.put(suffix, converter);
    }

    public boolean convert(String source, String target) {
//        pool.forEach((k, v) -> System.out.println("key:" + k + ",value:" + v));
        String filename = StringUtils.substringAfterLast(source, File.separator);
        LOG.info("start converting {} to pdf", filename);
        String suffix = source.substring(source.lastIndexOf(".") + 1).toLowerCase();
        Converter converter = pool.get(suffix);

        long start = System.currentTimeMillis();
        boolean flag = converter != null && converter.convert(source, target);
        long end = System.currentTimeMillis();
        LOG.info("finish converting {} ...cost {} ms.", filename, end - start);
        return flag;

    }
}
