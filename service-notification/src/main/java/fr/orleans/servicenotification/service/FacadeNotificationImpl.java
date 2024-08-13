package fr.orleans.servicenotification.service;

import fr.orleans.servicenotification.dao.NotificationDAO;
import fr.orleans.servicenotification.entities.Notification;
import fr.orleans.servicenotification.entities.TypeNotification;
import fr.orleans.servicenotification.service.exceptions.NotificationInexistanteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FacadeNotificationImpl implements FacadeNotification{
    private NotificationDAO notificationDAO;

    public FacadeNotificationImpl(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @Override
    public Notification addNotification(long idUtilisateur, String description, TypeNotification typeNotification) {
        Notification n = notificationDAO.save(Notification.builder()
                .idUtilisateur(idUtilisateur)
                .description(description)
                .typeNotification(typeNotification)
                .build());

        return n;
    }

    @Override
    public Notification getNotificationById(long idNotification) throws NotificationInexistanteException {
        Optional<Notification> notification = notificationDAO.findById(idNotification);
        if(notification.isEmpty())
            throw new NotificationInexistanteException();
        return notification.get();
    }

    @Override
    public Page<Notification> getNotificationsByIdUtilisateur(long idUtilisateur, Pageable pageable){
        return notificationDAO.findAllByIdUtilisateur(idUtilisateur, pageable);
    }
}
