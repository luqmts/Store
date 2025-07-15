package com.luq.storevs.controllers.API;

import com.luq.storevs.domain.User.AuthenticationDTO;
import com.luq.storevs.domain.User.LoginResponseDTO;
import com.luq.storevs.domain.User.RegisterDTO;
import com.luq.storevs.domain.User.User;
import com.luq.storevs.repositories.UserRepository;
import com.luq.storevs.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository uRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping(path="/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if (this.uRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.name(), data.login(), encryptedPassword, data.role());

        this.uRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path="/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authManager.authenticate(userNamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}