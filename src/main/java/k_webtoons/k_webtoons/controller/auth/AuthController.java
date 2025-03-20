package k_webtoons.k_webtoons.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import k_webtoons.k_webtoons.model.user.LoginDTO;
import k_webtoons.k_webtoons.model.user.UserRegisterDTO;
import k_webtoons.k_webtoons.model.user.UserResponse;
import k_webtoons.k_webtoons.security.JwtUtil;
import k_webtoons.k_webtoons.service.user.AppUserDetails;
import k_webtoons.k_webtoons.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    // 회원가입
    @Operation(
            summary = "회원가입 API",
            description = "사용자가 이메일, 비밀번호, 나이, 성별, 닉네임을 제공하여 회원가입합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "회원가입 성공",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterDTO dto){
        UserResponse response = userService.register(dto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //로그인
    @Operation(
            summary = "로그인 API",
            description = "사용자가 이메일과 비밀번호를 제공하여 로그인하고 JWT 토큰을 반환받습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공 및 JWT 토큰 반환",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.userEmail(), dto.userPassword())
        );

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        String role = userDetails.getRole();

        String token = jwtUtil.generateToken(dto.userEmail(), role);
        return ResponseEntity.ok(token);
    }
}