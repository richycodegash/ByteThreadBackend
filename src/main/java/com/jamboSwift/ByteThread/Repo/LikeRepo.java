package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepo extends JpaRepository<Like, UUID> {
    // Returns a list of Post IDs from the given list that this account has liked
    @Query("SELECT l.post.id FROM Like l WHERE l.account = :account AND l.post.id IN :postIds")
    List<UUID> findLikedPostIdsByAccountAndPostIds(@Param("account") Account account, @Param("postIds") List<UUID> postIds);

    boolean existsByAccount(Account account);

    Optional<Like> findByAccount(Account account);
}
