package com.bassbook.service;

import com.bassbook.dto.VideoDto;
import com.bassbook.model.*;
import com.bassbook.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;
    
    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private ArtistRepository artistRepository;
    
    @Autowired
    private SongRepository songRepository;

    public Page<Song> searchSongs(String songName, Long artistId, Tuning tuning, 
                                Technique technique, Tag tag, String userId, Pageable pageable) {
        return videoRepository.findDistinctSongsByFilters(
            songName, artistId, tuning, technique, tag, userId, pageable);
    }

    public List<Video> getVideosForSong(Long songId, String userId) {
        return videoRepository.findBySongIdAndUserId(songId, userId);
    }

    public Video saveVideo(VideoDto videoDto, String userId) {
        Video video = new Video();
        
        video.setYoutubeVideoId(videoDto.getYoutubeVideoId());
        
        Channel channel = getOrCreateChannel(videoDto);
        video.setChannel(channel);
        
        Song song = getOrCreateSong(videoDto);
        video.setSong(song);
        
        video.setTuning(videoDto.getTuning());
        video.setTechniques(videoDto.getTechniques());
        video.setBpm(videoDto.getBpm());
        
        if (userId != null) {
            video.setTag(videoDto.getTag());
            video.setUserId(userId);
        }
        
        return videoRepository.save(video);
    }

    public Video updateVideoTag(Long videoId, Tag tag, String userId) {
        Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> new RuntimeException("Video not found"));
        
        if (userId != null && (video.getUserId() == null || video.getUserId().equals(userId))) {
            video.setTag(tag);
            video.setUserId(userId);
            return videoRepository.save(video);
        }
        throw new RuntimeException("Unauthorized to update video tag");
    }

    private Channel getOrCreateChannel(VideoDto videoDto) {
        if (videoDto.getYoutubeChannelId() != null) {
            return channelRepository.findByYoutubeChannelId(videoDto.getYoutubeChannelId())
                .orElseGet(() -> {
                    Channel channel = new Channel();
                    channel.setDisplayName(videoDto.getChannelName());
                    channel.setYoutubeChannelId(videoDto.getYoutubeChannelId());
                    return channelRepository.save(channel);
                });
        }
        throw new RuntimeException("YouTube channel ID is required");
    }

    private Song getOrCreateSong(VideoDto videoDto) {
        if (videoDto.getSongId() != null) {
            return songRepository.findById(videoDto.getSongId())
                .orElseThrow(() -> new RuntimeException("Song not found"));
        }
        
        if (videoDto.getNewSongName() != null && videoDto.getArtistId() != null) {
            return songRepository.findByNameAndArtistId(videoDto.getNewSongName(), videoDto.getArtistId())
                .orElseGet(() -> {
                    Song song = new Song();
                    song.setName(videoDto.getNewSongName());
                    Artist artist = artistRepository.findById(videoDto.getArtistId())
                        .orElseThrow(() -> new RuntimeException("Artist not found"));
                    song.setArtist(artist);
                    return songRepository.save(song);
                });
        }
        
        throw new RuntimeException("Either existing song ID or new song name with artist ID is required");
    }

    public String extractVideoId(String youtubeUrl) {
        String pattern = "(?:youtube\\.com/watch\\?v=|youtu\\.be/|youtube\\.com/embed/)([a-zA-Z0-9_-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youtubeUrl);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Invalid YouTube URL");
    }
}