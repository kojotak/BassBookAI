class BassBookApp {
    constructor() {
        this.currentPage = 0;
        this.pageSize = 20;
        this.totalPages = 0;
        this.currentFilters = {};
        this.expandedRow = null;
        this.isAuthenticated = false;
        
        this.init();
    }

    init() {
        this.checkAuthStatus();
        this.loadEnums();
        this.loadArtists();
        this.setupEventListeners();
        this.loadSongs();
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
            if (response.ok) {
                const user = await response.json();
                this.isAuthenticated = true;
                this.showUserInfo(user);
            } else {
                this.showLoginOptions();
            }
        } catch (error) {
            console.log('Auth check failed:', error);
            this.showLoginOptions();
        }
    }

    showUserInfo(user) {
        document.getElementById('auth-section').style.display = 'none';
        document.getElementById('user-section').style.display = 'flex';
        document.getElementById('username').textContent = user.name;
        document.getElementById('tag-header').style.display = 'table-cell';
        
        this.currentFilters.userId = user.sub;
    }

    showOAuth2NotConfigured() {
        document.getElementById('auth-section').innerHTML = `
            <div class="alert alert-warning">
                <i class="fas fa-exclamation-triangle"></i> 
                OAuth2 is not configured. 
                <a href="/api/auth/status" class="alert-link">Check status</a>
            </div>
            <span class="navbar-text text-muted">Login not available</span>
        `;
        document.getElementById('user-section').style.display = 'none';
        document.getElementById('tag-header').style.display = 'none';
        
        // Disable form page link if it exists
        const formLink = document.querySelector('a[href="/form.html"]');
        if (formLink) {
            formLink.style.display = 'none';
        }
    }

    showLoginOptions() {
        document.getElementById('auth-section').innerHTML = `
            <a href="/oauth2/authorization/github" class="btn btn-outline-light">
                <i class="fab fa-github"></i> Login with GitHub
            </a>
        `;
        document.getElementById('user-section').style.display = 'none';
        document.getElementById('tag-header').style.display = 'none';
    }

    async loadEnums() {
        try {
            const [tuningsRes, techniquesRes, tagsRes] = await Promise.all([
                fetch('/api/enums/tunings'),
                fetch('/api/enums/techniques'),
                fetch('/api/enums/tags')
            ]);

            const tunings = await tuningsRes.json();
            const techniques = await techniquesRes.json();
            const tags = await tagsRes.json();

            this.populateSelect('tuning-select', tunings, 'All Tunings');
            this.populateSelect('technique-select', techniques, 'All Techniques');
            this.populateSelect('tag-select', tags, 'All Tags');
        } catch (error) {
            console.error('Error loading enums:', error);
        }
    }

    async loadArtists() {
        try {
            const response = await fetch('/api/artists');
            const artists = await response.json();
            
            const artistSelect = document.getElementById('artist-select');
            artists.forEach(artist => {
                const option = document.createElement('option');
                option.value = artist.id;
                option.textContent = artist.name;
                artistSelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error loading artists:', error);
        }
    }

    populateSelect(selectId, items, placeholder) {
        const select = document.getElementById(selectId);
        if (!select) return;
        
        items.forEach(item => {
            const option = document.createElement('option');
            option.value = item.value;
            option.textContent = item.displayName;
            select.appendChild(option);
        });
    }

    setupEventListeners() {
        document.getElementById('song-name').addEventListener('input', () => this.debounce(() => this.applyFilters(), 500)());
        
        document.getElementById('artist-select').addEventListener('change', () => this.applyFilters());
        document.getElementById('tuning-select').addEventListener('change', () => this.applyFilters());
        document.getElementById('technique-select').addEventListener('change', () => this.applyFilters());
        document.getElementById('tag-select')?.addEventListener('change', () => this.applyFilters());
        
        document.getElementById('clear-filters').addEventListener('click', () => this.clearFilters());
    }

    async applyFilters() {
        this.currentFilters.songName = document.getElementById('song-name').value;
        this.currentFilters.artistId = document.getElementById('artist-select').value || null;
        this.currentFilters.tuning = document.getElementById('tuning-select').value || null;
        this.currentFilters.technique = document.getElementById('technique-select').value || null;
        
        const tagSelect = document.getElementById('tag-select');
        if (tagSelect) {
            this.currentFilters.tag = tagSelect.value || null;
        }
        
        this.currentPage = 0;
        this.loadSongs();
    }

    clearFilters() {
        document.getElementById('song-name').value = '';
        document.getElementById('artist-select').value = '';
        document.getElementById('tuning-select').value = '';
        document.getElementById('technique-select').value = '';
        
        const tagSelect = document.getElementById('tag-select');
        if (tagSelect) {
            tagSelect.value = '';
        }
        
        this.currentFilters = { userId: this.currentFilters.userId };
        this.currentPage = 0;
        this.loadSongs();
    }

    async loadSongs() {
        try {
            this.showLoading();
            
            const params = new URLSearchParams({
                page: this.currentPage,
                size: this.pageSize,
                ...this.currentFilters
            });
            
            Object.keys(this.currentFilters).forEach(key => {
                if (this.currentFilters[key] === null || this.currentFilters[key] === '') {
                    params.delete(key);
                }
            });

            const response = await fetch(`/api/videos/songs?${params}`);
            const data = await response.json();

            this.totalPages = data.totalPages;
            this.renderSongs(data.content);
            this.renderPagination();
        } catch (error) {
            console.error('Error loading songs:', error);
            this.showError('Failed to load songs');
        }
    }

    renderSongs(songs) {
        const tbody = document.getElementById('songs-tbody');
        tbody.innerHTML = '';

        if (songs.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center">No songs found</td></tr>';
            return;
        }

        songs.forEach(song => {
            const row = this.createSongRow(song);
            tbody.appendChild(row);
        });
    }

    createSongRow(song) {
        const row = document.createElement('tr');
        row.className = 'expandable-row';
        row.dataset.songId = song.id;
        
        row.innerHTML = `
            <td><strong>${this.escapeHtml(song.name)}</strong></td>
            <td>${this.escapeHtml(song.artistName)}</td>
            <td>${song.channelCount}</td>
            <td>${this.escapeHtml(song.tunings || '')}</td>
            <td>${this.escapeHtml(song.techniques || '')}</td>
            <td class="tag-cell" style="${this.isAuthenticated ? '' : 'display: none;'}">
                ${this.createTagSelect(song)}
            </td>
            <td>
                <button class="btn btn-sm btn-outline-primary expand-btn">
                    <i class="fas fa-chevron-down"></i>
                </button>
            </td>
        `;

        row.addEventListener('click', (e) => {
            if (!e.target.closest('.tag-select') && !e.target.closest('.expand-btn')) {
                this.toggleRowExpansion(row);
            }
        });

        const expandBtn = row.querySelector('.expand-btn');
        expandBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.toggleRowExpansion(row);
        });

        const tagSelect = row.querySelector('.tag-select');
        if (tagSelect) {
            tagSelect.addEventListener('change', (e) => {
                e.stopPropagation();
                this.updateSongTag(song.id, e.target.value);
            });
        }

        return row;
    }

    createTagSelect(song) {
        const tags = ['PLAY', 'PRACTICE', 'SLOWDOWN', 'TODO', 'FORGET'];
        const options = tags.map(tag => 
            `<option value="${tag}" ${song.tag === tag ? 'selected' : ''}>${tag}</option>`
        ).join('');
        
        return `<select class="form-select form-select-sm tag-select">
            <option value="">No Tag</option>
            ${options}
        </select>`;
    }

    async toggleRowExpansion(row) {
        if (this.expandedRow && this.expandedRow !== row) {
            this.collapseRow(this.expandedRow);
        }

        if (row.classList.contains('expanded')) {
            this.collapseRow(row);
        } else {
            await this.expandRow(row);
        }
    }

    async expandRow(row) {
        const songId = row.dataset.songId;
        
        try {
            const response = await fetch(`/api/videos/songs/${songId}/videos`);
            const videos = await response.json();

            const expandedRow = document.createElement('tr');
            expandedRow.className = 'expanded-content';
            expandedRow.innerHTML = `
                <td colspan="7">
                    <div class="videos-container">
                        <h6>YouTube Videos</h6>
                        ${videos.map(video => this.createVideoHtml(video)).join('')}
                    </div>
                </td>
            `;

            row.insertAdjacentElement('afterend', expandedRow);
            row.classList.add('expanded');
            this.expandedRow = row;

            const expandBtn = row.querySelector('.expand-btn i');
            expandBtn.className = 'fas fa-chevron-up';
        } catch (error) {
            console.error('Error loading videos:', error);
            this.showError('Failed to load videos');
        }
    }

    collapseRow(row) {
        const expandedRow = row.nextElementSibling;
        if (expandedRow && expandedRow.classList.contains('expanded-content')) {
            expandedRow.remove();
        }
        
        row.classList.remove('expanded');
        this.expandedRow = null;

        const expandBtn = row.querySelector('.expand-btn i');
        expandBtn.className = 'fas fa-chevron-down';
    }

    createVideoHtml(video) {
        const youtubeUrl = `https://youtube.com/watch?v=${video.youtubeVideoId}`;
        const tuningBadge = video.tuning ? 
            `<span class="badge bg-secondary">${video.tuning}</span>` : '';
        
        const techniqueBadges = video.techniques && video.techniques.length > 0 ?
            video.techniques.map(t => `<span class="badge bg-info">${t}</span>`).join(' ') : '';

        return `
            <div class="video-row">
                <div class="channel-info">
                    <div>
                        <a href="${youtubeUrl}" target="_blank" class="btn btn-sm btn-link">
                            <i class="fab fa-youtube"></i> ${this.escapeHtml(video.channelName)}
                        </a>
                        ${tuningBadge} ${techniqueBadges}
                        ${video.bpm ? `<span class="badge bg-warning">${video.bpm} BPM</span>` : ''}
                    </div>
                </div>
            </div>
        `;
    }

    async updateSongTag(songId, tagValue) {
        try {
            const video = await this.findFirstVideoForSong(songId);
            if (video) {
                await fetch(`/api/videos/${video.id}/tag`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ tag: tagValue })
                });
            }
        } catch (error) {
            console.error('Error updating tag:', error);
            this.showError('Failed to update tag');
        }
    }

    async findFirstVideoForSong(songId) {
        try {
            const response = await fetch(`/api/videos/songs/${songId}/videos`);
            const videos = await response.json();
            return videos.find(v => this.isAuthenticated && v.tag !== null) || videos[0];
        } catch (error) {
            console.error('Error finding video:', error);
            return null;
        }
    }

    renderPagination() {
        const pagination = document.querySelector('#pagination ul');
        pagination.innerHTML = '';

        if (this.totalPages <= 1) return;

        const prevLi = document.createElement('li');
        prevLi.className = `page-item ${this.currentPage === 0 ? 'disabled' : ''}`;
        prevLi.innerHTML = `<a class="page-link" href="#" ${this.currentPage === 0 ? 'tabindex="-1"' : ''}>Previous</a>`;
        prevLi.addEventListener('click', (e) => {
            e.preventDefault();
            if (this.currentPage > 0) {
                this.currentPage--;
                this.loadSongs();
            }
        });
        pagination.appendChild(prevLi);

        for (let i = 0; i < this.totalPages; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === this.currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i + 1}</a>`;
            li.addEventListener('click', (e) => {
                e.preventDefault();
                this.currentPage = i;
                this.loadSongs();
            });
            pagination.appendChild(li);
        }

        const nextLi = document.createElement('li');
        nextLi.className = `page-item ${this.currentPage === this.totalPages - 1 ? 'disabled' : ''}`;
        nextLi.innerHTML = `<a class="page-link" href="#" ${this.currentPage === this.totalPages - 1 ? 'tabindex="-1"' : ''}>Next</a>`;
        nextLi.addEventListener('click', (e) => {
            e.preventDefault();
            if (this.currentPage < this.totalPages - 1) {
                this.currentPage++;
                this.loadSongs();
            }
        });
        pagination.appendChild(nextLi);
    }

    showLoading() {
        const tbody = document.getElementById('songs-tbody');
        tbody.innerHTML = '<tr><td colspan="7" class="loading"><i class="fas fa-spinner fa-spin"></i> Loading...</td></tr>';
    }

    showError(message) {
        const tbody = document.getElementById('songs-tbody');
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-danger">${message}</td></tr>`;
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new BassBookApp();
});