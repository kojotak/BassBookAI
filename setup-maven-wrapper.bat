@echo off
echo Downloading Maven wrapper files...

REM Create .mvn directory if it doesn't exist
if not exist ".mvn" mkdir .mvn
if not exist ".mvn\wrapper" mkdir .mvn\wrapper

REM Download Maven wrapper jar
echo Downloading Maven wrapper jar...
powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar'"

echo Downloading Maven wrapper script...
powershell -Command "Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/apache/maven/master/mvnw.cmd' -OutFile 'mvnw.cmd'"

echo Maven wrapper setup complete!
echo You can now run: mvnw.cmd clean compile