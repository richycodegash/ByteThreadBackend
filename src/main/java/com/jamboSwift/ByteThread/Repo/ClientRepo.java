package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepo extends JpaRepository<Client, UUID> {
    boolean existsByEmail(String email);

    Optional<Client> findByEmail(String email);
}
