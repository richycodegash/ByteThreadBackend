package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Enums.FollowStatus;
import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FollowRepo extends JpaRepository<Follow, UUID> {
    boolean existsByFollowedAccountAndFollowingAccountAndStatus(Account followedAccount, Account followingAccount, FollowStatus status);

    Optional<Follow> findByFollowedAccountAndFollowingAccount(Account followedAccount, Account followingAccount);
}
