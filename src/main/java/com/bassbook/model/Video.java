package com.bassbook.model;

import jakarta.persistence.*;

@Entity
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

    public Video() {}

    public Video(Long id, String youtubeVideoId, Song song, Channel channel, Tuning tuning, 
                java.util.List<Technique> techniques, Integer bpm, Tag tag, String userId) {
        this.id = id;
        this.youtubeVideoId = youtubeVideoId;
        this.song = song;
        this.channel = channel;
        this.tuning = tuning;
        this.techniques = techniques;
        this.bpm = bpm;
        this.tag = tag;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Tuning getTuning() {
        return tuning;
    }

    public void setTuning(Tuning tuning) {
        this.tuning = tuning;
    }

    public java.util.List<Technique> getTechniques() {
        return techniques;
    }

    public void setTechniques(java.util.List<Technique> techniques) {
        this.techniques = techniques;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}