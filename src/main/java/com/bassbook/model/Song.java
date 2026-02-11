package com.bassbook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "songs")
@EqualsAndHashCode(exclude = "artist")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;
}