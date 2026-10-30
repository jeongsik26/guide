# ⭐️ 프로젝트 소개 ⭐️
혼자 여행 가긴 싫은데... 😢😢
<br>
<br>
혼자 여행가기 싫은 사람들을 위한 여행 커뮤니티, 같이 여행 갈 여행메이트를 구할 수 있으며 실시간으로 다른 회원과의 채팅으로 여행 정보를 물어볼 수 있습니다.<br>
게시판으로 같이 여행 갈 여행 메이트 구하는 모집 페이지, 여행지에서 사고 판 물건을 거래할 수 있는 거래 페이지, 다른 회원들을 위해 여행지에 대한 경험을 공유하는 리뷰 페이지 또한 글쓴이의 여행 타입을 분석하여 나와 맞는 여행 특성인지 알 수 있는 여행 MBTI들로 구성되어 있습니다.
<br>
<br>

## ✈️ 프로젝트 기간: 2024.4월 29일(월) ~ 2024.5월 15일(수)
<br>
<br>

## 🤝 프로젝트 인원 & 역할 분배
| 이름 | 역할 | 기능 |
|---------|---------|---------|
| 강승규 | 팀장 / 채팅 | 프론트엔드 |
| 김시언 | 게시판 | 프론트엔드 |
| 김재호 | 총괄 | 풀스택 |
| 엄정식 | 채팅 | 백엔드 |
| 이채림 | 회원 | 풀스택 |
| 정범수 | 게시판 | 백엔드 |
<br>
<br>

## 📚 사용 스택
|  | 이름 |
|---------|---------|
| 프론트엔드| <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white">  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white">  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=CSS3&logoColor=white">  <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">|
| 백엔드| <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">|
| DB| <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">|
<br>
<br>

## 🛠️ Tools
<img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=VisualStudioCode&logoColor=white" /> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<br>
<br> 

## 🚀 실행 방법

- Java 25, Spring Boot 4.1, Gradle 9.8 (Gradle Wrapper 포함)
- 기본 실행은 `local` 프로필로 H2 파일 DB(`./data`)를 사용합니다. 따로 DB를 설치하지 않아도 됩니다.

```bash
./gradlew bootRun
```

- 접속: http://localhost:8888/
- H2 콘솔(로컬 전용): http://localhost:8888/h2-console (JDBC URL `jdbc:h2:file:./data/guidedb`, 사용자 `sa`)
- Swagger UI: http://localhost:8888/swagger-ui/index.html
- 업로드 이미지는 `./upload` 폴더에 저장됩니다(`GUIDE_UPLOAD_PATH` 환경변수로 변경 가능).

### 서버 배포(/opt/guide)

서버의 `/opt/guide` 는 이 저장소(develop)를 클론한 폴더입니다. 실행 설정은 Git에 올리지 않는 `/opt/guide/guide.env`(DB·HTTPS 인증서 비밀번호, 권한 600)에 있습니다.

```bash
cd /opt/guide
git pull
./gradlew clean build
./shutdown.sh
./start.sh
```

- `start.sh`: `build/libs` 의 jar 를 백그라운드로 실행하고 로그를 보여줍니다. `Ctrl+C` 로 로그 보기만 빠져나오며 앱은 계속 실행됩니다.
- 로그: `/opt/guide/logs/guide.log` (실시간 보기: `tail -F /opt/guide/logs/guide.log`)
  - 하루 단위로 `guide-YYYY-MM-DD.N.log` 로 나뉘고(하루 안에서도 100MB 마다 분할), 30일 지난 로그는 자동 삭제, 전체 1GB 상한
  - `nohup.out` 에는 앱이 아예 시작되지 못한 경우의 오류만 남습니다
- `shutdown.sh`: 정상 종료를 기다리고, 30초 안에 안 꺼지면 강제 종료합니다.
- 접속: `https://<서버IP>:8888/` (자체 서명 인증서 `ssl.p12`)

### MySQL(운영) 실행

DB 접속 정보는 소스에 넣지 않고 환경변수로 전달합니다.

```bash
GUIDE_DB_URL="jdbc:mysql://<host>:3306/guidedb?characterEncoding=UTF-8&serverTimezone=Asia/Seoul" GUIDE_DB_USERNAME=guidedb GUIDE_DB_PASSWORD=<비밀번호> ./gradlew bootRun --args='--spring.profiles.active=mysql'
```

기존 MySQL 데이터를 계속 쓸 경우, 바뀐 컬럼 타입은 `ddl-auto=update`로 자동 변경되지 않으므로 직접 반영해야 합니다.

```sql
ALTER TABLE post_join MODIFY start_travel_date DATE, MODIFY end_travel_date DATE;
ALTER TABLE review    MODIFY start_travel_date DATE, MODIFY end_travel_date DATE;
ALTER TABLE post      MODIFY locationx DECIMAL(12,8), MODIFY locationy DECIMAL(12,8);
```

### 카카오맵

