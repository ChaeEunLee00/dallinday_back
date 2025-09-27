#!/bin/bash
# CodeDeploy의 SSH로 스크립트 실행 (즉, EC2의 SSH에서 로그 확인 불가)
# 배포 훅 실행 로그 확인: sudo tail -n 200 /var/log/aws/codedeploy-agent/codedeploy-agent.log
# 애플리케이션(Spring) 로그 확인:
# 1) 최근 100줄
#    sudo journalctl -u dallinday -n 100 --no-pager
# 2) 실시간(종료: Ctrl+C)
#    sudo journalctl -u dallinday -f

# 빌드 파일의 이름이 콘텐츠와 다르다면 다음 줄의 .jar 파일 이름을 수정
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