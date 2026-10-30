package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {
    boolean existsBySlug(String categorySlug);

    Optional<Category> findBySlug(String categorySlug);
}
