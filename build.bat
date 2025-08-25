@echo off
echo ===============================================
echo  Microservice DDD Hexagonal - Build Script
echo ===============================================

echo.
echo Checking Java version...
java -version

echo.
echo Note: Make sure Maven is installed and in your PATH
echo.

echo To compile the project, run:
echo mvn clean compile

echo.
echo To run tests:
echo mvn test

echo.
echo To run the application:
echo mvn spring-boot:run -pl infrastructure

echo.
echo To package the application:
echo mvn clean package

echo.
echo ===============================================
pause