package com.lawencon.payroll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_r_notification")
public class Notification extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "notification_template_id", nullable = false)
    private NotificationTemplate notificationTemplate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "route_link", nullable = false)
    private String routeLink;
}
