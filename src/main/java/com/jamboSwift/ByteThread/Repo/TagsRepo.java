package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TagsRepo extends JpaRepository<Tag, UUID> {
    Optional<Tag> findBySlug(String slug);
}
