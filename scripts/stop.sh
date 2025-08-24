#!/usr/bin/env bash

echo "> 현재 실행 중인 애플리케이션 pid 확인"
CURRENT_PID=$(lsof -ti tcp:8080)

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 실행 중인 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 5
fi