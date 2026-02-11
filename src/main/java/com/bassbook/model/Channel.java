package com.bassbook.model;

import jakarta.persistence.*;

@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String displayName;
    
    @Column(unique = true, nullable = false)
    private String youtubeChannelId;

    public Channel() {}

    public Channel(Long id, String displayName, String youtubeChannelId) {
        this.id = id;
        this.displayName = displayName;
        this.youtubeChannelId = youtubeChannelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getYoutubeChannelId() {
        return youtubeChannelId;
    }

    public void setYoutubeChannelId(String youtubeChannelId) {
        this.youtubeChannelId = youtubeChannelId;
    }
}