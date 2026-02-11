package com.bassbook.dto;

import com.bassbook.model.Tuning;
import com.bassbook.model.Technique;
import com.bassbook.model.Tag;
import java.util.List;

public class VideoDto {
    private String youtubeUrl;
    private String youtubeVideoId;
    private String channelName;
    private String youtubeChannelId;
    private Long artistId;
    private Long songId;
    private String newArtistName;
    private String newSongName;
    private Tuning tuning;
    private List<Technique> techniques;
    private Integer bpm;
    private Tag tag;

    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }

    public String getYoutubeVideoId() { return youtubeVideoId; }
    public void setYoutubeVideoId(String youtubeVideoId) { this.youtubeVideoId = youtubeVideoId; }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public String getYoutubeChannelId() { return youtubeChannelId; }
    public void setYoutubeChannelId(String youtubeChannelId) { this.youtubeChannelId = youtubeChannelId; }

    public Long getArtistId() { return artistId; }
    public void setArtistId(Long artistId) { this.artistId = artistId; }

    public Long getSongId() { return songId; }
    public void setSongId(Long songId) { this.songId = songId; }

    public String getNewArtistName() { return newArtistName; }
    public void setNewArtistName(String newArtistName) { this.newArtistName = newArtistName; }

    public String getNewSongName() { return newSongName; }
    public void setNewSongName(String newSongName) { this.newSongName = newSongName; }

    public Tuning getTuning() { return tuning; }
    public void setTuning(Tuning tuning) { this.tuning = tuning; }

    public List<Technique> getTechniques() { return techniques; }
    public void setTechniques(List<Technique> techniques) { this.techniques = techniques; }

    public Integer getBpm() { return bpm; }
    public void setBpm(Integer bpm) { this.bpm = bpm; }

    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }
}