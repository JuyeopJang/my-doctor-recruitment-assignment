## 실행 방법
```shell
git clone https://github.com/JuyeopJang/my-doctor-recruitment-assignment.git
```
```shell
cd ./my-doctor-recruitment-assignment
```
! docker desktop 또는 cli를 통해 docker를 먼저 실행합니다.
```shell
docker compose up --build -d
```
! spring app, mysql db가 모두 컨테이너화되었더라도 app과 db 연동 및 db 스키마 생성, 데이터 초기화 등의 작업으로 실제 애플리케이션이 동작하는 데엔 약간의 지연이 있을 수 있습니다.

## 초기 데이터 및 DB 환경
- 환자
  - id: 1, name: 손흥민
  - id: 2, name: 이강인
  - id: 3, name: 황희찬
- 의사
  - id: 1, name: 손웅래, hospital: 메라키병원
       - 진료과: 정형외과, 내과, 일반의
       - 비급여진료과: 없음
       - 영업시간: 월 ~ 금 09:00 ~ 19:00, 점심시간 11:00 ~ 12:00
         - 휴무일 근무: 없음
  - id: 2, name: 선재원, hospital: 메라키병원
       - 진료과: 한의사, 일반의
       - 비급여진료과: 다이어트약
       - 영업시간: 월 ~ 금 08:00 ~ 17:00, 점심시간 12:00 ~ 13:00
         - 휴무일 근무: 토 08:00 ~ 13:00
- db 환경
  - dbms: mysql
  - username: user
  - password: 1234
  - url: jdbc:mysql://mysql-db:3306/my_doctor
  - database: my_doctor

## API 스펙
http://localhost:8080/swagger-ui/index.html

## 요구사항 분석 및 기능 구현
1. 요구사항에 따라 주체는 환자, 의사가 있고 환자는 의사에게 진료 요청을 의사는 환자의 진료 요청을 수락할 수 있다.
2. 진료 요청 기능 (구현 완료)
   - 2.1 '의사의 영업시간이 아닌 값이 들어오면 ‘의사의 영업시간이 아님.’을 알려주는 값 반환'이라는 예외는 입력 값인 진료 희망 날짜 시간이 의사의 쉬는시간(출근 전, 퇴근 후, 주말, 점심시간)에 있을 경우 진료 요청이 불가능하도록 에외 처리를 적용한다.
   - 2.2 진료 요청의 만료 날짜 시간은 '진료 요청을 한 후 20분이 지난 후부터 의사는 진료 요청을 수락할 수 없습니다.'라는 예외는 진료 요청이 만들어질 때 진료 만료시간을 기록하고 이후에 의사가 진료 수락하는 시점의 시간과 이전에 기록한 진료 만료시간을 비교해 진료 수락하는 시점의 시간이 만료시간 이후라면 수락이 불가능하도록 예외 처리를 적용한다. 이를 토대로 4번의 진료 수락에서 실제로 예외 처리를 진행한다.
   - 2.3 또한 만료 날짜 시간은 '의사의 쉬는 시간에 진료 요청이 들어온 경우 진료 요청이 만료되는 시간은 의사의 다음 영업이 시작되는 시간 + 15분입니다.'에 따라 진료 요청 시점의 현재 시간이 의사의 출근 전, 퇴근 후, 주말, 점심시간인지에 따라 차이가 있게 된다. 출근 전의 경우 출근일의 영업 시작 시간 +15분, 퇴근 후는 다음날 영업 시작 시간 +15분, 주말은 돌아오는 가장 빠른 영업일의 영업 시작 시간 +15분, 점심시간은 점심시간이 끝나는 시간 +15분으로 지정한다. 
3. 진료 요청 검색 기능 (구현 완료)
   - 3.1 출력값이 진료 데이터, 환자 데이터가 있고 의사의 ID로 특정 의사에 해당하는 진료 요청 데이터를 조회해야 하므로 환자, 의사, 진료 3개의 테이블 간의 조인이 필요하다.
   - 3.2 이때 이미 수락된 진료는 조회하지 않아야 하므로 진료 엔터티는 칼럼으로 상태(진료 요청, 수락, 진행 중, 완료)를 갖도록 한다.
4. 진료 수락 기능 (구현 완료)
   - 4.1 진료 요청시에 기록한 진료 만료 시간과 수락하는 시점의 시간을 비교해 수락 시점의 시간이 만료 시간 이후라면 수락이 불가능하도록 예외 처리를 적용한다.
  
## ERD 설계
<img width="834" alt="스크린샷 2023-10-23 오후 4 17 14" src="https://github.com/JuyeopJang/my-doctor-recruitment-assignment/assets/68889506/96b8ceb6-60b4-4973-bdd7-1404c74f3ee3">

## 미구현 기능 구현 계획
- 의사 검색
  - 문자열 검색
    - 일반적으로 like 키워드를 통해 간단한 검색을 구현할 수도 있지만 mysql은 full text search 방식을 통해 text에 대한 전문적인 검색을 수행할 수 있는 것으로 알고 있습니다.
    - 검색 대상이 될 수 있는 칼럼으로 의사의 이름, 병원, 진료과, 비급여진료과목 칼럼을 index로 미리 만들어두고 해당 칼럼에 사용자의 검색어가 부분적으로 포함되는 경우에 해당하는 모든 행을 반환하도록 구현하려고 했습니다.
  - 날짜와 시간으로 영업 중인 의사 반환
    - 사용자가 지정한 날짜와 시간을 인풋으로 받아 의사가 가진 영업시간 리스트를 토대로 인풋의 날짜를 요일로 변환하고 영업시간 리스트에 변환한 요일이 있는지 우선 판단합니다.
    - 만약 요일이 있다면 사용자의 인풋 중 시간이 해당 요일에 출근 시간 전, 퇴근 시간 전, 점심시간이 아닌 근무 시간 사이에 있는지 판단합니다.
    - 위 두 가지 조건 모두 만족하는 의사의 리스트를 반환하도록 구현하려고 했습니다.

## 기술 스택
java, springboot, jpa, mysql

## 유닛 테스트
시간이 부족해 모든 기능에 대한 유닛 테스트를 하진 못했지만 2번의 진료 요청 기능의 예외 처리와 만료 시간 설정이 우선순위가 높다고 판단해 유닛 테스트를 작성했습니다.
2.1의 예외의 경우에 출근 전, 퇴근 후, 주말, 점심시간 네 가지 케이스에 대해 모두 진료 요청이 불가능하도록 예외를 던지는지 확인해야 하며 2.3의 만료 시간 설정값의 경우도 인풋 값에 따라 케이스가 많이 나뉘게 되는데 모든 케이스에 대한 테스트를 수행할 순 없었지만 시간이 조금 더 있었더라면 테스트와 더불어 좀 더 견고한 코드를 작성할 수 있었다고 생각합니다.

