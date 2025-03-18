package k_webtoons.k_webtoons.controller;

import k_webtoons.k_webtoons.model.AppUser;
import k_webtoons.k_webtoons.model.LoginDTO;
import k_webtoons.k_webtoons.model.UserRegisterDTO;
import k_webtoons.k_webtoons.model.UserResponse;
import k_webtoons.k_webtoons.security.JwtUtil;
import k_webtoons.k_webtoons.service.AppUserDetails;
import k_webtoons.k_webtoons.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
