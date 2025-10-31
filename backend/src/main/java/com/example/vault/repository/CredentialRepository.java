package com.example.vault.repository;

import com.example.vault.entity.Credential;
import com.example.vault.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    List<Credential> findByUser(User user);
}
