package org.kb.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.board.security.filter.APILoginFilter;
import org.kb.board.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity // 보안 설정 활성화
public class CustomSecurityConfig {

    private final UserServiceImpl userService;

    // PasswordEncoder 빈을 생성
    // 복호화가 불가능한 암호화를 수행해주는 클래스
    // 복호화는 불가능하지만 비교는 가능.
    // SpringBoot에서는 내부적으로 BCryptPasswordEncoder를 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 이전에는 WebSecurityConfigurerAdapter를 상속받아서 하는 시큐리티 설정이 있었는데 Deprecated 되었다. -> filter chain 사용
    // Spring Security에서 제공하는 인증, 인가를 위한 필터들의 모음.
    // http 요청 -> servlet contianer -> filter1 -> filter2 -> ... -> servlet -> controller
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("----------------configure------------------");

        // 인증 관리자 설정
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        // 필터 등록 - 토큰을 생성할 때 동작
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        // 필터가 먼저 동작하도록 설정
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);


        // csrf
        // 공격자가 인증된 브라우저에 저장된 쿠키의 세션 정보를 활용하여 웹 서버에 사용자가 의도하지 않은 위조 요청을 전달하는 것.
        // REST API 개발 진행중이고 Session 기반 인증과 다르기 때문에 서버에 인증 정보를 보관하지 않고,
        // 권한 요청시 필요한 인증정보(OAuth2, jwt토큰 등) 요청을 포함하기 때문에 불필요한 csrf 보안은 disable 한다.

        // Httpbasic, FormLogin
        // HttpBasic -> Http basic Auth 기반으로 로그인 인증창이 뜬다.
        // Json을 통해 로그인을 진행하는데, 로그인 이후 refresh 토큰이 만료되기 전까지 토큰을 통한 인증을 진행할 것이기 때문에 비활성화

        // authorizeHttpRequests()
        // 인증, 인가가 필요한 URL 지정
        // requestMathers("url").permitAll() -> 여기에서 지정된 url은 인증, 인가 없이도 접근을 허용한다.
        // anyRequest() -> requestMathers에서 지정된 URL외의 요청에 대한 설정
        // authenticated() -> 해당 URL에 진입하기 위해서는 인증이 필요함.
        // hasAuthority() -> 해당 URL에 진입하기 위해서 Authorization(인가, 예로 ADMIN만 진입가능 -> .hasAuthority(UserRole.ADMIN.name())

        // logout()
        // 로그아웃에 대한 정보
        // invalidateHttpSession() -> 로그아웃 이후 전체 세션 삭제 여뷰

        // sessionManagement()
        // 세션 생성 및 사용 여부에 대한 정책 설정
        // sessionCreationPolicy -> 정책을 설정한다.
        // SessionCreationPolicy.STATELESS -> 4가지 정책 중 하나로 스프링 시큐리팅가 생성하지 않고 존재해도 사용하지 않는다.(JWT와 같이 세션을 사용하지 않는 경우 사용)
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/signup", "/**", "/login").permitAll() // /** -> / 로 바꿔주어야 함.
                        .anyRequest().authenticated())
                .logout((logout) -> logout.logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}