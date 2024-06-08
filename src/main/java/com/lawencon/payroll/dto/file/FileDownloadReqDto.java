package com.lawencon.payroll.dto.file;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileDownloadReqDto {
    private String remoteFile;
    private String downloadFile;
}
