package fr.orleans.servicenotification.dao;

import fr.orleans.servicenotification.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationDAO extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByIdUtilisateur(long idUtilisateur, Pageable pageable);
}
