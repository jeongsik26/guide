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

# 로그: logs/guide.log (하루 단위 분할, 30일 보관, 최대 1GB)
export GUIDE_LOG_PATH=${APP_HOME}/logs
mkdir -p ${GUIDE_LOG_PATH}

# setsid: 터미널(Xshell)을 닫거나 Ctrl+C 해도 앱이 같이 종료되지 않도록 세션 분리
# 화면(콘솔) 로그는 끄고 로그 파일에만 쓴다. nohup.out 에는 앱이 아예 못 뜬 경우의 오류만 남는다
setsid nohup java -Xms256m -Xmx1g -jar ${APP_HOME}/${JAR} \
    --logging.threshold.console=OFF --spring.main.banner-mode=log \
    > ${APP_HOME}/nohup.out 2>&1 < /dev/null &
echo $! > ${APP_HOME}/pid.file
echo "guide 시작 (PID: $!)"
echo "로그: ${GUIDE_LOG_PATH}/guide.log  (Ctrl+C 로 로그 보기만 종료, 앱은 계속 실행)"

# -F: 로그 파일이 새로 생기거나 날짜가 바뀌어 교체돼도 계속 따라간다
tail -n 100 -F ${GUIDE_LOG_PATH}/guide.log
