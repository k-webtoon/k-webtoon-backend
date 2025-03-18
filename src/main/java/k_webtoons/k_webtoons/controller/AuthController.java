package k_webtoons.k_webtoons.controller;

import k_webtoons.k_webtoons.model.LoginDTO;
import k_webtoons.k_webtoons.model.UserRegisterDTO;
import k_webtoons.k_webtoons.model.UserResponse;
import k_webtoons.k_webtoons.security.JwtUtil;
import k_webtoons.k_webtoons.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterDTO dto){
        UserResponse response = userService.register(dto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.userEmail(),dto.userPassword())
        );
        String token =jwtUtil.generateToken(dto.userEmail());
        return ResponseEntity.ok(token);
    }
}
