#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ubuntu/app/zip  # ← 사용자 디렉토리 수정

echo "> Build 파일을 복사합니다."
# zip 내부에서 zip으로 복사하므로 생략 가능 (원문 유지 시 아래 줄 삭제 가능)
cp $REPOSITORY/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"

# plain.jar을 제외한 실행 가능한 jar만 선택
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | grep -v 'plain' | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한을 부여합니다."
chmod +x $JAR_NAME

IDLE_PROFILE=$(find_idle_profile)

echo "> 새 애플리케이션을 $IDLE_PROFILE 로 실행합니다."

# ===== 환경변수 설정 =====
export DB_URL=jdbc:mysql://localhost:3306/dndnDB
export DB_USERNAME=root
export DB_PASSWORD=pyj0402
# ========================

nohup java -jar \
  -Dspring.profiles.active=$IDLE_PROFILE,prod \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
