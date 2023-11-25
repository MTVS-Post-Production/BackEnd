## 💡 Life : Backend>

## 🤔 About Life
- 소/중규모 애니메이션 제작 공정에 있어 프리비즈 단계에서 애니메이션의 퀄리티를 높여 전체 제작 단계를 간소화시키는 작업이 이루어지고 있습니다. 또한 언리얼 제작 환경에 특화된 병렬 방식 제작 파이프라인을 도입하여 기존 제작 공정 기간을 단축시키고 효율성을 증대시키는 것이 목표입니다.
- 제작자에게 하여금 편리한 기능을 제공하고 다양한 제작 환경에 유연하게 대처할 수 있도록 AI 기술을 융합하여 `언리얼 플러그인`을 지향하고 있습니다.

<br>

## 🕰️ 개발 기간
- 2023.10.4 ~ 2023.11.25

<br>

## 📌 주요 기능

1. 로그인 - [상세 정보](https://github.com/MTVS-Post-Production/BackEnd/wiki/Login)
- Spring Security를 활용한 소셜 로그인 구축
  - 카카오, 깃허브, 구글, 페이스북 지원
  - JWT 토큰을 통한 인가 / 인증

2. 그룹 - [상세 정보](https://github.com/MTVS-Post-Production/BackEnd/wiki/%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C-%E2%80%90-Group)
- 사용자는 그룹이라는 하나의 집단에 속할 수 있고, 해당 그룹 내에서 다양한 애니메이션 프로젝트를 생성하여 관리할 수 있습니다.
  - 프로젝트 생성 (프로젝트 정보, 배역, 스태프 등 선택)

3. 음성 변환 - [상세 정보](https://github.com/MTVS-Post-Production/BackEnd/wiki/%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C-%E2%80%90-Voice)
- 클라이언트(언리얼)에서 배역과 음성 녹음을 하고 Spring -> Flask를 거쳐 음성이 변환됩니다.
  - 변환된 음성과 배역은 서버에 저장됩니다.

4. 모션 인식 - [상세 정보](https://github.com/MTVS-Post-Production/BackEnd/wiki/%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C-%E2%80%90-Motion)
- 클라이언트(언리얼)에서 동영상을 보내면 Spring -> Flask를 거쳐 영상 내에서 등장하는 행동을 문자열(run, walk, jump... etc)로 바꿉니다.
  - Spring에서는 해당 기록을 저장하고 그 기록에 해당하는 모션을 찾아 클라이언트에 보여줍니다.
  - 사용자는 해당 모션을 확인하여 다운받을 수 있습니다.

<br>

## 🧑‍🤝‍🧑 팀원 구성
<table>
  <tr>
    <td align="center"><a href="https://github.com/bbbbooo"><img src="https://avatars.githubusercontent.com/bbbbooo" width="150px;" alt="">
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/bbbbooo"><b>이현석</b></td>
  </tr>
    <tr>
    <td align="center"><strong>Server to Server 데이터 서빙</strong></td>
  </tr>
</table>

<br>

## ⚙️ 개발 환경
- `java 11`
- IDE : `IntelliJ`
- FrameWork : `Spring Boot 2.7.16`
- DataBase : `Mysql 8.1.0`
- ORM : `Hibernate`

<br>

## ⚙️ 기술 스택
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/Spring WebClient-6DB33F?style=for-the-badge&logo=Spring WebClient&logoColor=white"> <img src="https://img.shields.io/badge/ThymeLeaf-114d15?style=for-the-badge&logo=ThymeLeaf&logoColor=white"> <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=JPA&logoColor=white"> <img src="https://img.shields.io/badge/Mysql-3085c9?style=for-the-badge&logo=Mysql&logoColor=white"> <img src="https://img.shields.io/badge/Google Cloud-3075c9?style=for-the-badge&logo=Google Cloud&logoColor=white">

<br>

## 🔫 트러블 슈팅
- [Git Action에서 Json 파일이 생성이 안되는 현상](https://velog.io/@bbbbooo/Git-Action-Json-%EC%83%9D%EC%84%B1)
- [페이징 쿼리 개선](https://velog.io/@bbbbooo/%ED%8E%98%EC%9D%B4%EC%A7%95-%EC%BF%BC%EB%A6%AC-%EC%B5%9C%EC%A0%81%ED%99%94)
- [프로젝트 상세 조회 N+1 개선]()
- [프로젝트 생성 시간 최적화]()

<br>

## 🤝 협업툴
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"/> <img src="https://img.shields.io/badge/Google Cloud-000000?style=for-the-badge&logo=Google Cloud&logoColor=white"/> <img src="https://img.shields.io/badge/Miro-F7DF1E?style=for-the-badge&logo=Miro&logoColor=black"/>

<br>

## 🤝 Collaborator

<table>
  <tr>
    <td align="center"><a href="https://github.com/MinSooC"><img src="https://avatars.githubusercontent.com/MinSooC" width="150px;" alt="">
    <td align="center"><a href="https://github.com/fortress43-dev"><img src="https://avatars.githubusercontent.com/fortress43-dev" width="150px;" alt="">
    <td align="center"><a href="https://github.com/"><img src="https://avatars.githubusercontent.com/" width="150px;" alt="">
    <td align="center"><a href="https://github.com/"><img src="https://avatars.githubusercontent.com/" width="150px;" alt="">
    <td align="center"><a href="https://github.com/"><img src="https://avatars.githubusercontent.com/" width="150px;" alt="">
    <td align="center"><a href="https://github.com/JoeHeeJae"><img src="https://avatars.githubusercontent.com/JoeHeeJae" width="150px;" alt="">
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/MinSooC"><b>차민수</b></td>
    <td align="center"><a href="https://github.com/fortress43-dev"><b>도경원</b></td>
    <td align="center"><a href="https://github.com/"><b>임성준</b></td>
    <td align="center"><a href="https://github.com/"><b>김효겸</b></td>
    <td align="center"><a href="https://github.com/"><b>이은서</b></td>
    <td align="center"><a href="https://github.com/JoeHeeJae"><b>조희재</b></td>
  </tr>
    <tr>
    <td align="center"><strong>AI</strong></td>
    <td align="center"><strong>Unreal</strong></td>
    <td align="center"><strong>Unreal</strong></td>
    <td align="center"><strong>Unreal</strong></td>
    <td align="center"><strong>3D Modeling</strong></td>
    <td align="center"><strong>Data Science</strong></td>
  </tr>
</table>

<br>

## 🤝 컨벤션

### 코드 컨벤션

[Java 컨벤션](https://github.com/woowacourse/woowacourse-docs/tree/main/styleguide/java) 가이드를 참고하여 코드를 작성합니다.


---------------------------------------------------


#### ✅ **PR & Commit 규칙**

- main branch에 바로 push 금지! develop branch로 Pull requests 하기.
- git convention을 지키기.
- PR 전에 이슈 발행 필수, PR 할 때 이슈 번호 입력 필수!
- 이슈 하나는 본인이 하루 내에 해결할 수 있는 양으로 선정하기.
- PR에 적극적으로 코드 리뷰 남기기 (LGTM 금지🙅).
