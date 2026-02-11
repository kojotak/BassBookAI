#!/bin/bash

# OAuth2 Setup Script for Bass Book AI - Linux/Mac

echo "================================================"
echo "  Bass Book AI - OAuth2 Configuration Setup"
echo "================================================"
echo

if [ "$1" = "oauth2" ]; then
    # OAuth2 mode
    if [ ! -f "oauth2-credentials.properties" ]; then
        echo "Creating OAuth2 credentials template file..."
        echo
        
        cat > oauth2-credentials.properties << 'EOF'
# Please fill in your actual OAuth2 credentials
# GitHub OAuth2 only - Google integration removed
GITHUB_CLIENT_ID=YOUR_GITHUB_CLIENT_ID
GITHUB_CLIENT_SECRET=YOUR_GITHUB_CLIENT_SECRET
EOF
        
    echo "✅ Created oauth2-credentials.properties"
    echo "📝 Please edit this file with your actual GitHub credentials"
    echo
    echo "📖 Setup Guide: GITHUB_OAUTH2_SETUP.md"
        echo
        exit 0
    fi

    echo "Loading GitHub OAuth2 credentials from oauth2-credentials.properties..."
    
    if mvn spring-boot:run -Dspring.profiles.active=oauth2; then
        echo "🚀 Application started successfully with OAuth2!"
    else
        echo
        echo "❌ Failed to start application"
        echo "Please check your OAuth2 credentials in oauth2-credentials.properties"
        echo
        exit 1
    fi
else
    # Default read-only mode
    echo "Starting Bass Book AI in read-only mode..."
    echo
    echo "📖 To enable login features:"
    echo "   1. Set environment variables and run with oauth2 profile"
    echo "   2. Run: ./setup-oauth2.sh oauth2"
    echo

    if mvn spring-boot:run; then
        echo "🚀 Application started successfully in read-only mode!"
    else
        echo
        echo "❌ Failed to start application"
        exit 1
    fi
fi