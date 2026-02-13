# OAuth2 Setup Guide for Bass Book AI

## 🚨 Error Resolution

The error `Client id must not be empty` occurs because Spring Boot requires OAuth2 credentials to be configured when OAuth2 is enabled.

## 📋 Complete OAuth2 Setup Guide

### 1. Google OAuth2 Setup

#### Step 1: Create Google Cloud Project
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable **Google+ API** and **Google OAuth2 API**:
   - Go to "APIs & Services" → "Library"
   - Search and enable "Google+ API" and "Google OAuth2 API"

#### Step 2: Create OAuth2 Credentials
1. Go to "APIs & Services" → "Credentials"
2. Click "+ CREATE CREDENTIALS" → "OAuth client ID"
3. Select **Web application**
4. Configure:
   - **Name**: Bass Book AI
   - **Authorized JavaScript origins**: `http://localhost:8080`
   - **Authorized redirect URIs**: `http://localhost:8080/login/oauth2/code/google`
5. Click "Create"

#### Step 3: Get Credentials
Copy your **Client ID** and **Client Secret** from the credentials page.

### 2. GitHub OAuth2 Setup

#### Step 1: Create GitHub OAuth App
1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. Click "OAuth Apps" → "New OAuth App"
3. Configure:
   - **Application name**: Bass Book AI
   - **Homepage URL**: `http://localhost:8080`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
   - **Application description**: Bass guitar play-along video manager
4. Click "Register application"

#### Step 2: Get Credentials
Copy your **Client ID** and **Client Secret** from the application page.

## ⚙️ Configuration Methods

### Method 1: Environment Variables (Recommended)

#### Windows:
```cmd
set GOOGLE_CLIENT_ID=your_google_client_id_here
set GOOGLE_CLIENT_SECRET=your_google_client_secret_here
set GITHUB_CLIENT_ID=your_github_client_id_here
set GITHUB_CLIENT_SECRET=your_github_client_secret_here
mvn spring-boot:run
```

#### Linux/Mac:
```bash
export GOOGLE_CLIENT_ID=your_google_client_id_here
export GOOGLE_CLIENT_SECRET=your_google_client_secret_here
export GITHUB_CLIENT_ID=your_github_client_id_here
export GITHUB_CLIENT_SECRET=your_github_client_secret_here
mvn spring-boot:run
```

### Method 2: application.properties File

Add to `src/main/resources/application.properties`:

```properties
# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=your_google_client_id_here
spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret_here
spring.security.oauth2.client.registration.google.scope=openid,profile,email

# GitHub OAuth2
spring.security.oauth2.client.registration.github.client-id=your_github_client_id_here
spring.security.oauth2.client.registration.github.client-secret=your_github_client_secret_here
spring.security.oauth2.client.registration.github.scope=user:email
```

### Method 3: application.yml File (Alternative)

Create `src/main/resources/application.yml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your_google_client_id_here
            client-secret: your_google_client_secret_here
            scope: openid,profile,email
          github:
            client-id: your_github_client_id_here
            client-secret: your_github_client_secret_here
            scope: user:email
```

## 🛡️ Security Best Practices

### For Development:
- ✅ Use `http://localhost:8080` for all URLs
- ✅ Add both JavaScript origins and redirect URIs
- ✅ Keep client secrets out of version control

### For Production:
- 🔄 Change `localhost:8080` to your actual domain
- 🔒 Use HTTPS URLs
- 📝 Restrict OAuth scopes to minimum required
- 🗑️ Regularly rotate client secrets

## 🚀 Quick Start Template

### Step 1: Create `.env` file (for local development):
```bash
# Create .env file in project root
cat > .env << EOF
GOOGLE_CLIENT_ID=your_google_client_id_here
GOOGLE_CLIENT_SECRET=your_google_client_secret_here
GITHUB_CLIENT_ID=your_github_client_id_here
GITHUB_CLIENT_SECRET=your_github_client_secret_here
EOF
```

### Step 2: Create startup script:
```bash
# start-with-env.sh
#!/bin/bash

# Load environment variables from .env
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
fi

# Start application
mvn spring-boot:run
```

### Step 3: Make it executable:
```bash
chmod +x start-with-env.sh
./start-with-env.sh
```

## 🔍 Testing OAuth2 Setup

### Test Google Login:
1. Start application: `mvn spring-boot:run`
2. Navigate to `http://localhost:8080`
3. Click "Login with Google"
4. Should redirect to Google OAuth2 consent screen
5. After approval, redirect back to application

### Test GitHub Login:
1. Click "Login with GitHub" 
2. Should redirect to GitHub OAuth2 authorization
3. After authorization, redirect back to application

## 🐛 Troubleshooting

### Common Issues:

#### 1. "Client id must not be empty"
**Solution**: Set environment variables or update application.properties with actual credentials

#### 2. "redirect_uri_mismatch" Error
**Solution**: Ensure redirect URI exactly matches:
- Google: `http://localhost:8080/login/oauth2/code/google`
- GitHub: `http://localhost:8080/login/oauth2/code/github`

#### 3. "invalid_client" Error  
**Solution**: Verify client ID and secret are correct and properly copied

#### 4. CORS Issues
**Solution**: Add your domain to authorized JavaScript origins in OAuth2 app settings

## 📋 Configuration Checklist

- [ ] Google OAuth2 app created and enabled
- [ ] Google redirect URI: `http://localhost:8080/login/oauth2/code/google`
- [ ] GitHub OAuth2 app created
- [ ] GitHub redirect URI: `http://localhost:8080/login/oauth2/code/github`
- [ ] Client IDs and secrets configured (environment variables or properties)
- [ ] Application starts without OAuth2 errors
- [ ] Login buttons work and redirect properly
- [ ] User profile information displays correctly

## 🔗 Useful Links

- [Google Cloud Console](https://console.cloud.google.com/)
- [GitHub OAuth Apps](https://github.com/settings/developers)
- [Spring Security OAuth2 Documentation](https://docs.spring.io/spring-security/reference/servlet/oauth2/login.html)

After following this guide, your OAuth2 login should work perfectly! 🎉