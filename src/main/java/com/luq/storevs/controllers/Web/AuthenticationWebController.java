package com.luq.storevs.controllers.Web;

import com.luq.storevs.domain.User.AuthenticationDTO;
import com.luq.storevs.domain.User.LoginResponseDTO;
import com.luq.storevs.domain.User.User;
import com.luq.storevs.domain.User.UserRole;
import com.luq.storevs.services.AuthorizationService;
import com.luq.storevs.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
public class AuthenticationWebController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private TokenService tService;
    @Autowired
    private AuthorizationService uService;

    @GetMapping(path="/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping(path="/user/list")
    public ModelAndView userList(
        @RequestParam(name="sortBy", required=false, defaultValue="name") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="login", required=false) String login,
        @RequestParam(name="role", required=false) String role
    ){
        name = (Objects.equals(name, "")) ? null : name;
        login = (Objects.equals(login, "")) ? null : login;

        List<User> uList = uService.getAllSorted(sortBy, direction, name, login, role);

        ModelAndView mv = new ModelAndView("user-list");
        mv.addObject("page", "user");
        mv.addObject("users", uList);
        mv.addObject("roles", UserRole.values());
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);
        mv.addObject("selectedRole", role);
        mv.addObject("name", name);
        mv.addObject("login", login);

        return mv;
    }

    @GetMapping(path="/user/form")
    public ModelAndView registerPage(){
        ModelAndView mv = new ModelAndView("user-form");
        mv.addObject("user", new User());
        mv.addObject("page", "user");
        mv.addObject("roles", UserRole.values());

        return mv;
    }

    @GetMapping(path="/user/form/{id}")
    public ModelAndView editUserPage(@PathVariable("id") String id){
        User user = uService.getById(id);
        ModelAndView mv = new ModelAndView("user-form");
        mv.addObject("user", user);
        mv.addObject("page", "user");
        mv.addObject("roles", UserRole.values());

        return mv;
    }

    @PostMapping(path="/login")
    public String login(@RequestBody @Valid AuthenticationDTO data){
        var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authManager.authenticate(userNamePassword);

        var token = tService.generateToken((User) auth.getPrincipal());
        ResponseEntity.ok(new LoginResponseDTO(token));
        return "redirect:/product/list";
    }

    @PostMapping(path="/user/form")
    public String postUser(User user, Model model){
        boolean hasError = false;

        if (user.getPassword() == null){
            model.addAttribute("passwordError", "Passwords don't match");
            hasError = true;
        }

        if(hasError){
            model.addAttribute("page", "user");
            model.addAttribute("roles", UserRole.values());
            return "user-form";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(null);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setCreatedBy(authentication.getName());
            user.setCreated(LocalDateTime.now());
            user.setModifiedBy(authentication.getName());
            user.setModified(LocalDateTime.now());

            uService.register(user);
        } else {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setModifiedBy(authentication.getName());
            user.setModified(LocalDateTime.now());
            uService.update(user.getId(), user);
        }

        return "redirect:/user/list";
    }

    @GetMapping(path="/user/delete/{id}")
    public String delete(@PathVariable("id") String id){
        uService.delete(id);
        return "redirect:/user/list";
    }
}