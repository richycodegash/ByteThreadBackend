package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepo extends JpaRepository<Notification, UUID> {
}
