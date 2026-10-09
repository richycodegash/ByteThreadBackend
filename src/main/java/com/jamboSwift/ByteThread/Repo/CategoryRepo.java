package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category, UUID> {
    boolean existsBySlug(String categorySlug);

    Optional<Category> findBySlug(String categorySlug);
}
