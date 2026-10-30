#!/bin/bash
# guide 종료 스크립트 (/opt/guide)
APP_HOME=/opt/guide
JAR=build/libs/guide-0.0.1-SNAPSHOT.jar

cd ${APP_HOME} || exit 1

PID=$(pgrep -f "java .*${APP_HOME}/${JAR}")
if [ -z "${PID}" ]; then
    echo "실행 중인 guide 가 없습니다."
    rm -f ${APP_HOME}/pid.file
    exit 0
fi

# 정상 종료(SIGTERM) 후 30초 안에 안 내려가면 강제 종료
kill ${PID}
for i in $(seq 1 30); do
    if ! kill -0 ${PID} 2>/dev/null; then
        echo "guide 종료 (PID: ${PID})"
        rm -f ${APP_HOME}/pid.file
        exit 0
    fi
    sleep 1
done

kill -9 ${PID}
echo "guide 강제 종료 (PID: ${PID})"
rm -f ${APP_HOME}/pid.file
