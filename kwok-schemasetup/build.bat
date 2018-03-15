@echo off

set CURRENT_DIR=%~dp0

cd %CURRENT_DIR%
C:\Kwok\Server\apache_ant_1.7.0\bin\ant dist
pause