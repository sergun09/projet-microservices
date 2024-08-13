package fr.orleans.servicenotification.dtos;

import fr.orleans.servicenotification.entities.TypeNotification;

import java.io.Serializable;

public record NotificationDTO (
    long idNotification,
    long idUtilisateur,
    String description,
    TypeNotification typeNotification
) implements Serializable
{}