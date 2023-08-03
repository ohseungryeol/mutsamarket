# Project_1_OhSeungryeol
## ERD
![image](https://github.com/likelion-backend-5th/Project_1_OhSeungryeol/assets/82445894/9d5979d5-a791-4fdd-b198-5a28a7ead1b2)
- users 1:N sales_item
- users 1:N comment
- users 1:N negotiaion

- items 1:N comment
- items 1:N negotiaion  
## 📃 Product Progress


#### ✅ 회원 가입 : curl --location 'http://localhost:8080/users/join' \
```
--data '{
    "username": "승민",
    "password": "123456"    
}'
```

```
{
    "message": "회원가입 성공"
}
```

- 사용자 이름 중복 불가 

#### ✅ 로그인 : curl --location 'http://localhost:8080/users/login' \

- 존재하지 않는 사용자 예외처리 
- 비밀번호 오류 예외처리 

#### Request
```
--data '{
    "username": "승민",
    "password": "123456"    
}'
```
#### Response  
```
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLsirnrr7wiLCJpYXQiOjE2OTA5ODM0MzMsImV4cCI6MTY5MDk4NzAzM30.YM9ILgPObglSu806dMguSORXM8yU8k6Gx9n1miDuumiI0qZ74d022edXoX-DHThOR3tEvt-gqlicWbwgzZ1UdA",
    "exprTime": 3600000,
    "user": {
        "id": 1,
        "username": "승민",
        "password": "",
        "items": [],
        "comments": [],
        "negos": []
    }
}
```

#### ✅ 새 아이템 작성 : curl --location 'http://localhost:8080/items' \
- 인증된 사용자만 작성 가능
- 
#### Request
```
--data '{
    "title": "노트팝니다",
    "description": "노트 팔아요.",
    "min_price_wanted":200000
    
}'
```

#### Response
```
{
    "message": "등록이 완료되었습니다."
}
```
#### ✅ 모든 아이템 조회 :curl --location 'http://localhost:8080/items'

- 등록된 물품이 아닌 요청시 예외처리

#### Response
```
{
    "content": [
        {
            "id": 1,
            "title": "노트팝니다",
            "description": "노트 팔아요.",
            "min_price_wanted": 200000,
            "status": "판매중",
            "writer": "승민"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 25,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 1,
    "totalPages": 1,
    "size": 25,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
}
```

#### ✅ 제안글 작성 'http://localhost:8080/items/1/proposals' \
- 해당 물품 없을시 예외처리
- 해당 제안 없을시 예외처리
- 인증된 유저만 작성 가능
#### Request
```
{
    "suggested_price": 1000000
}
```

### Response
```
{
    "message": "구매 제안이 등록되었습니다."
}
```

#### ✅ 제안 수정 : http://localhost:8080/myinfo?take=item
- 인증 유저가 제안 등록자일 때
  - 현재 status가 "제안"이면 suggested_price만 수정
  - 현재 status가 "수락"이면 status를 "확정"으로 변경, 물품의 상태를 "판매완료"로 변경, 해당 물품의 나머지 제안 글들의 status를 "거절"로 변경
     
- 인증 유저가 아이템 등록자일 때
  - 요청 바디 status가 "수락"이면 제안 status를 "수락"으로 변경
  - 요청 바디 status가 "거절"이면 제안 status를 "거절"로 변경 


## RESTAPI

### ✅ users
- 회원가입 기능, 로그인시 jwt발급 
#### POST : 회원가입 http://localhost:8080/users/join

#### POST : 로그인 http://localhost:8080/users/login



---
### ✅ SalesItem

#### POST : 상품 등록 /items

#### GET : 단일 상품 조회 /items/{itemId}

#### GET : 전체 상품 페이지별 조회 /items?page=0&limit=25

#### PUT : 상품 수정 /items/{itemId}

#### DELETE : 상품 삭제 /items/{itemId}

#### PUT : 프로필 이미지 설정 /items/{itemId}/image

✅⬜
### ✅ Comment
- 물품에 대한 댓글 관리 기능 

#### POST : 댓글 등록 /items/{itemId}/comments

#### GET : 불러오기 /items/{itemId}/comments

#### PUT : 수정 /items/{itemId}/comments/{commentId}

#### PUT : 답글 추가 /items/{itemId}/comments/{commentId}/reply

#### DELETE : 답글 삭제 /items/{itemId}/comments/{commentId}

---

### ✅ Negotiation
- 물품에 대한 제안 관리 기능 

#### POST :제안 글등록 /items/{itemId}/proposals

#### GET : 제안 글 조회  /items/{itemId}/proposals

#### PUT : 제안 수정 /items/{itemId}/proposals/{proposalId}

#### DELETE : 제안 삭제 /items/{itemId}/proposals/{proposalId}




