package com.lawencon.payroll.util;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class SaveLocalPdfUtil {
    private static final String TMP_DIR = "tmp";

    public static String saveLocal(InputStream file, String fileName) {
        InputStream input = null;
        String filename = fileName;
        if (StringUtils.lastIndexOf(filename, "/") != -1) {
            filename = StringUtils.substringAfterLast(filename, "/");
        }
        if (StringUtils.lastIndexOf(filename, "\\") != -1) {
            filename = StringUtils.substringAfterLast(filename, "\\");
        }
        String localName = TMP_DIR + File.separator + new Date().getTime() + "_" + filename;
        try {
            input = file;
            File destination = new File(localName);
            FileUtils.copyToFile(input, destination);
            localName = destination.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(input);
        }
        return localName;
    }
}
