#!/bin/sh

# 도커 로그인 환경변수 설정 (CI/CD 워크플로우에서 export 해주거나, env로 전달받는다고 가정)
export DOCKER_USERNAME=${DOCKER_USERNAME}
export DOCKER_PASSWORD=${DOCKER_PASSWORD}

# 도커 로그인 및 도커 이미지 다운로드
echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin
docker pull matt1235/plac:latest

# 'plac-server'라는 이름의 도커 컨테이너가 실행 중인 경우, 컨테이너를 중지하고 삭제
if [ $(docker ps -aq -f name=plac-server) ]; then
    docker stop plac-server
    docker rm plac-server
fi

# 도커 컨테이너 실행시 필요한 환경변수 설정 (GitHub Secrets 값들 환경변수로 전달)
export SERVER_PORT=${SERVER_PORT}
export DB_HOST=${DB_HOST}

export JWT_ACCESS_SECRET=${JWT_ACCESS_SECRET}
export JWT_REFRESH_SECRET=${JWT_REFRESH_SECRET}

# 도커 컨테이너 실행
docker run -d \
  --name plac-server \
  -p $SERVER_PORT:$SERVER_PORT \
  -e DB_HOST=$DB_HOST \
  -e JWT_ACCESS_SECRET=$JWT_ACCESS_SECRET \
  -e JWT_REFRESH_SECRET=$JWT_REFRESH_SECRET \
  matt1235/plac:latest
