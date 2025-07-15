package com.luq.storevs.controllers.Web;

import com.luq.storevs.domain.User.AuthenticationDTO;
import com.luq.storevs.domain.User.LoginResponseDTO;
import com.luq.storevs.domain.User.User;
import com.luq.storevs.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationWebController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private TokenService tService;

    @GetMapping(path="/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping(path="/register")
    public String registerPage(){
        return "register";
    }

    @PostMapping(path="/login")
    public String login(@RequestBody @Valid AuthenticationDTO data){
        var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authManager.authenticate(userNamePassword);

        var token = tService.generateToken((User) auth.getPrincipal());
        ResponseEntity.ok(new LoginResponseDTO(token));
        return "redirect:/product/list";
    }
}