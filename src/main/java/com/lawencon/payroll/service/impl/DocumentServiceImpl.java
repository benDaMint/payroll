package com.lawencon.payroll.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.payroll.constant.NotificationCodes;
import com.lawencon.payroll.constant.ScheduleRequestTypes;
import com.lawencon.payroll.dto.document.CalculatedResDto;
import com.lawencon.payroll.dto.document.DocumentDownloadResDto;
import com.lawencon.payroll.dto.document.DocumentReqDto;
import com.lawencon.payroll.dto.document.DocumentResDto;
import com.lawencon.payroll.dto.document.DocumentsResDto;
import com.lawencon.payroll.dto.document.OldDocumentResDto;
import com.lawencon.payroll.dto.document.UpdateCalculatedDocumentReqDto;
import com.lawencon.payroll.dto.document.UpdateDocumentReqDto;
import com.lawencon.payroll.dto.document.UpdateDocumentScheduleReqDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.model.CalculatedPayrollDocuments;
import com.lawencon.payroll.model.Document;
import com.lawencon.payroll.model.Notification;
import com.lawencon.payroll.repository.CalculatedPayrollDocumentsRepository;
import com.lawencon.payroll.repository.ClientAssignmentRepository;
import com.lawencon.payroll.repository.DocumentRepository;
import com.lawencon.payroll.repository.NotificationRepository;
import com.lawencon.payroll.repository.NotificationTemplateRepository;
import com.lawencon.payroll.repository.ScheduleRepository;
import com.lawencon.payroll.repository.ScheduleRequestTypeRepository;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.DocumentService;
import com.lawencon.payroll.service.EmailService;
import com.lawencon.payroll.service.PrincipalService;
import com.lawencon.payroll.util.FtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final ScheduleRepository scheduleRepository;
    private final PrincipalService principalService;
    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final ScheduleRequestTypeRepository scheduleRequestTypeRepository;
    private final ClientAssignmentRepository clientAssignmentRepository;
    private final CalculatedPayrollDocumentsRepository calculatedPayrollDocumentsRepository;
    private final UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public InsertResDto createDocuments(DocumentReqDto data) {
        final var insertRes = new InsertResDto();

        final var schedule = scheduleRepository.findById(data.getScheduleId()).get();

        final var documentsReq = data.getDocumentsReqDto();

        final var scheduleRequestType = scheduleRequestTypeRepository
                .findByScheduleRequestCode(ScheduleRequestTypes.SQT02.name());

        schedule.setScheduleRequestType(scheduleRequestType);

        final var calculatedSalaryDocument = new CalculatedPayrollDocuments();
        final var paycheckDocument = new CalculatedPayrollDocuments();

        calculatedSalaryDocument.setActivity("Calculated Salary Document");
        calculatedSalaryDocument.setSchedule(schedule);
        calculatedSalaryDocument.setCreatedBy(principalService.getUserId());

        paycheckDocument.setActivity("Paycheck Document");
        paycheckDocument.setSchedule(schedule);
        paycheckDocument.setCreatedBy(principalService.getUserId());

        calculatedPayrollDocumentsRepository.save(calculatedSalaryDocument);
        calculatedPayrollDocumentsRepository.save(paycheckDocument);

        documentsReq.forEach(documentReq -> {

            final var deadline = LocalDateTime.parse(documentReq.getDocumentDeadline());
            final var activity = documentReq.getActivity();

            var document = new Document();
            document.setDocumentDeadline(deadline);
            document.setActivity(activity);
            document.setSchedule(schedule);
            document.setIsSignedByClient(false);
            document.setIsSignedByPs(false);
            document.setCreatedBy(principalService.getUserId());

            document = documentRepository.save(document);
        });

        final var notificationTemplate = notificationTemplateRepository
                .findByNotificationCode(NotificationCodes.NT002.name());

        final var routeLink = "payrolls/" + data.getScheduleId();

        final var notification = new Notification();
        notification.setNotificationTemplate(notificationTemplate);
        notification.setCreatedBy(principalService.getUserId());
        notification.setRouteLink(routeLink);
        notification.setUser(schedule.getClientAssignment().getClientId());

        notificationRepository.save(notification);

        final var user = schedule.getClientAssignment().getClientId();

        final var email = user.getEmail();

        final var subject = "Your New Payroll Schedule Is Ready!";

        Map<String, Object> templateBody = new HashMap<>();
        templateBody.put("date", LocalDateTime.now());
        templateBody.put("subject", subject);
        templateBody.put("username", user.getUserName());
        templateBody.put("message",
                "We are excited to inform you that your new payroll schedule has been successfully created. You can now view and uppdate your upcoming payroll activities according to the schedule.");

        final Runnable runnable = () -> {
            try {
                emailService.sendEmail(email, subject, templateBody);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };

        final var mailThread = new Thread(runnable);
        mailThread.start();

        scheduleRepository.save(schedule);

        insertRes.setId(null);
        insertRes.setMessage("Document(s) have been made!");
        return insertRes;
    }

    @Override
    public DocumentResDto getDocumentsByScheduleId(String scheduleId) {
        final var documentRes = new DocumentResDto();
        final var documentsRes = new ArrayList<DocumentsResDto>();
        final var finalDocumentsRes = new ArrayList<CalculatedResDto>();

        final var documents = documentRepository.findByScheduleIdOrderByDocumentDeadlineAsc(scheduleId);
        final var finalDocuments = calculatedPayrollDocumentsRepository.findAllByScheduleId(scheduleId);
        final var schedule = scheduleRepository.findById(scheduleId);
        final var clientAssignmentId = schedule.get().getClientAssignment().getId();

        documentRes.setClientAssignmentId(clientAssignmentId);

        documents.forEach(document -> {
            final var newDocumentRes = new DocumentsResDto();

            final var id = document.getId();
            final var activity = document.getActivity();
            final var deadline = document.getDocumentDeadline().toString();
            final var directory = document.getDocumentDirectory();
            final var name = document.getDocumentName();
            final var isSignedByClient = document.getIsSignedByClient();
            final var isSignedByPs = document.getIsSignedByPs();

            newDocumentRes.setDocumentId(id);
            newDocumentRes.setActivity(activity);
            newDocumentRes.setDocumentDeadline(deadline);
            newDocumentRes.setDocumentDirectory(directory);
            newDocumentRes.setDocumentName(name);
            newDocumentRes.setIsSignedByClient(isSignedByClient);
            newDocumentRes.setIsSignedByPs(isSignedByPs);

            documentsRes.add(newDocumentRes);
        });

        finalDocuments.forEach(finalDocument -> {
            final var finalDocumentRes = new CalculatedResDto();

            final var activity = finalDocument.getActivity();
            final var documentId = finalDocument.getId();
            final var documentName = finalDocument.getDocumentName();
            final var documentDirectory = finalDocument.getDocumentDirectory();

            finalDocumentRes.setActivity(activity);
            finalDocumentRes.setDocumentId(documentId);
            finalDocumentRes.setDocumentName(documentName);
            finalDocumentRes.setDocumentDirectory(documentDirectory);

            finalDocumentsRes.add(finalDocumentRes);
        });

        documentRes.setDocumentsRes(documentsRes);

        documentRes.setCalculatedDataResDto(finalDocumentsRes);

        return documentRes;
    }

    @Override
    @Transactional
    public UpdateResDto rescheduleDocuments(List<UpdateDocumentScheduleReqDto> data) {
        final var updateRes = new UpdateResDto();

        data.forEach(documentReq -> {
            var oldDoc = documentRepository.findById(documentReq.getDocumentId()).get();

            oldDoc.setDocumentDeadline(
                    LocalDateTime.parse(documentReq.getDocumentDeadline(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            oldDoc.setUpdatedBy(principalService.getUserId());

            oldDoc = documentRepository.save(oldDoc);
        });

        final var doc = documentRepository.findById(data.get(0).getDocumentId());

        final var schedule = scheduleRepository.findById(doc.get().getSchedule().getId());

        final var user = userRepository.findById(schedule.get().getClientAssignment().getClientId().getId());

        final var email = user.get().getEmail();

        final var subject = "Your Payroll Deadline Has Been Rescheduled";

        Map<String, Object> templateBody = new HashMap<>();
        templateBody.put("date", LocalDateTime.now());
        templateBody.put("subject", "Payroll Deadline Rescheduled");
        templateBody.put("username", user.get().getUserName());
        templateBody.put("message",
                "We are writing to inform you that the payroll deadline has been rescheduled. Please check the details of your payroll on our website.");

        final Runnable runnable = () -> {
            try {
                emailService.sendEmail(email, subject, templateBody);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };

        final var mailThread = new Thread(runnable);
        mailThread.start();

        final var notificationTemplate = notificationTemplateRepository
                .findByNotificationCode(NotificationCodes.NT002.name());

        final var routeLink = "payrolls/" + schedule.get().getId();

        final var notification = new Notification();
        notification.setNotificationTemplate(notificationTemplate);
        notification.setCreatedBy(principalService.getUserId());
        notification.setRouteLink(routeLink);
        notification.setUser(schedule.get().getClientAssignment().getClientId());

        notificationRepository.save(notification);

        updateRes.setVersion(null);
        updateRes.setMessage("Document(s) have been rescheduled!");
        return updateRes;
    }

    @Override
    @Transactional
    public UpdateResDto uploadDocument(UpdateDocumentReqDto data) {
        final var updateRes = new UpdateResDto();

        var oldDocument = documentRepository.findById(data.getDocumentId()).get();

        final var lastDocument = documentRepository
                .findFirstByScheduleIdOrderByDocumentDeadlineDesc(data.getScheduleId());

        final var current = LocalDateTime.now();
        final var month = current.getMonth() + "-" + current.getYear();
        final var base64 = data.getBase64();
        final var clientAssignmentId = data.getClientAssignmentId();
        final var documentName = data.getDocumentName();

        final var clientAssignment = clientAssignmentRepository.findById(clientAssignmentId);

        final var userId = clientAssignment.get().getClientId().getId();

        final var directory = "/Files/" + userId + "/" + month + "/";

        final var remoteFile = directory + documentName;

        FtpUtil.sendFile(base64, remoteFile);

        oldDocument.setDocumentName(documentName);
        oldDocument.setDocumentDirectory(directory);

        oldDocument.setUpdatedBy(principalService.getUserId());

        final var notification = new Notification();
        final var routeLink = "payrolls/" + data.getScheduleId();
        notification.setCreatedBy(principalService.getUserId());
        notification.setRouteLink(routeLink);

        if (data.getIsSignedByPS()) {
            oldDocument.setIsSignedByPs(true);
            if (data.getDocumentId().equals(lastDocument.get().getId())) {
                final var scheduleRequestType = scheduleRequestTypeRepository
                        .findByScheduleRequestCode(ScheduleRequestTypes.SQT03.name());
                final var schedule = scheduleRepository.findById(data.getScheduleId());
                if (schedule.isPresent()) {
                    schedule.get().setScheduleRequestType(scheduleRequestType);
                    ;

                    scheduleRepository.save(schedule.get());
                }
            }

            final var notificationTemplate = notificationTemplateRepository
                    .findByNotificationCode(NotificationCodes.NT004.name());
            notification.setNotificationTemplate(notificationTemplate);
            notification.setUser(clientAssignment.get().getClientId());

            final var user = clientAssignment.get().getClientId();
            final var email = user.getEmail();

            final var subject = "Your Document Has Been Signed";

            Map<String, Object> templateBody = new HashMap<>();
            templateBody.put("date", LocalDateTime.now());
            templateBody.put("subject", subject);
            templateBody.put("username", user.getUserName());
            templateBody.put("message",
                    "We are pleased to inform you that your document has been successfully signed by our payroll service. You can view and download the signed document by logging into your account.");

            final Runnable runnable = () -> {
                try {
                    emailService.sendEmail(email, subject, templateBody);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            };

            final var mailThread = new Thread(runnable);
            mailThread.start();

        } else if (data.getIsSignedByClient()) {
            oldDocument.setIsSignedByClient(true);

            final var notificationTemplate = notificationTemplateRepository
                    .findByNotificationCode(NotificationCodes.NT003.name());
            notification.setNotificationTemplate(notificationTemplate);
            notification.setUser(clientAssignment.get().getPsId());

            final var user = clientAssignment.get().getPsId();
            final var email = user.getEmail();

            final var subject = "New Document Uploaded: Awaiting Your Review";

            Map<String, Object> templateBody = new HashMap<>();
            templateBody.put("date", LocalDateTime.now());
            templateBody.put("subject", subject);
            templateBody.put("username", user.getUserName());
            templateBody.put("message",
                    "A new document has been uploaded by your client [" + clientAssignment.get().getClientId()
                            + "]. Please review the document at your earliest convenience. You can access the document through the PetalPay system.");

            final Runnable runnable = () -> {
                try {
                    emailService.sendEmail(email, subject, templateBody);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            };

            final var mailThread = new Thread(runnable);
            mailThread.start();
        }

        notificationRepository.save(notification);
        oldDocument = documentRepository.save(oldDocument);

        updateRes.setVersion(oldDocument.getVer());
        updateRes.setMessage("Document Has Been Uploaded!");

        return updateRes;
    }

    @Override
    @Transactional
    public UpdateResDto uploadFinalDocument(UpdateCalculatedDocumentReqDto dataRes) {
        final var updateRes = new UpdateResDto();

        final var schedule = scheduleRepository.findById(dataRes.getScheduleId());
        final var scheduleRequestType = scheduleRequestTypeRepository
                .findByScheduleRequestCode(ScheduleRequestTypes.SQT04.name());
        final var clientAssignment = clientAssignmentRepository.findById(dataRes.getClientAssignmentId());
        final var userId = clientAssignment.get().getClientId().getId();
        final var current = LocalDateTime.now();
        final var month = current.getMonth() + "-" + current.getYear();

        schedule.get().setScheduleRequestType(scheduleRequestType);

        scheduleRepository.save(schedule.get());

        dataRes.getDocuments().forEach(data -> {
            final var oldDocument = calculatedPayrollDocumentsRepository.findById(data.getDocumentId());

            final var base64 = data.getBase64();
            final var documentName = data.getDocumentName();

            final var directory = "/Files/" + userId + "/" + month + "/" + "finalDocuments/";

            final var remoteFile = directory + documentName;

            FtpUtil.sendFile(base64, remoteFile);

            oldDocument.get().setDocumentName(documentName);
            oldDocument.get().setDocumentDirectory(directory);
            oldDocument.get().setUpdatedBy(principalService.getUserId());

            calculatedPayrollDocumentsRepository.save(oldDocument.get());
        });

        final var notificationTemplate = notificationTemplateRepository
                .findByNotificationCode(NotificationCodes.NT006.name());

        final var routeLink = "payrolls/" + dataRes.getScheduleId();

        final var notification = new Notification();
        notification.setNotificationTemplate(notificationTemplate);
        notification.setCreatedBy(principalService.getUserId());
        notification.setRouteLink(routeLink);
        notification.setUser(clientAssignment.get().getClientId());

        notificationRepository.save(notification);

        final var user = clientAssignment.get().getClientId();
        final var email = user.getEmail();

        final var subject = "Monthly Payroll Document Uploaded";

        Map<String, Object> templateBody = new HashMap<>();
        templateBody.put("date", LocalDateTime.now());
        templateBody.put("subject", "Payroll Process Completed");
        templateBody.put("username", user.getUserName());
        templateBody.put("message",
                "We are pleased to inform you that the payroll document for this month has been successfully uploaded and the payroll process is now complete. You can access and review the document through the PetalPay website.");

        final Runnable runnable = () -> {
            try {
                emailService.sendEmail(email, subject, templateBody);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };

        final var mailThread = new Thread(runnable);
        mailThread.start();

        updateRes.setVersion(null);
        updateRes.setMessage("Document Upload Success!");

        return updateRes;
    }

    @Override
    @Transactional
    public DocumentDownloadResDto downloadDocument(String id) {
        final var downloadRes = new DocumentDownloadResDto();

        final var document = documentRepository.findById(id).get();

        final var documentName = document.getDocumentName();

        final var remoteFile = (document.getDocumentDirectory()) + documentName;

        downloadRes.setFileName(documentName);
        downloadRes.setFileBytes(FtpUtil.downloadFile(remoteFile));

        return downloadRes;
    }

    @Override
    public DocumentDownloadResDto downloadFinalDocument(String id) {
        final var downloadRes = new DocumentDownloadResDto();

        final var document = calculatedPayrollDocumentsRepository.findById(id).get();

        final var documentName = document.getDocumentName();

        final var remoteFile = (document.getDocumentDirectory()) + documentName;

        downloadRes.setFileName(documentName);
        downloadRes.setFileBytes(FtpUtil.downloadFile(remoteFile));

        return downloadRes;
    }

    @Override
    public List<OldDocumentResDto> getOldDocuments(String scheduleId) {
        final var oldDocumentsRes = new ArrayList<OldDocumentResDto>();
        final var documents = documentRepository.findByScheduleIdOrderByDocumentDeadlineAsc(scheduleId);
        documents.forEach(document -> {
            final var oldDocumentRes = new OldDocumentResDto();

            final var id = document.getId();
            final var activity = document.getActivity();
            final var deadline = document.getDocumentDeadline().toString();

            oldDocumentRes.setActivity(activity);
            oldDocumentRes.setDocumentDeadline(deadline);
            oldDocumentRes.setDocumentId(id);

            oldDocumentsRes.add(oldDocumentRes);
        });

        return oldDocumentsRes;
    }

}
