#!/bin/bash
# 빌드 파일의 이름이 콘텐츠와 다르다면 다음 줄의 .jar 파일 이름을 수정하시기 바랍니다.
BUILD_JAR=$(ls /home/ubuntu/action/build/libs/dallinday-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)

echo "> 현재 시간: $(date)"
echo "> build 파일명: $JAR_NAME"
echo "> build 파일 복사"

DEPLOY_PATH=/home/ubuntu/
cp $BUILD_JAR $DEPLOY_PATH

# 기존 프로세스는 systemd가 관리하므로 kill -9 대신 systemctl 사용
echo "> systemd 데몬 리로드"
sudo systemctl daemon-reload

# 서비스가 이미 돌고 있어도 restart로 대체 배포
echo "> 서비스 재시작"
sudo systemctl restart dallinday

echo "> 상태 확인"
sleep 2
sudo systemctl status dallinday --no-pager -l || true

echo "> 최근 100줄 로그"
sudo journalctl -u "$SERVICE" -n 100 --no-pager

#echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/deploy.log
#CURRENT_PID=$(pgrep -f $JAR_NAME)
#
#if [ -z $CURRENT_PID ]
#then
#  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/deploy.log
#else
#  echo "> kill -9 $CURRENT_PID" >> /home/ubuntu/deploy.log
#  sudo kill -9 $CURRENT_PID
#  sleep 5
#fi
#
#
#DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
#echo "> DEPLOY_JAR 배포"    >> /home/ubuntu/deploy.log
#sudo nohup java -jar $DEPLOY_JAR >> /home/ubuntu/deploy.log 2>/home/ubuntu/deploy_err.log &