# DNDN-BackEnd-Repository

든든팀의 백엔드 레포지토리입니다.

# DNDN-BackEnd-Repository

든든팀의 백엔드 레포지토리입니다.

## 🌿 브랜치 전략

```bash
- main
- develop
  - [팀원 이름]
    - 라벨/기능명

- `main` : 배포 가능한 안정화 버전  
- `develop` : 다음 배포를 위한 통합 브랜치  
- `[팀원 이름]` : 팀원별 브랜치  
- `feature/*` : 기능 단위 작업용 브랜치  
- `chore` : 빌드 설정, 프로젝트 초기 설정, 환경 구성 등
```

## 🧱 코드 컨벤션

### 📦 도메인 기반 패키지 구조

```java
└── global
    ├── config
    ├── exception
    └── s3
    ...

└── domain(기능명)
    └── api
        ├── controller
        └── dto
            ├── request
            │   └── {어떤 dto인지}ReqDto
            └── response
                └── {어떤 dto인지}ResDto
    └── application
        └── service
    └── domain
        ├── repository
        │   └── Repository
        └── entity
```

## 📝 네이밍 컨벤션

| 항목                     | 스타일                | 예시                                                       |
|------------------------|---------------------|----------------------------------------------------------|
| 패키지명 / 변수명 / 메서드명 | `lowerCamelCase`      | `addUser()`, `userName`, `findByEmail()`                 |
| 클래스명 / 인터페이스명    | `UpperCamelCase`      | `UserService`, `LoginRequestDto`, `UserController`       |
| 상수명 (static final)   | `SCREAMING_SNAKE_CASE` (전체 대문자 + 언더스코어) | `DEFAULT_PAGE_SIZE`, `MAX_LOGIN_ATTEMPTS`                |

※ `/`를 입력해 명령어처럼 사용 가능

---

## 🌐 API 엔드포인트 네이밍

- **기능**: `/quest/list`  
- **파라미터**: `{quest-id}`

## ✅ Issue 네이밍 규칙

- 기능 추가 : `feat`
- 버그 수정 : `fix`
- 리팩토링 : `refactor`
- 문서 작업 : `docs`

📌 이슈 이름 예시:  
`[Feat] 작업 내용`

## Issue Template

```
## 어떤 기능인가요?

> 추가하려는 기능에 대해 간결하게 설명해주세요

## 작업 상세 내용

- [ ] 새로운 기능
- [ ] 테스트
- [ ] TODO
```

## PR Template

```
#️⃣연관된 이슈
ex) #이슈번호, #이슈번호

📝작업 내용
이번 PR에서 작업한 내용을 간략히 설명해주세요

💬리뷰 요구사항(선택)
리뷰어가 특별히 봐주었으면 하는 부분이 있다면 작성해주세요

ex) 메서드 XXX의 이름을 더 잘 짓고 싶은데 혹시 좋은 명칭이 있을까요?
```

## 커밋 규칙

- feat[#23]: 로그인 API 구현
