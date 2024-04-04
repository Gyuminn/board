//package org.kb.board.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.kb.board.dto.ResponseDto;
//import org.kb.board.dto.UserDto;
//import org.kb.board.service.UserServiceImpl;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/auth")
//public class UserController {
//    private final UserServiceImpl userService;
//    private AuthenticationManager authenticationManager;
//    @PostMapping("/login")
//    public ResponseEntity<ResponseDto<UserDto>> login(@RequestBody Map<String, String> paramMap) {
//        String userId = paramMap.get("user_id");
//        String userPw = paramMap.get("user_pw");
//
//        UserDetails loginUser = userService.loadUserByUsername(userId);
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userId, userPw)
//        );
//
//        // 인증 객체를 SecurityContextHolder에 설정
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        ResponseDto<UserDto> dto = new ResponseDto<>();
//
//    }
//}
