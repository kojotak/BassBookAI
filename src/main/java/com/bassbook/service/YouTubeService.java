package com.bassbook.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class YouTubeService {
    
    @Value("${youtube.api.key:}")
    private String youtubeApiKey;
    
    private static final String YOUTUBE_API_BASE = "https://www.googleapis.com/youtube/v3";
    private static final String VIDEO_URL_PATTERN = "(?:youtube\\.com/watch\\?v=|youtu\\.be/|youtube\\.com/embed/)([a-zA-Z0-9_-]{11})";
    private static final String CHANNEL_URL_PATTERN = "youtube\\.com/channel/([a-zA-Z0-9_-]{24})|youtube\\.com/c/([^/?]+)|youtube\\.com/([^/?]+)";
    
    private final RestTemplate restTemplate = new RestTemplate();

    public String extractVideoId(String url) {
        Pattern pattern = Pattern.compile(VIDEO_URL_PATTERN);
        Matcher matcher = pattern.matcher(url);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Invalid YouTube URL format");
    }

    public YouTubeVideoInfo getVideoInfo(String videoId) {
        if (youtubeApiKey == null || youtubeApiKey.isEmpty()) {
            return createMockVideoInfo(videoId);
        }
        
        try {
            String url = YOUTUBE_API_BASE + "/videos?id=" + videoId + 
                        "&part=snippet,contentDetails&key=" + youtubeApiKey;
            
            YouTubeApiResponse response = restTemplate.getForObject(url, YouTubeApiResponse.class);
            
            if (response != null && response.items != null && !response.items.isEmpty()) {
                YouTubeApiResponse.VideoItem item = response.items.get(0);
                return convertToVideoInfo(item);
            }
        } catch (Exception e) {
            // Fallback to mock data if API fails
        }
        
        return createMockVideoInfo(videoId);
    }

    public YouTubeChannelInfo getChannelInfo(String channelId) {
        if (youtubeApiKey == null || youtubeApiKey.isEmpty()) {
            return createMockChannelInfo(channelId);
        }
        
        try {
            String url = YOUTUBE_API_BASE + "/channels?id=" + channelId + 
                        "&part=snippet&key=" + youtubeApiKey;
            
            YouTubeChannelApiResponse response = restTemplate.getForObject(url, YouTubeChannelApiResponse.class);
            
            if (response != null && response.items != null && !response.items.isEmpty()) {
                YouTubeChannelApiResponse.ChannelItem item = response.items.get(0);
                return convertToChannelInfo(item);
            }
        } catch (Exception e) {
            // Fallback to mock data if API fails
        }
        
        return createMockChannelInfo(channelId);
    }

    private YouTubeVideoInfo createMockVideoInfo(String videoId) {
        YouTubeVideoInfo info = new YouTubeVideoInfo();
        info.videoId = videoId;
        info.title = "Sample Video Title";
        info.channelName = "Sample Channel";
        info.channelId = "sample_channel_" + System.currentTimeMillis();
        return info;
    }

    private YouTubeChannelInfo createMockChannelInfo(String channelId) {
        YouTubeChannelInfo info = new YouTubeChannelInfo();
        info.channelId = channelId;
        info.title = "Sample Channel";
        return info;
    }

    private YouTubeVideoInfo convertToVideoInfo(YouTubeApiResponse.VideoItem item) {
        YouTubeVideoInfo info = new YouTubeVideoInfo();
        info.videoId = item.id;
        info.title = item.snippet.title;
        info.channelName = item.snippet.channelTitle;
        info.channelId = item.snippet.channelId;
        return info;
    }

    private YouTubeChannelInfo convertToChannelInfo(YouTubeChannelApiResponse.ChannelItem item) {
        YouTubeChannelInfo info = new YouTubeChannelInfo();
        info.channelId = item.id;
        info.title = item.snippet.title;
        return info;
    }

    // Inner classes for API response mapping
    public static class YouTubeVideoInfo {
        public String videoId;
        public String title;
        public String channelName;
        public String channelId;
    }

    public static class YouTubeChannelInfo {
        public String channelId;
        public String title;
    }

    // Response class for video API
    private static class YouTubeApiResponse {
        public java.util.List<VideoItem> items;
        
        public static class VideoItem {
            public String id;
            public VideoSnippet snippet;
        }
        
        public static class VideoSnippet {
            public String title;
            public String channelTitle;
            @JsonProperty("channelId")
            public String channelId;
        }
    }

    // Response class for channel API
    private static class YouTubeChannelApiResponse {
        public java.util.List<ChannelItem> items;
        
        public static class ChannelItem {
            public String id;
            public ChannelSnippet snippet;
        }
        
        public static class ChannelSnippet {
            public String title;
        }
    }
}