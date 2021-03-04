/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.model.CursoModalidade;
import com.br.oferta.api.model.Usuario;
import com.br.oferta.api.service.CategoriaService;
import com.br.oferta.api.service.CursoService;
import com.br.oferta.api.util.filter.CategoriaFilter;
import com.br.oferta.api.util.filter.CursoFilter;
import com.br.oferta.api.util.page.PageWrapper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author frc
 */
@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ModelAndView pesquisar(CursoFilter cursoFilter, CategoriaFilter categoriaFilter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/home/home");

        mv.addObject("modalidades", CursoModalidade.values());
        mv.addObject("categorias", categoriaService.findAll());

        PageWrapper<Curso> cursosFilter = new PageWrapper<>(cursoService.filtrar(cursoFilter, pageable), httpServletRequest);
        mv.addObject("pagina", cursosFilter);
        return mv;
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") Curso curso, Usuario usuario) {
        ModelAndView mv = new ModelAndView("paginas/curso/view");
        mv.addObject(curso);
        return mv;
    }

    @GetMapping("/searchpessoa/{id}")
    public ModelAndView searchpessoa(@PathVariable("id") Curso curso, Usuario usuario) {
        ModelAndView mv = new ModelAndView("paginas/curso/searchpessoa");
        mv.addObject(curso);
        return mv;
    }

    @GetMapping("/contato")
    public ModelAndView contato() {
        ModelAndView mv = new ModelAndView("paginas/contato/contato");
        return mv;
    }

    @GetMapping("/sobre")
    public ModelAndView sobre() {
        ModelAndView mv = new ModelAndView("paginas/sobre/sobre");
        return mv;
    }
}
