@echo off
REM OAuth2 Setup Script for Bass Book AI - Windows

echo ================================================
echo   Bass Book AI - OAuth2 Configuration Setup
echo ================================================
echo.

if "%1"=="oauth2" goto :oauth2
if "%1"=="profile" goto :oauth2

:default_mode
echo Starting Bass Book AI in read-only mode...
echo.
echo 📖 To enable login features:
echo    1. Set environment variables and run with oauth2 profile
echo    2. Run: setup-oauth2.bat oauth2
echo.

mvn spring-boot:run
goto :end

:oauth2
REM Check if credentials file exists
if not exist "oauth2-credentials.properties" (
    echo Creating OAuth2 credentials template file...
    echo.
    echo # Please fill in your actual OAuth2 credentials > oauth2-credentials.properties
    echo # Google OAuth2 >> oauth2-credentials.properties
    echo GOOGLE_CLIENT_ID=YOUR_GOOGLE_CLIENT_ID >> oauth2-credentials.properties
    echo GOOGLE_CLIENT_SECRET=YOUR_GOOGLE_CLIENT_SECRET >> oauth2-credentials.properties
    echo. >> oauth2-credentials.properties
    echo # GitHub OAuth2 >> oauth2-credentials.properties
    echo GITHUB_CLIENT_ID=YOUR_GITHUB_CLIENT_ID >> oauth2-credentials.properties
    echo GITHUB_CLIENT_SECRET=YOUR_GITHUB_CLIENT_SECRET >> oauth2-credentials.properties
    echo.
    echo ✅ Created oauth2-credentials.properties
    echo 📝 Please edit this file with your actual credentials
    echo.
    echo 📖 Setup Guide: OAUTH2_SETUP_GUIDE.md
    echo.
    pause
    exit /b 0
)

REM Load credentials from file
echo Loading OAuth2 credentials from oauth2-credentials.properties...

REM Start application with OAuth2 profile
mvn spring-boot:run -Dspring.profiles.active=oauth2

if %errorlevel% neq 0 (
    echo.
    echo ❌ Failed to start application
    echo Please check your OAuth2 credentials in oauth2-credentials.properties
    echo.
    pause
)

:end