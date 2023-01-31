# 수영장 서비스 관리 프로그램
![appimage](https://user-images.githubusercontent.com/53043464/209839490-8948fc52-b238-4d52-bc54-09eb9eed6ece.png)

## 프로젝트 개요
가상의 비즈니스 시나리오 로 수영장 운영 시 발생하는 데이터들을 관리하는 java swing gui 프로그램 개발 프로젝트 입니다.<br>
수영장(워터파크) 에 클라이언트(현장에서 등록되는 고객, 또는 입장권)-전자키(락커 룸 열쇠, 선 후불 전자 지갑- 수영장 안에서 간식 사먹거나 할 때 쓰는 그것) 관계를 DB 에 구현.<br>
이 두가지의 객체가 발생시킨 상호작용(지불,주문,전자키 등록,기타등등……) 을 기록하는 서비스 기록 도 DB 에 구현.<br>
이 DB 를 관리하고(CRUD) 데이터 들을 gui 형태로 관리하는 것이 주요기능 입니다.<br>

## 비즈니스 시나리오
수영장 고객이 현장구매 또는 예약으로 입장권을 구매 -><br>
프로그램에서 클라이언트 생성 기능으로 구매시 입력한 정보를 기반으로 고객을 의미하는 클라이언트를 DB에 생성 -><br>
고객이 수영장에 입장 할시 전자키 발급, 발급된 전자키 정보와 발급시 입력된 기타(전자지갑 잔액, 결제방식)정보 들을 DB 에 저장 -><br>
전자키 를 통한 서비스 사용내역이 서비스 기록 DB 에 반영 됨 -><br>
수영장 에서 나갈 시 정산 후 서비스 종료 기능으로 클라이언트 와 전자키 를 DB 에서 삭제

## 개발 사항
Java version : JavaSE - 1.8 <br>
Build Tool : Maven <br>
RDBMS : oracle 11g <br>
사용한 라이브러리 : java swing, ojdbc6 11.2.0.3 version <br>

## how to run
1. oracle DB 덤프
- EXPDP, IMPDP 로 백업과 덤프를 진행합니다.
- 먼저 백업 dmp 파일이 있을 디렉토리 를 생성합니다.
- sys dba 모드로 덤프를 진행할 디렉토리를 지정합니다.

```
create or replace directory dbbackup as '생성한 디렉토리의 절대 경로';
```

- 덤프를 진행하기 위하여 백업에 사용한 계정과 똑같은 계정을 생성 한 후 필요한 권한을 부여합니다.

```
create user pool_maneger identified by 1234;
grant create session, create table to pool_maneger;
grant read, write on directory dbbackup to pool_maneger;
quit;
```

- IMPDP 명령어로 덤프를 진행합니다 

```
IMPDP pool_maneger/1234 directory=dbbackup dumpfile=POOLDATA.dmp
```

2. 실행
- 실행파일 폴더에 있는 poolDataTableManager.jar 을 클릭 or cmd 창 에서 jar 실행 명령을 입력하여 실행합니다.
- 실행파일 폴더에 있는 이미지 들과 jar 파일이 같은 경로에 있어야 합니다.

```
java -jar poolDataTableManager.jar
```

- 실행시 위에서 덤프시 사용한 계정정보를 입력하거나 동등한 권한을 가진 계정 정보를 입력하세요.

## 프로그램 기능
1. 클라이언트 생성

![appimage2](https://user-images.githubusercontent.com/53043464/209994261-7c4fec21-e425-4919-bc49-acef42636a70.png)
  - 현장에서 입장 할 시 또는 예약을 할 시 사용
  - 이름, 나이, 성별, 이용권 시각을 입력하여 DB 에 입장 또는 예약을 나타내는 클라이언트 row 생성
2. 전자키 할당

![appimage2](https://user-images.githubusercontent.com/53043464/209994310-fc31372e-dd8f-4c98-8fa3-68d959a8744d.png)
  - 등록된 클라이언트가 수영장에 입장 할 시 전자키 를 발급 받을 시 사용
  - 발급 받을 클라이언트 id, 락커번호(전자키 번호), 지불방식, 할당 충전액 을 입력하여 DB 에 전자키를 나타내는 전자키 row 생성
3. 서비스 종료 

![image](https://user-images.githubusercontent.com/53043464/209994348-532a6ea3-32ec-4b7a-b1fc-1a7f549cd712.png)
  - 클라이언트가 수영장에서 나갈때 - 이용을 종료 할 때 DB 에서 클라이언트 정보와 전자키 정보를 지우는 기능
  
![image](https://user-images.githubusercontent.com/53043464/209994413-40aab0ec-c426-4eaa-9fdc-dd439ec54b3a.png)
  - 클라이언트 ID 를 입력하여 기능 사용
4. 테이블 검색

![image](https://user-images.githubusercontent.com/53043464/209994575-bf5dda31-2b4a-44d7-822c-86f01dbbea54.png)
  - 입력 조건에 따라 원하는 클라이언트, 전자키, 서비스 목록을 검색 가능
  
![image](https://user-images.githubusercontent.com/53043464/209994680-fa4ddfe3-52e2-42a7-b8f2-75e236b8962c.png)
  - 검색 결과는 그림처럼 새로운 bar 를 생성하여 출력.
5. DB 정보 열람, 서비스 기록 열람
  - 테이블 형태로 열람가능.







