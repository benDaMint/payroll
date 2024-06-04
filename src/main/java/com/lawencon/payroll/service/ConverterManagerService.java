package com.lawencon.payroll.service;

import com.lawencon.payroll.converter.Converter;

public interface ConverterManagerService {
  void register(String suffix, Converter converter);
  boolean convert(String source, String target);
}
