package fr.orleans.servicenotification.service;

import fr.orleans.servicenotification.entities.Notification;
import fr.orleans.servicenotification.entities.TypeNotification;
import fr.orleans.servicenotification.service.exceptions.NotificationInexistanteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacadeNotification {
    Notification addNotification(long idUtilisateur, String description, TypeNotification typeNotification);

    Notification getNotificationById(long idNotification) throws NotificationInexistanteException;

    Page<Notification> getNotificationsByIdUtilisateur(long idUtilisateur, Pageable pageable);
}
