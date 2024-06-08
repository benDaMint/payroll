package com.lawencon.payroll.dto.document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentDownloadResDto {
    private String fileName;
    private byte[] fileBytes;
}
