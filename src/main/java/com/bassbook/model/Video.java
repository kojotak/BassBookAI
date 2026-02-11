package com.bassbook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "youtube_video_id", nullable = false, unique = true)
    private String youtubeVideoId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tuning")
    private Tuning tuning;
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "video_techniques", joinColumns = @JoinColumn(name = "video_id"))
    @Column(name = "technique")
    private java.util.List<Technique> techniques;
    
    @Column(name = "bpm")
    private Integer bpm;
    
    @Enumerated(EnumType.STRING)
    private Tag tag;
    
    @Column(name = "user_id")
    private String userId;
}