package com.lawencon.payroll.converter;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.lawencon.payroll.service.ConverterManagerService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by zhangshichen on 2017/8/3.
 */
@Component
@RequiredArgsConstructor
public class WordConverter implements Converter {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ConverterManagerService converterManagerService;

    @PostConstruct
    public void init() {
        converterManagerService.register("doc", this);
        converterManagerService.register("docx", this);
        converterManagerService.register("docm", this);
    }

    public boolean convert(String source, String target) {
        ComThread.InitSTA();
        ActiveXComponent app = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);

            Dispatch docs = app.getProperty("Documents").toDispatch();
            Dispatch doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();

            File tofile = new File(target);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", target, docSaveAsPDF);
            Dispatch.call(doc, "Close", false);
        } catch (Exception e) {
            LOG.error("convert exception:", e);
            return false;
        } finally {
            if (app != null)
                app.invoke("Quit", wdNotSaveChanges);
            ComThread.Release();
        }
        return true;
    }
}
