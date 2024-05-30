package com.lawencon.payroll.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationResDto {
  private String notificationId;
  private String notificationCode;
  private String notificationBody;
  private String notificationHeader;
  private String routeLink;
  private Boolean isActive;
}
