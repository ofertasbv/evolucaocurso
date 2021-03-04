/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.service.CategoriaService;
import com.br.oferta.api.service.CursoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author frc
 */
@Controller
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ModelAndView pesquisar() {
        ModelAndView mv = new ModelAndView("paginas/curso/table");
        mv.addObject("cursos", cursoService.findAll());
        return mv;
    }

    @RequestMapping("/nova")
    public ModelAndView nova(Curso curso) {
        ModelAndView mv = new ModelAndView("paginas/curso/create");
        mv.addObject("categorias", categoriaService.findBySort());
        return mv;
    }

    @RequestMapping(value = "/nova", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Curso curso, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(curso);
        }

        cursoService.create(curso);
        attributes.addFlashAttribute("mensagem", "Curso salvo com sucesso!");
        return new ModelAndView("redirect:/cursos");
    }

    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable("id") Curso curso) {
        ModelAndView mv = nova(curso);
        mv.addObject(curso);
        return mv;
    }

}
