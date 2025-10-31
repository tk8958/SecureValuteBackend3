package com.example.vault.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CredentialDTO {
    private Long id;
    private String title;
    private String username;
    private String ciphertext;
    private String iv;
    private String salt;
    private String notes;
}
