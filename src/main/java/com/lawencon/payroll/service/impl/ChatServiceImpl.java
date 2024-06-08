package com.lawencon.payroll.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.payroll.constant.Roles;
import com.lawencon.payroll.dto.chat.ChatMessageDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.model.Chat;
import com.lawencon.payroll.model.Notification;
import com.lawencon.payroll.model.NotificationTemplate;
import com.lawencon.payroll.model.User;
import com.lawencon.payroll.repository.ChatRepository;
import com.lawencon.payroll.repository.NotificationRepository;
import com.lawencon.payroll.repository.NotificationTemplateRepository;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.ChatService;
import com.lawencon.payroll.service.ClientAssignmentService;
import com.lawencon.payroll.service.PrincipalService;
import com.lawencon.payroll.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final PrincipalService principalService;
    private final RoleService roleService;
    private final ClientAssignmentService clientAssignmentService;
    private final NotificationTemplateRepository notificationTemplateRepository;

    @Override
    public ArrayList<ChatMessageDto> getChats(String targetId) {
        final ArrayList<ChatMessageDto> chatsRes = new ArrayList<>();

        String currentUserId = principalService.getUserId();
        targetId = currentUserId.equals(targetId) ? clientAssignmentService.getByClientId(currentUserId).getPsId()
                : targetId;

        User currentUser = userRepository.findById(currentUserId).orElse(null);
        User targetUser = userRepository.findById(targetId).orElse(null);

        final List<Chat> chatsIn = chatRepository.findByRecipientIdOrCreatedBy(currentUser, targetId);
        final List<Chat> chatsOut = chatRepository.findByRecipientIdOrCreatedBy(targetUser, currentUserId);

        for (Chat chat : chatsIn) {
            final ChatMessageDto chatRes = new ChatMessageDto();
            chatRes.setMessage(chat.getMessage());
            chatRes.setTimestamp(chat.getCreatedAt());
            chatRes.setRecipientId(chat.getRecipientId().getId());
            chatRes.setSenderId(chat.getCreatedBy());
            chatsRes.add(chatRes);
        }

        for (Chat chat : chatsOut) {
            final ChatMessageDto chatRes = new ChatMessageDto();
            chatRes.setMessage(chat.getMessage());
            chatRes.setTimestamp(chat.getCreatedAt());
            chatRes.setRecipientId(chat.getRecipientId().getId());
            chatRes.setSenderId(chat.getCreatedBy());
            chatsRes.add(chatRes);
        }

        chatsRes.sort((chat1, chat2) -> chat1.getTimestamp().compareTo(chat2.getTimestamp()));

        return chatsRes;
    }

    @Override
    @Transactional
    public InsertResDto saveChat(ChatMessageDto chatReq) {
        Optional<User> currentUser = userRepository.findById(chatReq.getSenderId());
        User user = currentUser.orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = new Chat();
        User recipient = userRepository.findById(chatReq.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));
        chat.setMessage(chatReq.getMessage());
        chat.setRecipientId(recipient);
        chat.setCreatedBy(user.getId());
        chat.setCreatedAt(LocalDateTime.now());
        chat = chatRepository.save(chat);

        final String userRoleName = roleService.getById(user.getRoleId().getId()).getRoleName();
        final String chatUrl = userRoleName.equals(Roles.RL003.getRoleName()) ? user.getId() : recipient.getId();

        Notification notification = new Notification();
        notification.setUser(recipient);
        notification.setRouteLink("chat/" + chatUrl);

        NotificationTemplate notificationTemplate = new NotificationTemplate();
        notificationTemplate.setNotificationHeader("New chat from: " + user.getUserName() + " - " + userRoleName);
        notificationTemplate.setNotificationBody(chatReq.getMessage());
        notificationTemplate.setNotificationCode("NTCUS");
        notificationTemplate.setCreatedBy(principalService.getUserId());
        notificationTemplate = notificationTemplateRepository.save(notificationTemplate);

        notification.setNotificationTemplate(
                notificationTemplate);

        notification.setCreatedBy(user.getId());
        notification = notificationRepository.save(notification);

        InsertResDto insertRes = new InsertResDto();
        insertRes.setId(chat.getId());
        insertRes.setMessage("Chat successfully saved!");
        return insertRes;
    }

    @Override
    public ChatMessageDto findChat(String id) {
        ChatMessageDto chatRes = new ChatMessageDto();
        Optional<Chat> chat = chatRepository.findById(id);

        if (chat.isPresent()) {
            Chat selectedChat = chat.get();
            chatRes.setMessage(selectedChat.getMessage());
            chatRes.setRecipientId(selectedChat.getRecipientId().getId());
            chatRes.setSenderId(selectedChat.getCreatedBy());
            chatRes.setTimestamp(selectedChat.getCreatedAt());
            return chatRes;
        }

        return null;
    }
}
