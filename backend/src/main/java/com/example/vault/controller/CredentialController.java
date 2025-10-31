package com.example.vault.controller;

import com.example.vault.dto.CredentialDTO;
import com.example.vault.entity.Credential;
import com.example.vault.entity.User;
import com.example.vault.service.CredentialService;
import com.example.vault.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/credentials")
@RequiredArgsConstructor
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CredentialDTO dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        Credential c = new Credential();
        c.setTitle(dto.getTitle());
        c.setUsername(dto.getUsername());
        c.setCiphertext(dto.getCiphertext());
        c.setIv(dto.getIv());
        c.setSalt(dto.getSalt());
        c.setNotes(dto.getNotes());
        Credential saved = credentialService.createCredential(user, c);
        return ResponseEntity.ok(saved.getId());
    }

    @GetMapping
    public ResponseEntity<?> list(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Credential> list = credentialService.listForUser(user);
        List<CredentialDTO> dtos = list.stream().map(c -> {
            CredentialDTO d = new CredentialDTO();
            d.setId(c.getId());
            d.setTitle(c.getTitle());
            d.setUsername(c.getUsername());
            d.setCiphertext(c.getCiphertext());
            d.setIv(c.getIv());
            d.setSalt(c.getSalt());
            d.setNotes(c.getNotes());
            return d;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody CredentialDTO dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        Credential updated = new Credential();
        updated.setTitle(dto.getTitle());
        updated.setUsername(dto.getUsername());
        updated.setCiphertext(dto.getCiphertext());
        updated.setIv(dto.getIv());
        updated.setSalt(dto.getSalt());
        updated.setNotes(dto.getNotes());
        Credential saved = credentialService.updateCredential(user, id, updated);
        return ResponseEntity.ok(saved.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        User user = userService.findByEmail(userDetails.getUsername());
        credentialService.deleteCredential(user, id);
        return ResponseEntity.ok().build();
    }
}
