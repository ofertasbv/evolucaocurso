package com.br.oferta.api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SegurancaController {

    @RequestMapping(value = "/login")
    public String login(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/";
        }
        return "Login";
    }

    @GetMapping("/403")
    public ModelAndView acessoNegado() {
        return new ModelAndView("/403");
    }

}
