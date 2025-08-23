#!/usr/bin/env bash
set -e

REPOSITORY=/home/ubuntu/app/zip
LOGFILE=$REPOSITORY/nohup.out
PORT=8080

echo "> 실행할 JAR 선택"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | grep -v 'plain' | tail -n 1)
echo "> JAR: $JAR_NAME"

chmod +x "$JAR_NAME"

echo "> 애플리케이션 실행 (port: $PORT)"
nohup java -jar \
  -Dserver.port=$PORT \
  "$JAR_NAME" > "$LOGFILE" 2>&1 &

sleep 2
echo "> 시작 로그 tail"
tail -n 50 "$LOGFILE" || true
