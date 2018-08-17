@echo off

cd /d %~dp0


call mvn package -Pdev -U -Dmaven.test.skip=true
if %errorlevel% == 0 (echo. & echo [Building] Successful. & echo. & pause)
if %errorlevel% == 1 (echo. & echo [Building] Failed. & echo. & pause)
