package com.lawencon.payroll.dto.file;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileReqDto {
    private String base64;
    private String extension;
    private String remoteFile;
}
