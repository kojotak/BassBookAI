# GitHub OAuth2 Setup Guide - Bass Book AI

## 🎯 Overview

Bass Book AI now supports **GitHub OAuth2 only**. Google integration has been removed to simplify setup and configuration.

## 📋 GitHub OAuth2 Setup

### Step 1: Create GitHub OAuth App
1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. Click "OAuth Apps" → "New OAuth App"
3. Configure the application:
   - **Application name**: Bass Book AI
   - **Homepage URL**: `http://localhost:8080`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
   - **Application description**: Bass guitar play-along video manager
4. Click "Register application"

### Step 2: Get Credentials
From your OAuth application page, copy:
- **Client ID**
- **Client Secret**

## ⚙️ Configuration Methods

### Method 1: Environment Variables

#### Windows:
```cmd
set GITHUB_CLIENT_ID=your_github_client_id_here
set GITHUB_CLIENT_SECRET=your_github_client_secret_here
```

#### Linux/Mac:
```bash
export GITHUB_CLIENT_ID=your_github_client_id_here
export GITHUB_CLIENT_SECRET=your_github_client_secret_here
```

### Method 2: Credentials File

Create `oauth2-credentials.properties`:
```properties
# GitHub OAuth2
GITHUB_CLIENT_ID=your_github_client_id_here
GITHUB_CLIENT_SECRET=your_github_client_secret_here
```

## 🚀 Running the Application

### Option 1: Read-Only Mode (No Setup)
```bash
mvn spring-boot:run
```

### Option 2: Full Login Mode
```bash
# Using environment variables
mvn spring-boot:run -Dspring.profiles.active=oauth2

# Using setup script
setup-oauth2.sh oauth2  # Linux/Mac
setup-oauth2.bat oauth2  # Windows
```

### Option 3: Using Credentials File
```bash
# Load from oauth2-credentials.properties
source oauth2-credentials.properties
mvn spring-boot:run -Dspring.profiles.active=oauth2
```

## 🎯 Features by Mode

### Read-Only Mode (Default):
✅ **Available**:
- Browse all bass play-along videos
- Search and filter by song, artist, tuning, technique
- View video details and channels
- Pagination and sorting

❌ **Not Available**:
- Add new videos
- Set personal tags (PLAY, PRACTICE, SLOWDOWN, TODO, FORGET)
- User-specific data persistence

### Full Mode (OAuth2 Enabled):
✅ **All Features**:
- All read-only features
- Add and manage videos
- Personal tagging system
- User authentication via GitHub
- User-specific song tagging

## 🛡️ Security Notes

### For Development:
- ✅ Use `http://localhost:8080` for all URLs
- ✅ Add `http://localhost:8080/login/oauth2/code/github` as callback
- ✅ Keep client secrets out of version control
- ✅ Use HTTPS in production

### For Production:
- 🔄 Change `localhost:8080` to your actual domain
- 🔒 Use HTTPS URLs for production
- 📝 Restrict OAuth scopes to minimum required
- 🗑️ Regularly rotate client secrets

## 🎉 Quick Start

### For Immediate Testing:
```bash
mvn spring-boot:run
```
Application starts in read-only mode immediately.

### For Full Features:
```bash
# Create GitHub OAuth App
export GITHUB_CLIENT_ID=your_client_id
export GITHUB_CLIENT_SECRET=your_client_secret

# Run with OAuth2
mvn spring-boot:run -Dspring.profiles.active=oauth2
```

## 🔧 Troubleshooting

### Common Issues:

#### 1. "redirect_uri_mismatch" Error
**Solution**: Ensure redirect URI exactly matches:
- `http://localhost:8080/login/oauth2/code/github`

#### 2. "invalid_client" Error  
**Solution**: Verify client ID and secret are correct and match your GitHub OAuth App

#### 3. Profile Not Activating
**Solution**: Ensure you're using:
```bash
mvn spring-boot:run -Dspring.profiles.active=oauth2
```

## 📋 Configuration Checklist

- [ ] GitHub OAuth2 app created
- [ ] Homepage URL: `http://localhost:8080`
- [ ] Callback URL: `http://localhost:8080/login/oauth2/code/github`
- [ ] Client ID and secret configured
- [ ] Application starts without errors
- [ ] GitHub login button works
- [ ] User profile information displays correctly

## 🔗 Useful Links

- [GitHub OAuth Apps](https://github.com/settings/developers)
- [Spring Security OAuth2 Documentation](https://docs.spring.io/spring-security/reference/servlet/oauth2/login.html)
- [Application Status](http://localhost:8080/auth) (when running)

After following this guide, your GitHub OAuth2 login should work perfectly! 🎉