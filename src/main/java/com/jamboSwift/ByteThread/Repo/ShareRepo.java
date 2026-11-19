package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.ShareInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShareRepo extends JpaRepository<ShareInstance, UUID> {
}
