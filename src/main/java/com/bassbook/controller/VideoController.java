package com.bassbook.controller;

import com.bassbook.dto.SongDto;
import com.bassbook.model.*;
import com.bassbook.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/songs")
    public Page<SongDto> getSongs(
            @RequestParam(required = false) String songName,
            @RequestParam(required = false) Long artistId,
            @RequestParam(required = false) Tuning tuning,
            @RequestParam(required = false) Technique technique,
            @RequestParam(required = false) Tag tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal OidcUser user) {
        
        String userId = user != null ? user.getSubject() : null;
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Song> songs = videoService.searchSongs(
            songName, artistId, tuning, technique, tag, userId, pageable);
        
        List<SongDto> songDtos = songs.getContent().stream()
            .map(song -> convertToSongDto(song, userId))
            .collect(Collectors.toList());
        
        return new PageImpl<>(songDtos, pageable, songs.getTotalElements());
    }

    @GetMapping("/songs/{songId}/videos")
    public List<com.bassbook.dto.VideoDto> getVideosForSong(
            @PathVariable Long songId,
            @AuthenticationPrincipal OidcUser user) {
        
        String userId = user != null ? user.getSubject() : null;
        return videoService.getVideosForSong(songId, userId).stream()
            .map(this::convertToVideoDto)
            .collect(Collectors.toList());
    }

    @PostMapping
    public com.bassbook.dto.VideoDto createVideo(@RequestBody com.bassbook.dto.VideoDto videoDto,
                                                 @AuthenticationPrincipal OidcUser user) {
        String userId = user != null ? user.getSubject() : null;
        
        if (videoDto.getYoutubeUrl() != null) {
            String videoId = videoService.extractVideoId(videoDto.getYoutubeUrl());
            videoDto.setYoutubeVideoId(videoId);
        }
        
        Video video = videoService.saveVideo(videoDto, userId);
        return convertToVideoDto(video);
    }

    @PutMapping("/{videoId}/tag")
    public ResponseEntity<Void> updateVideoTag(@PathVariable Long videoId,
                                               @RequestBody Map<String, String> request,
                                               @AuthenticationPrincipal OidcUser user) {
        String userId = user != null ? user.getSubject() : null;
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        
        Tag tag = Tag.valueOf(request.get("tag"));
        videoService.updateVideoTag(videoId, tag, userId);
        return ResponseEntity.ok().build();
    }

    private SongDto convertToSongDto(Song song, String userId) {
        SongDto dto = new SongDto();
        dto.setId(song.getId());
        dto.setName(song.getName());
        dto.setArtistName(song.getArtist().getName());
        
        List<Video> videos = videoService.getVideosForSong(song.getId(), userId);
        dto.setChannelCount(videos.size());
        
        String tunings = videos.stream()
            .filter(v -> v.getTuning() != null)
            .map(v -> v.getTuning().getDisplayName())
            .distinct()
            .collect(Collectors.joining(", "));
        dto.setTunings(tunings);
        
        String techniques = videos.stream()
            .filter(v -> v.getTechniques() != null && !v.getTechniques().isEmpty())
            .flatMap(v -> v.getTechniques().stream())
            .map(Technique::getDisplayName)
            .distinct()
            .collect(Collectors.joining(", "));
        dto.setTechniques(techniques);
        
        if (userId != null) {
            Tag userTag = videos.stream()
                .filter(v -> userId.equals(v.getUserId()) && v.getTag() != null)
                .map(Video::getTag)
                .findFirst()
                .orElse(null);
            dto.setTag(userTag);
        }
        
        return dto;
    }

    private com.bassbook.dto.VideoDto convertToVideoDto(Video video) {
        com.bassbook.dto.VideoDto dto = new com.bassbook.dto.VideoDto();
        dto.setYoutubeVideoId(video.getYoutubeVideoId());
        dto.setChannelName(video.getChannel().getDisplayName());
        dto.setTuning(video.getTuning());
        dto.setTechniques(video.getTechniques());
        dto.setBpm(video.getBpm());
        dto.setTag(video.getTag());
        return dto;
    }
}