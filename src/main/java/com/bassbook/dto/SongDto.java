package com.bassbook.dto;

import com.bassbook.model.Tag;
import java.util.List;

public class SongDto {
    private Long id;
    private String name;
    private String artistName;
    private int channelCount;
    private String tunings;
    private String techniques;
    private Tag tag;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public int getChannelCount() { return channelCount; }
    public void setChannelCount(int channelCount) { this.channelCount = channelCount; }

    public String getTunings() { return tunings; }
    public void setTunings(String tunings) { this.tunings = tunings; }

    public String getTechniques() { return techniques; }
    public void setTechniques(String techniques) { this.techniques = techniques; }

    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }
}