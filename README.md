# 코발트 커피
<div align="left">
<img width="300" src="img/ic_logo.png">
</div>

<br/>

## 프로젝트 소개
**코발트 커피** 프로젝트는 Android Mobile Application 프로젝트로, 가상의 카페앱 제작의뢰를 상정하여 어떤 기능이 필요할지 요구사항을 추려내고, 해당 요구사항에 따라 기능을 구현한 **개발진의 역량향상** 프로젝트입니다.

코발트 블루 색상에서 테마색을 착안하였고, 주로 **스타벅스** 및 **이디야 커피** 애플리케이션의 UI 구성 및 기능을 클론코딩하고자 하였습니다.

- 2022.11.25. 삼성청년소프트웨어아카데미(SSAFY) 8기 Mobile 트랙 1학기 관통프로젝트 최우수

<br/>

## 목차
- [참여자](#참여자)
- [발표자료](#발표자료)
- [기간](#기간)
- [기능구현](#기능구현)
- [스크린샷](#스크린샷)

<br/>

## 참여자

| 조수연([@su6378](https://github.com/su6378)) | 한재용([@rigizer](https://github.com/rigizer)) |
|:----:|:----:|
|<img width="200" src="img/sy_jo.png">|<img width="200" src="img/jy_han.png">|

<br/>

# 발표자료
[다운로드](ppt/221125_8기_구미_6반_관통PJT_한재용_조수연.pptx) (*.pptx)

<br/>

## 기간
2022.11.17. ~ 2022.11.25.

<br/>

# 사용 기술스택
- Android
  - Android Studio
  - Kotlin
  - AndroidX ROOM
  - SQLite
  - 부트페이 API
- Back-end
  - AWS Lightsail
  - Java
  - Spring Boot
  - MariaDB

## 기능구현
- 스플래시
    - Lottie 이용한 로딩화면 구현
- 안드로이드 커스텀 폰트 적용
- FitButton 이용한 커스텀 버튼 구현
- Retrofit 이용하여 AWS Lightsail의 MariaDB와 RestAPI 통신
- CollapsingToolbarLayout 사용하여 레이아웃 애니메이션 적용
- SliderShow 라이브러리 사용하여 배너광고 애니메이션 적용
- SharedPreference, Room 등 라이브러리 활용
- SwitchButton 라이브러리 활용하여 토글버튼 구현

<br/>

## 스크린샷
<h5>스플래시</h5>
<hr>

- Handler 이용하여 overridePendingTransition 애니메이션 적용

![image](./img/코발트커피_스플래시.gif)
<br/>

<h5>로그인</h5>
<hr>

- 정규표현식 패턴 사용
- 자동로그인 구현 (SharedPreference 사용)

| 자동로그인 ON | 자동로그인 OFF | 계정정보 불일치 |
|:----:|:----:|:----:|
|![image](./img/코발트커피_자동로그인_ON.gif)|![image](./img/코발트커피_자동로그인_OFF.gif)|![image](./img/코발트커피_로그인_계정정보불일치.gif)|
<br/>

<h5>회원가입</h5>
<hr>

- 정규표현식 패턴 사용

| 이메일 중복확인 | 비밀번호 확인 |
|:----:|:----:|
|![image](./img/코발트커피_회원가입_아이디.gif)|![image](./img/코발트커피_회원가입_비밀번호.gif)|
<br/>

<h5>홈</h5>
<hr>

- Lottie 라이브러리 사용
- CollapsingToolbarLayout 사용
- SliderShow 라이브러리 사용

| CollapsingToolbarLayout | 첫 번째 배너 |
|:----:|:----:|
|![image](./img/코발트커피_홈.gif)|![image](./img/코발트커피_홈_첫번째배너.gif)|

| 수평 슬라이더 메뉴 | 하단 배너 | 뒤로가기 버튼 |
|:----:|:----:|:----:|
|![image](./img/코발트커피_홈_수평슬라이더메뉴.gif)|![image](./img/코발트커피_홈_하단배너.gif)|![image](./img/코발트커피_홈_뒤로가기.gif)|
<br/>

<h5>주문</h5>
<hr>

- 배너광고 무한스크롤, 자동스크롤 지원
- FitButton 라이브러리 사용

| 주문 전 | 최근 주문 내역 |
|:----:|:----:|
|![image](./img/코발트커피_주문.gif)|![image](./img/코발트커피_주문_최근주문내역.gif)|
<br/>

<h5>메뉴</h5>
<hr>

- RecyclerView, Retrofit 이용하여 메뉴정보 조회

| 싱품목록 | 상품 상세정보 |
|:----:|:----:|
|![image](./img/코발트커피_주문_메뉴.gif)|![image](./img/코발트커피_주문_메뉴상세정보.gif)|
<br/>

<h5>장바구니</h5>
<hr>

- Android Room 이용하여 SQLite 접근
- Retrofit 이용하여 상품정보 조회

| 주문 | 주문불가 |
|:----:|:----:|
|![image](./img/코발트커피_주문_장바구니담기.gif)|![image](./img/코발트커피_주문_주문불가.gif)|
<br/>

<h5>결제</h5>
<hr>

- 부트페이 API 사용하여 결제기능 구현
- 신용/체크카드, 카카오페이, 페이코 결제 지원
- 장바구니 연동
- 결제 완료시 FLAG_ACTIVITY_CLEAR_TASK, FLAG_ACTIVITY_NEW_TASK 플래그 정보에 의한 태스크 스택 적용

| 쿠폰사용 | 신용/체크카드 결제 |
|:----:|:----:|
|![image](./img/코발트커피_주문_쿠폰사용.gif)|![image](./img/코발트커피_주문_신용카드결제.gif)|

| 카카오페이 결제 | 결제완료 |
|:----:|:----:|
|![image](./img/코발트커피_결제_카카오페이.gif)|![image](./img/코발트커피_결제완료.gif)|
<br/>

<h5>기타</h5>
<hr>

- 쿠폰/스탬프 개수 올라가는 애니메이션 구현
  - CoroutineScope, Dispatcher.IO, suspend
- 로그아웃 기능 구현
  - SharedPreference에 있는 사용자 정보 삭제

| 사용자 정보 | 스탬프 |
|:----:|:----:|
|![image](./img/코발트커피_기타_스탬프쿠폰.gif)|![image](./img/코발트커피_기타_스탬프발급_적립내역.gif)|

| 쿠폰 | 주문내역 |
|:----:|:----:|
|![image](./img/코발트커피_쿠폰.gif)|![image](./img/코발트커피_기타_주문내역.gif)|
<br/>

<h5>설정</h5>
<hr>

- Retrofit 이용하여 사용자 테이블 정보수정
- 토글 가능한 버튼 UI 구현 (SwitchButton)

| 설정 | 닉네임 변경 | 비밀번호 변경 |
|:----:|:----:|:----:|
|![image](./img/코발트커피_설정.gif)|![image](./img/코발트커피_설정_닉네임변경.gif)|![image](./img/코발트커피_설정_비밀번호변경.gif)|

| 푸쉬 알림 | 위치 권한 | 마케팅 활용 정보동의 |
|:----:|:----:|:----:|
|![image](./img/코발트커피_설정_푸쉬알림설정.gif)|![image](./img/코발트커피_설정_위치권한설정.gif)|![image](./img/코발트커피_설정_마케팅활용동의설정.gif)|
<br/>
