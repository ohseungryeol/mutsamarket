# Project_1_OhSeungryeol
중고거래 플랫폼을 착안하여 아이템을 등록하고, 댓글을 이용한 소통, 구매 제안과 구매 확정 기능을 제공하는 개인 백엔드 프로젝트입니다.
## ERD
![image](https://github.com/likelion-backend-5th/Project_1_OhSeungryeol/assets/82445894/9d5979d5-a791-4fdd-b198-5a28a7ead1b2)

- items 1:N negotiaion  
## 📃 Product Progress



## RESTAPI

### ✅ users
- 회원가입 기능, 로그인시 jwt발급 
#### POST : 회원가입 http://localhost:8080/users/join

#### POST : 로그인 http://localhost:8080/users/login



---
### ✅ SalesItem
- 상품 관리 기능
#### POST : 상품 등록 /items

#### GET : 단일 상품 조회 /items/{itemId}

#### GET : 전체 상품 페이지별 조회 /items?page=0&limit=25

#### PUT : 상품 수정 /items/{itemId}

#### DELETE : 상품 삭제 /items/{itemId}

#### PUT : 프로필 이미지 등록 /items/{itemId}/image

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




