package org.kb.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.domain.UserEntity;
import org.kb.board.dto.UserDto;
import org.kb.board.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
// Spring Security에서 필수로 구현해야 하는 UserDetailsService
// Spring Secuirty에서는 회원 정보를 User라고 하고 아이디 대신에 username이라는 용어를 사용
// 아이디를 가지고 데이터를 먼저 조회하고 그 후 비밀번호를 비교하는 형식으로 로그인을 수행
// 비밀번호가 틀리면 Bad Credenital 이라는 결과를 만듬.
// 로그인에 성공하면 자원에 접근할 수 있는 권한이 있는지 확인하고 권한이 없으면 Access Denied
// loadUserByUsername이라는 메서드만 소유
// 리턴 타입을 만드는 방법은 DTO 클래스에 UserDetails를 구현하는 방법이 있고 별도로 DTO 클래스를 생성하는 방법이 있다.
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // User 찾아오기
        UserEntity userEntity = userRepository.findUserEntityByEmailId(username);

        if (userEntity == null) {
            log.info("username '{}' is not found", username);
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        } else {
            log.info("user--------------------");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 로그인 성공했을 때 정보를 저장해서 리턴
        // 비밀번호는 제외하는 경우가 많다.
        UserDto userDto = new UserDto(
                userEntity.getUserId(),
                userEntity.getEmailId(),
                userEntity.getPassword(),
                userEntity.getNickname(),
                userEntity.getIntroContent(),
                userEntity.getProvider(),
                authorities
        );

        log.info(String.valueOf(userDto));
        return userDto;
    }
}
