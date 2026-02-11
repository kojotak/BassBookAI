# 🚨 QUICK FIX: OAuth2 Client ID Error

## **Immediate Solution**

The error `Client id must not be empty` means you need to provide OAuth2 credentials.

### **Option 1: Quick Environment Variables (Windows)**
```cmd
set GOOGLE_CLIENT_ID=your_google_client_id_here
set GOOGLE_CLIENT_SECRET=your_google_client_secret_here
set GITHUB_CLIENT_ID=your_github_client_id_here
set GITHUB_CLIENT_SECRET=your_github_client_secret_here
mvn spring-boot:run
```

### **Option 2: Quick Environment Variables (Linux/Mac)**
```bash
export GOOGLE_CLIENT_ID=your_google_client_id_here
export GOOGLE_CLIENT_SECRET=your_google_client_secret_here
export GITHUB_CLIENT_ID=your_github_client_id_here
export GITHUB_CLIENT_SECRET=your_github_client_secret_here
mvn spring-boot:run
```

### **Option 3: Add to application.properties**
Add these lines to `src/main/resources/application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=your_google_client_id_here
spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret_here
spring.security.oauth2.client.registration.github.client-id=your_github_client_id_here
spring.security.oauth2.client.registration.github.client-secret=your_github_client_secret_here
```

## **Get OAuth2 Credentials**

### **Google OAuth2**:
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create project → APIs & Services → Credentials
3. Create "OAuth client ID" → "Web application"
4. Add `http://localhost:8080` as authorized origin
5. Add `http://localhost:8080/login/oauth2/code/google` as redirect URI

### **GitHub OAuth2**:
1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. "New OAuth App"
3. Name: "Bass Book AI"
4. Homepage: `http://localhost:8080`
5. Callback: `http://localhost:8080/login/oauth2/code/github`

## **Test the Fix**

After setting credentials, run:
```bash
mvn spring-boot:run
```

The application should start without OAuth2 errors and you can click the login buttons! 🎉