package com.example.vault.service;

import com.example.vault.entity.Credential;
import com.example.vault.entity.User;
import com.example.vault.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CredentialService {
    private final CredentialRepository credentialRepository;

    public Credential createCredential(User user, Credential credential) {
        credential.setUser(user);
        return credentialRepository.save(credential);
    }

    public List<Credential> listForUser(User user) {
        return credentialRepository.findByUser(user);
    }

    public Credential updateCredential(User user, Long id, Credential updated) {
        Credential existing = credentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if (!existing.getUser().getId().equals(user.getId())) throw new RuntimeException("Forbidden");
        existing.setTitle(updated.getTitle());
        existing.setUsername(updated.getUsername());
        existing.setCiphertext(updated.getCiphertext());
        existing.setIv(updated.getIv());
        existing.setSalt(updated.getSalt());
        existing.setNotes(updated.getNotes());
        return credentialRepository.save(existing);
    }

    public void deleteCredential(User user, Long id) {
        Credential existing = credentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if (!existing.getUser().getId().equals(user.getId())) throw new RuntimeException("Forbidden");
        credentialRepository.delete(existing);
    }
}
