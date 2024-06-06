package com.lawencon.payroll.service.impl;

import org.springframework.stereotype.Service;

import com.lawencon.payroll.dto.file.FileReqDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.model.File;
import com.lawencon.payroll.repository.FileRepository;
import com.lawencon.payroll.service.FileService;
import com.lawencon.payroll.service.PrincipalService;
import com.lawencon.payroll.util.FtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final PrincipalService principalService;

    @Override
    public File saveFile(String content, String extension) {
        final var file = new File();
        
        file.setFileContent(content);
        file.setFileExtension(extension);

        file.setCreatedBy(principalService.getUserId());

        return fileRepository.save(file);
    }

    @Override
    public File loadById(String id) {
        final var file = fileRepository.findById(id);
        return file.get();
    }

    @Override
    public File updateFile(File file) {
        file.setUpdatedBy(principalService.getUserId());
        
        return fileRepository.save(file);
    }

    @Override
    public InsertResDto uploadFile(FileReqDto data) {
        final var base64 = data.getBase64();
        final var extension = data.getExtension();
        final var remoteFile = data.getRemoteFile();

        FtpUtil.sendFile(base64, remoteFile);

        var file = new File();
        file.setFileContent(base64);
        file.setFileExtension(extension);
        file.setCreatedBy(principalService.getUserId());

        file = fileRepository.save(file);

        final var insertRes = new InsertResDto();
        insertRes.setId(file.getId());
        insertRes.setMessage("File has been uploaded");
        
        return insertRes;
    }

    @Override
    public byte[] downloadFile(String id) {
        final String remoteFile = "/Files/attachment22.png";

        return FtpUtil.downloadFile(remoteFile);
    }

    @Override
    public File getById(String id) {
        return fileRepository.findById(id).get();
    }

}
