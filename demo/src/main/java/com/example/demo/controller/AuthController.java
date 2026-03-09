package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return redirectBasedOnRole(auth.getAuthorities());
        }
        return "login";
    }

    @GetMapping("/login/success")
    public String loginSuccess(Authentication authentication, jakarta.servlet.http.HttpSession session) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        session.setAttribute("usuarioEmail", authentication.getName());

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String rol = authority.getAuthority();
            session.setAttribute("usuarioRol", rol.replace("ROLE_", ""));
        }

        return redirectBasedOnRole(authentication.getAuthorities());
    }

    private String redirectBasedOnRole(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null)
            return "redirect:/login";
        for (GrantedAuthority authority : authorities) {
            String rol = authority.getAuthority();
            if (rol.equals("ROLE_ADMIN")) {
                return "redirect:/panel/admin/estudiantes";
            } else if (rol.equals("ROLE_ESTUDIANTE")) {
                return "redirect:/panel/estudiante";
            } else if (rol.equals("ROLE_INSTRUCTOR")) {
                return "redirect:/panel/instructor";
            }
        }
        return "redirect:/login";
    }
}
