/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.model.CursoModalidade;
import com.br.oferta.api.service.CategoriaService;
import com.br.oferta.api.service.CursoService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author frc
 */
@Controller
@RequestMapping(value = "/search")
public class SearchCursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CategoriaService categoriaService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView pesquisar(CursoFilter cursoFilter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/curso/search");
        mv.addObject("modalidades", CursoModalidade.values());
        mv.addObject("cursos", cursoService.findAll());
        mv.addObject("categorias", categoriaService.findBySort());

        PageWrapper<Curso> paginaWrapper = new PageWrapper<>(cursoService.filtrar(cursoFilter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") Curso curso) {
        ModelAndView mv = new ModelAndView("paginas/curso/view");
        mv.addObject(curso);
        return mv;
    }
}