글쓰기·상세 화면의 지도는 카카오 JavaScript 키를 사용합니다. Kakao Developers > 앱 > 플랫폼 > Web에 실행 도메인(예: `http://localhost:8888`)을 등록해야 지도가 표시됩니다. 지도가 없어도 나머지 기능은 동작합니다.

### 테스트

```bash
./gradlew test
```

테스트는 `test` 프로필의 인메모리 H2를 사용하므로 로컬 개발 DB에 영향을 주지 않습니다.

## 📖 사용 라이브러리
<img src="https://img.shields.io/badge/KakaoAPI-FFCD00?style=for-the-badge&logo=kakao&logoColor=black"> <img src="https://img.shields.io/badge/sockjs-010101?style=for-the-badge&logo=socketdotio&logoColor=white">
<img src="https://img.shields.io/badge/stomp-010101?style=for-the-badge&logo=socketdotio&logoColor=white">
<br>
<br>

## 🧾 요구 사항 명세서
![스크린샷 2024-05-22 172221](https://github.com/GILAJABI/guide/assets/93421611/b265e0d7-9190-4eb4-b478-b6127c32afd4)

<br>
<br>

## 🧾 UseCase Diagram
![스크린샷 2024-06-06 193958](https://github.com/GILAJABI/guide/assets/93421611/f1589d1f-cac2-47eb-8418-4c8083c40c73)
![스크린샷 2024-06-06 193550](https://github.com/GILAJABI/guide/assets/93421611/d1855d1e-421d-47ee-a09b-acc0649be298)
![스크린샷 2024-06-06 193721](https://github.com/GILAJABI/guide/assets/93421611/3b8dcbf3-78e3-48f4-8884-9b4ac8f78869)

## 🎨 피그마
https://www.figma.com/design/nc1D7av1s0GGj9xY48Q5DH/TRAVELMAKER?node-id=0%3A1&t=89nA830d1BsoRpDI-1
<br>
<br>

## 🗺️ 프로젝트 아키텍쳐
![ac](https://github.com/GILAJABI/guide/assets/93421611/aa514af2-01bd-43e2-bea1-23d7285b0f44)

<br>
<br>

## 🛢️ ERD 설계
![스크린샷 2024-05-23 120255](https://github.com/GILAJABI/guide/assets/93421611/06edd656-ff0a-4fa2-bc36-222417c9812c)
<br>
<br>


## 📣 기능 소개
### 메인 페이지

**Header**
  - 사용자는 로그인 유무에 따라 보여지는 항목이 달라집니다. [로그인, 회원가입] -> [마이페이지, 로그아웃]
  
**회원 정보 영역**
  - 사용자의 프로필 등록 여부, 로그인 상태에 따라 보여지는 내용이 달라집니다.
  
**회원 슬라이더**
  - 프로필을 등록한 사용자들을 조회할 수 있습니다.
<img width="1710" alt="main" src="https://github.com/GILAJABI/guide/assets/93421611/6a86f00a-0335-4ee2-8926-634dc2647988">

### 회원 기능

**회원가입**
  - 사용자로부터 정보를 입력받아 회원가입을 진행합니다.
  - JS를 활용하여 입력값을 모두 입력받을 수 있도록 하며, 중복확인 과정을 통해 중복된 ID가 없도록 합니다.
  - 비밀번호는 salt를 할당받아 SHA-256으로 해싱합니다.

**로그인**
  - ID와 PW를 입력받아 회원 정보가 일치하면 로그인에 성공합니다.
  - 로그인 성공 시 Session, Session Storage에 회원 정보를 할당합니다.

**프로필**
  - 회원 한줄 소개, 여행유형, 사진 업로드가 가능합니다.

**마이페이지**
  - 프로필이 등록되지 않은 회원은 프로필 등록 페이지로 이동됩니다.
  - 회원 정보, 프로필, 작성한 게시글 등을 확인 및 이동이 가능합니다.

**타회원 조회**
  - 다른 회원의 정보, 프로필, 작성한 게시글 등을 확인 및 이동이 가능합니다.
  - 1대1 채팅방으로 이동할 수 있습니다.


<img width="1710" alt="2024-05-16_2 26 37" src="https://github.com/GILAJABI/guide/assets/93421611/181d17e9-4efa-4f79-98a8-030073162138">
<img width="1710" alt="2024-05-16_2 26 33" src="https://github.com/GILAJABI/guide/assets/93421611/6a7fe496-3c63-4bcf-ab15-a6d340735d93">
<img width="1710" alt="2024-05-16_2 27 21" src="https://github.com/GILAJABI/guide/assets/93421611/15291830-afec-41ee-bba7-e894b21b250a">
<img width="1710" alt="2024-05-16_2 27 38" src="https://github.com/GILAJABI/guide/assets/93421611/39a59e09-9270-4a93-b692-1a14163dd4a2">
<img width="1710" alt="2024-05-16_2 27 50" src="https://github.com/GILAJABI/guide/assets/93421611/b1d43e56-6c1d-4e9a-ab76-c82f0edf0258">

### 게시판 기능
**게시판 테이블 구조**
  - 상속 관계 매핑 전략
  - @Inheritance(strategy = InheritanceType.JOINED) : 상위 클래스 Post Entity에 설정 (정규화에 강점)
  - @DiscriminatorValue : 하위 클래스(Join, Review, Carrot)에서 설정하고, Post Entity를 상속 받아 사용한다.
  - 결과로 아래 ERD형태로 데이터가 저장된다. 유형에 따라 3가지 형태의 게시글이 등록되지만, post 테이블에 모든 게시물의 데이터가 있기 때문에 1개의 댓글 테이블로 모든 게시글의 댓글을 연결하여 사용 가능하다.

![스크린샷 2024-05-24 145453](https://github.com/GILAJABI/guide/assets/93421611/8b6f4f6f-09c0-49c1-90ae-bca5aec159d9)
![스크린샷 2024-05-24 151719](https://github.com/GILAJABI/guide/assets/93421611/397018c3-dd60-4f6d-8b65-7394f611060a)

**게시판 작성**
  - 로그인된 회원만 게시글 작성이 가능하도록 합니다. 로그인이 되지 않은 회원은 로그인 페이지로 이동됩니다.
  - 게시글 유형(리뷰, 모집, 중고거래)에 따라 저장되는 형태가 달라집니다.
  - KakaoMap API를 활용하여 여행위치, 거래위치 정보를 저장합니다.
  - 게시물 작성 성공시 회원 테이블의 회원 게시물 작성 수가 반영됩니다.

**게시판 삭제**
  - 글을 작성한 사용자는 본인이 작성한 게시글을 조회하면 삭제 버튼을 볼 수 있으며, 클릭시 등록한 게시물이 삭제됩니다.
    
**게시판 목록 조회**
  - 게시판 이미지와, 제목, 조회수, 댓글 수 등 게시판 정보를 보여주며 6개 단위로 페이징 처리하여 보여줍니다.
    
**게시판 상세 조회**
  - 게시글 상세 정보 조회가 가능합니다.
  - 조회시 조회수가 증가합니다.
  - 작성자 타회원 조회 페이지로 이동이 가능합니다.
    
**게시판 댓글 작성**
  - 로그인 된 회원은 댓글 작성이 가능합니다.
  - 로그인 하지 않을 경우 '로그인을 진행해주세요'라는 alert문구를 보여줍니다.
  - 댓글 작성시 회원 테이블의 회원 댓글 수가 반영됩니다.
    
**게시판 댓글 조회**
  - 해당 게시글에 작성된 댓글 조회가 가능합니다.
  - 이름, 내용, 시간 형태로 6개 단위로 페이징 처리하여 보여줍니다.

<img width="1710" alt="2024-05-16_2 27 56" src="https://github.com/GILAJABI/guide/assets/93421611/276f680d-a96c-4641-a4c3-e77b70aff4e0">
<img width="1710" alt="2024-05-16_2 28 00" src="https://github.com/GILAJABI/guide/assets/93421611/bb6e005d-9596-4555-9876-1d27aba54e90">
<img width="1710" alt="2024-05-16_2 28 04" src="https://github.com/GILAJABI/guide/assets/93421611/9ca44b9d-4614-4f34-912c-ec44b2a3f877">
<img width="1710" alt="2024-05-16_2 28 11" src="https://github.com/GILAJABI/guide/assets/93421611/16dfa1cd-25fd-40b3-8ffb-713d4dfc42d1">
<img width="1710" alt="2024-05-16_2 28 18" src="https://github.com/GILAJABI/guide/assets/93421611/7a92e41a-43fe-40e0-ad76-50d97459a8e7">
<img width="1710" alt="2024-05-16_2 28 26" src="https://github.com/GILAJABI/guide/assets/93421611/f89ac541-1f14-4544-ba83-6813f3a01512">
<img width="1710" alt="2024-05-16_2 29 44" src="https://github.com/GILAJABI/guide/assets/93421611/18807878-4b98-45d6-837a-73246fa25b94">

### 채팅 기능
**채팅 기능 구조**
![image](https://github.com/GILAJABI/guide/assets/93421611/78dbab7f-c943-456d-a455-fd42d5838da9) 
![스크린샷 2024-05-24 162238](https://github.com/GILAJABI/guide/assets/93421611/c34fc5b4-5dcf-45b6-bb1c-28ae555012d5)

<br>
<br>

**1대1 채팅 기능**
  - 회원은 다른 회원과 1대1 채팅이 가능합니다.
  - 타회원조회 페이지를 통해 채팅방 생성이 가능하며, 기존의 채팅방이 존재하면 기존 채팅방으로 이동됩니다.
    
**채팅방 목록**
  - 사용자는 생성된 채팅방 목록을 조회할 수 있습니다.
    
<img width="1710" alt="image" src="https://github.com/GILAJABI/guide/assets/93421611/81111fc9-be44-4c74-8514-58940040aae9">
<img width="1710" alt="2024-05-16_2 30 15" src="https://github.com/GILAJABI/guide/assets/93421611/1e29bc0b-56b6-427e-845b-003626963596">




