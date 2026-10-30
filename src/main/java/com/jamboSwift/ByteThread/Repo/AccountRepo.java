package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepo extends JpaRepository<Account, UUID> {
    boolean existsByAccountName(String accountName);

    Optional<Account> findByAccountName(String accountName);
}
