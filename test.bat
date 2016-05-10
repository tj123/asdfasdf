@echo off
set "current_dir=%~dp0%"
set exe="%current_dir%target\sshlogin-1.0-SNAPSHOT-jar-with-dependencies.jar"

call java -jar %exe% -s 192.168.1.22 -u root -p yuanben -f "%current_dir%test"

pause