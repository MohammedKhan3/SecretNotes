package com.FK.Notes.Secrets.Models;


import lombok.Data;
import jakarta.persistence.*;
@Entity
@Data
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;
    private String ownerUsername;

/*
Lob-> large object type.
 */
}
