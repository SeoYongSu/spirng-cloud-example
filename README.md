# spirng-cloud-mas-example
spring cloud msa example<br/>
Reactive를 활용<br/>

서비스 목록<br/>
#1) User service(webflux) : 사용자 및 인증관련 서비스 예제<br/>
Spring Security 관련 설정
RouterConfig에 Handler Commpornet 등록 후 URI 설정<br/>
# api List<br/>
 Handelr<br/>
      - 회원가입<br/>
      - 로그<br/>
      - oauth 로그인<br/>
      - 유저 상세정보<br/>
 Controller<br/>
    - 유저의 board 목록연결 -> WebClient Example<br/>
    
    
 
 gateway  : Api-Gateway 예제로만 사<br/>

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
     
     
 
