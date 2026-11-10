package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Enums.NotificationStatus;
import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepo extends JpaRepository<Notification, UUID> {
    Slice<Notification> findByAccountAndStatus(Account account, NotificationStatus status, Pageable pageable);
}
