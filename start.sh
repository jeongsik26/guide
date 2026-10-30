#!/bin/bash
# guide 실행 스크립트 (/opt/guide)
APP_HOME=/opt/guide
JAR=build/libs/guide-0.0.1-SNAPSHOT.jar

cd ${APP_HOME} || exit 1

if [ ! -f ${APP_HOME}/${JAR} ]; then
    echo "실행할 jar 가 없습니다. 먼저 ./gradlew clean build 로 빌드하세요."
    exit 1
fi

PID=$(pgrep -f "java .*${APP_HOME}/${JAR}")
if [ -n "${PID}" ]; then
    echo "guide 가 이미 실행 중입니다. (PID: ${PID})"
    exit 1
fi

# DB 접속 정보, HTTPS 인증서 설정 등 (권한 600)
set -a
. ${APP_HOME}/guide.env
set +a

# setsid: 터미널(Xshell)을 닫거나 Ctrl+C 해도 앱이 같이 종료되지 않도록 세션 분리
setsid nohup java -Xms256m -Xmx1g -jar ${APP_HOME}/${JAR} > ${APP_HOME}/nohup.out 2>&1 < /dev/null &
echo $! > ${APP_HOME}/pid.file
echo "guide 시작 (PID: $!)"

tail -100f ${APP_HOME}/nohup.out
