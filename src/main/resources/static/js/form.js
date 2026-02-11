class VideoFormApp {
    constructor() {
        this.parsedVideoData = null;
        this.currentArtist = null;
        this.currentSong = null;
        this.enums = {};
        
        this.init();
    }

    init() {
        this.checkAuthStatus();
        this.loadEnums();
        this.loadArtists();
        this.setupEventListeners();
    }

    async checkAuthStatus() {
        try {
            // First check if OAuth2 is configured
            const authConfigResponse = await fetch('/api/auth/config');
            const authConfig = await authConfigResponse.json();
            
            if (!authConfig.oauth2Configured) {
                this.showOAuth2NotConfigured();
                return;
            }

            // OAuth2 is configured, check user status
            const response = await fetch('/api/user/info');
            if (!response.ok) {
                this.showLoginRequired();
                return;
            }
            const user = await response.json();
            document.getElementById('username').textContent = user.name;
            document.getElementById('user-section').style.display = 'flex';
        } catch (error) {
            console.log('Auth check failed:', error);
            this.showLoginRequired();
        }
    }

    showOAuth2NotConfigured() {
        document.getElementById('user-section').innerHTML = `
            <span class="navbar-text text-warning">
                <i class="fas fa-exclamation-triangle"></i> 
                OAuth2 not configured - Login unavailable
            </span>
        `;
        
        // Disable form functionality
        this.disableForm();
    }

    showLoginRequired() {
        document.getElementById('user-section').innerHTML = `
            <span class="navbar-text">
                <i class="fas fa-lock"></i> 
                <a href="/" style="color: white;">Login required</a>
            </span>
        `;
        
        // Disable form functionality
        this.disableForm();
    }

    disableForm() {
        const form = document.getElementById('video-form');
        if (form) {
            form.innerHTML = `
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i>
                    <strong>Access Denied:</strong> This feature requires authentication.
                    <br>OAuth2 is not properly configured.
                    <br>Please check the <a href="/api/auth/status" target="_blank">configuration status</a>
                    or consult the setup guide.
                </div>
                <div class="text-center mt-3">
                    <a href="/" class="btn btn-primary">
                        <i class="fas fa-home"></i> Back to Home
                    </a>
                </div>
            `;
        }
    }

    async loadEnums() {
        try {
            const [tuningsRes, techniquesRes, tagsRes] = await Promise.all([
                fetch('/api/enums/tunings'),
                fetch('/api/enums/techniques'),
                fetch('/api/enums/tags')
            ]);

            this.enums.tunings = await tuningsRes.json();
            this.enums.techniques = await techniquesRes.json();
            this.enums.tags = await tagsRes.json();

            this.populateEnumSelects();
        } catch (error) {
            console.error('Error loading enums:', error);
            this.showError('Failed to load form data');
        }
    }

    populateEnumSelects() {
        this.populateSelect('tuning-select', this.enums.tunings, 'Select tuning...');
        this.populateMultiSelect('technique-select', this.enums.techniques);
        this.populateSelect('tag-select', this.enums.tags, 'No tag');
    }

    populateSelect(selectId, items, placeholder) {
        const select = document.getElementById(selectId);
        if (!select) return;
        
        select.innerHTML = `<option value="">${placeholder}</option>`;
        items.forEach(item => {
            const option = document.createElement('option');
            option.value = item.value;
            option.textContent = item.displayName;
            select.appendChild(option);
        });
    }

    populateMultiSelect(selectId, items) {
        const select = document.getElementById(selectId);
        if (!select) return;
        
        items.forEach(item => {
            const option = document.createElement('option');
            option.value = item.value;
            option.textContent = item.displayName;
            select.appendChild(option);
        });
    }

    async loadArtists() {
        try {
            const response = await fetch('/api/artists');
            const artists = await response.json();
            
            const select = document.getElementById('artist-select');
            artists.forEach(artist => {
                const option = document.createElement('option');
                option.value = artist.id;
                option.textContent = artist.name;
                select.appendChild(option);
            });
        } catch (error) {
            console.error('Error loading artists:', error);
            this.showError('Failed to load artists');
        }
    }

    setupEventListeners() {
        document.getElementById('parse-btn').addEventListener('click', () => this.parseYouTubeUrl());
        document.getElementById('youtube-url').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                this.parseYouTubeUrl();
            }
        });

        document.getElementById('artist-select').addEventListener('change', () => this.onArtistChange());
        document.getElementById('add-artist-btn').addEventListener('click', () => this.showAddArtistModal());
        document.getElementById('add-song-btn').addEventListener('click', () => this.showAddSongModal());

        document.getElementById('save-artist-btn').addEventListener('click', () => this.saveArtist());
        document.getElementById('save-song-btn').addEventListener('click', () => this.saveSong());

        document.getElementById('video-form').addEventListener('submit', (e) => this.saveVideo(e));
    }

    async parseYouTubeUrl() {
        const url = document.getElementById('youtube-url').value.trim();
        if (!url) {
            this.showError('Please enter a YouTube URL');
            return;
        }

        try {
            this.showLoading('parse-btn', 'Parsing...');
            
            const response = await fetch('/api/youtube/parse', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ url })
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.error || 'Failed to parse URL');
            }

            const videoInfo = await response.json();
            this.setParsedVideoData(videoInfo, url);
            
        } catch (error) {
            console.error('Error parsing URL:', error);
            this.showError('Failed to parse YouTube URL: ' + error.message);
        } finally {
            this.hideLoading('parse-btn', '<i class="fas fa-search"></i> Parse');
        }
    }

    extractVideoId(url) {
        const patterns = [
            /(?:youtube\.com\/watch\?v=|youtu\.be\/|youtube\.com\/embed\/)([a-zA-Z0-9_-]{11})/,
            /youtube\.com\/.*[?&]v=([a-zA-Z0-9_-]{11})/
        ];
        
        for (const pattern of patterns) {
            const match = url.match(pattern);
            if (match) {
                return match[1];
            }
        }
        return null;
    }

    setParsedVideoData(videoInfo, url) {
        this.parsedVideoData = {
            videoId: videoInfo.videoId,
            url: url,
            title: videoInfo.title,
            channelName: videoInfo.channelName,
            channelId: videoInfo.channelId
        };

        this.displayParsedInfo();
        this.enableFormFields();
    }

    displayParsedInfo() {
        const parsedInfo = document.getElementById('parsed-info');
        const parsedDetails = document.getElementById('parsed-details');
        
        parsedDetails.innerHTML = `
            <div><strong>Video ID:</strong> ${this.parsedVideoData.videoId}</div>
            <div><strong>Title:</strong> ${this.escapeHtml(this.parsedVideoData.title)}</div>
            <div><strong>Channel:</strong> ${this.escapeHtml(this.parsedVideoData.channelName)}</div>
        `;
        
        parsedInfo.style.display = 'block';
        document.getElementById('channel-display').value = this.parsedVideoData.channelName;
    }

    enableFormFields() {
        document.getElementById('artist-select').disabled = false;
        document.getElementById('save-btn').disabled = false;
    }

    onArtistChange() {
        const artistId = document.getElementById('artist-select').value;
        const songSelect = document.getElementById('song-select');
        const addSongBtn = document.getElementById('add-song-btn');

        if (artistId) {
            this.loadSongsForArtist(artistId);
            songSelect.disabled = false;
            addSongBtn.disabled = false;
        } else {
            songSelect.innerHTML = '<option value="">Select an artist first...</option>';
            songSelect.disabled = true;
            addSongBtn.disabled = true;
        }
    }

    async loadSongsForArtist(artistId) {
        try {
            const response = await fetch(`/api/artists/${artistId}/songs`);
            const songs = await response.json();
            
            const select = document.getElementById('song-select');
            select.innerHTML = '<option value="">Select a song...</option>';
            
            songs.forEach(song => {
                const option = document.createElement('option');
                option.value = song.id;
                option.textContent = song.name;
                select.appendChild(option);
            });

            // If we have parsed data, try to match it
            if (this.parsedVideoData) {
                const matchedSong = songs.find(song => 
                    song.name.toLowerCase().includes(this.parsedVideoData.title.toLowerCase())
                );
                if (matchedSong) {
                    select.value = matchedSong.id;
                }
            }
        } catch (error) {
            console.error('Error loading songs:', error);
            this.showError('Failed to load songs');
        }
    }

    showAddArtistModal() {
        const modal = new bootstrap.Modal(document.getElementById('artistModal'));
        document.getElementById('new-artist-name').value = '';
        modal.show();
    }

    showAddSongModal() {
        const artistId = document.getElementById('artist-select').value;
        if (!artistId) {
            this.showError('Please select an artist first');
            return;
        }

        const modal = new bootstrap.Modal(document.getElementById('songModal'));
        document.getElementById('new-song-name').value = '';
        
        // Pre-fill with parsed title if available
        if (this.parsedVideoData) {
            document.getElementById('new-song-name').value = this.parsedVideoData.title;
        }
        
        modal.show();
    }

    async saveArtist() {
        const name = document.getElementById('new-artist-name').value.trim();
        if (!name) {
            this.showError('Artist name is required');
            return;
        }

        try {
            this.showLoading('save-artist-btn', 'Saving...');
            
            const response = await fetch('/api/artists', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name })
            });

            if (!response.ok) {
                throw new Error('Failed to save artist');
            }

            const artist = await response.json();
            
            // Add to select and select it
            const select = document.getElementById('artist-select');
            const option = document.createElement('option');
            option.value = artist.id;
            option.textContent = artist.name;
            select.appendChild(option);
            select.value = artist.id;

            // Close modal and trigger artist change
            bootstrap.Modal.getInstance(document.getElementById('artistModal')).hide();
            this.onArtistChange();
            
        } catch (error) {
            console.error('Error saving artist:', error);
            this.showError('Failed to save artist');
        } finally {
            this.hideLoading('save-artist-btn', 'Save Artist');
        }
    }

    async saveSong() {
        const name = document.getElementById('new-song-name').value.trim();
        const artistId = document.getElementById('artist-select').value;
        
        if (!name) {
            this.showError('Song name is required');
            return;
        }

        try {
            this.showLoading('save-song-btn', 'Saving...');
            
            const response = await fetch(`/api/artists/${artistId}/songs`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name })
            });

            if (!response.ok) {
                throw new Error('Failed to save song');
            }

            const song = await response.json();
            
            // Add to select and select it
            const select = document.getElementById('song-select');
            const option = document.createElement('option');
            option.value = song.id;
            option.textContent = song.name;
            select.appendChild(option);
            select.value = song.id;

            // Close modal
            bootstrap.Modal.getInstance(document.getElementById('songModal')).hide();
            
        } catch (error) {
            console.error('Error saving song:', error);
            this.showError('Failed to save song');
        } finally {
            this.hideLoading('save-song-btn', 'Save Song');
        }
    }

    async saveVideo(event) {
        event.preventDefault();

        if (!this.parsedVideoData) {
            this.showError('Please parse a YouTube URL first');
            return;
        }

        const artistId = document.getElementById('artist-select').value;
        const songId = document.getElementById('song-select').value;
        const tuning = document.getElementById('tuning-select').value;
        const techniques = Array.from(document.getElementById('technique-select').selectedOptions)
            .map(option => option.value);
        const bpm = document.getElementById('bpm-input').value;
        const tag = document.getElementById('tag-select').value;

        if (!artistId || !songId) {
            this.showError('Please select an artist and song');
            return;
        }

        try {
            this.showLoading('save-btn', 'Saving...');

            const videoData = {
                youtubeUrl: this.parsedVideoData.url,
                youtubeVideoId: this.parsedVideoData.videoId,
                channelName: this.parsedVideoData.channelName,
                youtubeChannelId: this.parsedVideoData.channelId,
                artistId: parseInt(artistId),
                songId: parseInt(songId),
                tuning: tuning || null,
                techniques: techniques,
                bpm: bpm ? parseInt(bpm) : null,
                tag: tag || null
            };

            const response = await fetch('/api/videos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(videoData)
            });

            if (!response.ok) {
                throw new Error('Failed to save video');
            }

            // Redirect to home page
            window.location.href = '/';
            
        } catch (error) {
            console.error('Error saving video:', error);
            this.showError('Failed to save video');
        } finally {
            this.hideLoading('save-btn', '<i class="fas fa-save"></i> Save Video');
        }
    }

    showLoading(buttonId, text) {
        const button = document.getElementById(buttonId);
        button.disabled = true;
        button.innerHTML = `<i class="fas fa-spinner fa-spin"></i> ${text}`;
    }

    hideLoading(buttonId, originalHtml) {
        const button = document.getElementById(buttonId);
        button.disabled = false;
        button.innerHTML = originalHtml;
    }

    showError(message) {
        const alertHtml = `
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i> ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
        
        // Insert after the card header
        const cardHeader = document.querySelector('.card-header');
        cardHeader.insertAdjacentHTML('afterend', alertHtml);
        
        // Auto dismiss after 5 seconds
        setTimeout(() => {
            const alert = cardHeader.nextElementSibling;
            if (alert && alert.classList.contains('alert')) {
                alert.remove();
            }
        }, 5000);
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new VideoFormApp();
});