# spirng-cloud-mas-example
spirng-cloud-mas-example


User service : Webflux 사용<br/> 사용자 및 인증관련 서비스 샘플
 #Spring Security 관련 예제 소스
 #RouterConfig에 Handler Commpornet 등록 후 URI 설정
 api List
    Handelr
      - 회원가입
      - 로그
      - oauth 로그인
      - 유저 상세정보
    Controller
    - 유저의 board 목록연결 -> WebClient Example
    
    
 
 gateway  : Api-Gateway 예제로만 사
    
    yml로 설정한 URL
      Board Service 
        - GET /board/all
        - GET /board/get/{idx}
      UserBoard -> UserService
        - GET /userboard
        
   Class
     Signup  -> UserService
       - POST /signup
     userInfo -> UserService GatewayURL과 Service의 URL을 다르게 사용예제
       - GET /user   -> UserService GET /my
     인증관련 -> userService
     
     
 
