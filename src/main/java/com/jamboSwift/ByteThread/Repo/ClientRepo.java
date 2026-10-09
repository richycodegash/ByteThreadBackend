package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepo extends JpaRepository<Client, UUID> {
    boolean existsByEmail(String email);

    Optional<Client> findByEmail(String email);
}
