package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepo extends JpaRepository<Account, UUID> {
    boolean existsByAccountName(String accountName);
}
