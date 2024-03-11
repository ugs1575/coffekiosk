# 1. :memo: 서비스 소개

- https://coffeekiosk.kro.kr
- CoffeeKiosk는 간단한 카페 주문 키오스크를 구현한 사이트입니다.

# 2. :coffee: 핵심 기능

## 메뉴

- 메뉴는 권한에 따라 분리되어 있습니다.
- 권한은 사용자와 관리자 2개이며 각 권한에 따라 노출되는 메뉴는 다음과 같습니다.
- 비회원 사용자는 공지사항 읽기만 가능합니다.

### 회원

[![회원 메뉴](https://github.com/ugs1575/coffekiosk/assets/44694501/d4493215-176d-4421-9683-a08241fc5f02)](https://github.com/ugs1575/coffekiosk/assets/44694501/d4493215-176d-4421-9683-a08241fc5f02)

- 공지사항 - 읽기
- 주문하기
- 장바구니
- 주문내역
- 내정보
- 포인트 충전

### 관리자

[![관리자 메뉴](https://github.com/ugs1575/coffekiosk/assets/44694501/80fd0529-85a0-4768-b423-1f828e1a2081)](https://github.com/ugs1575/coffekiosk/assets/44694501/80fd0529-85a0-4768-b423-1f828e1a2081)

- 공지사항 - 등록, 수정, 삭제
- 상품관리
- 내정보

### 권한 변경 방법

- 로그인 후 홈페이지에서 권한 변경 버튼을 클릭하시면 됩니다.
  [![권한 변경](https://github.com/ugs1575/coffekiosk/assets/44694501/332df5c1-8b61-4a71-9572-5dbc1a2ad63e)](https://github.com/ugs1575/coffekiosk/assets/44694501/332df5c1-8b61-4a71-9572-5dbc1a2ad63e)

## 주문 방법 및 기타 기능 설명

- 구글 로그인을 통해 회원가입 또는 로그인을 진행합니다.
- 처음 가입하시면 사용자는 포인트가 없는 상태이며 음료 주문은 포인트 충전 후 진행할 수 있습니다.
- 완료된 주문내역은 주문내역 페이지에서 확인할 수 있습니다.
- 현재 보유 포인트는 내정보 페이지에서 확인할 수 있습니다.
- 관리자는 메뉴에 노출될 상품을 상품관리 페이지에서 관리할 수 있습니다.
- 동일한 로직을 Form과 Api형태로 분리하여 개발하였습니다.
- Api 문서는 아래 링크에서 확인가능합니다.
    - https://coffeekiosk.kro.kr/docs/index.html

# 3. :house: 프로젝트 구조

[![다이어그램](https://github.com/ugs1575/coffekiosk/assets/44694501/45e05433-a546-40d9-9d16-09015ffe226b)](https://github.com/ugs1575/coffekiosk/assets/44694501/45e05433-a546-40d9-9d16-09015ffe226b)

# 4. :hammer: 기술스택

- Java 17, Spring Boot 3, Spring Security, QueryDsl, Spring Data JPA, Thymeleaf
- H2, MariaDB, Redis
- Nginx, AWS EC2, AWS CodeDeploy, AWS S3, AWS RDS, Travis, Docker
