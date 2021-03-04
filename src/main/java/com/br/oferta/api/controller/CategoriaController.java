/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Categoria;
import com.br.oferta.api.model.Curso;
import com.br.oferta.api.service.CategoriaService;
import com.br.oferta.api.service.exception.ImpossivelExcluirEntidadeException;
import com.br.oferta.api.util.exception.NomeCategoriaJaCadastradoException;
import com.br.oferta.api.util.filter.CategoriaFilter;
import com.br.oferta.api.util.page.PageWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author frc
 */
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ModelAndView pesquisar(CategoriaFilter filter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/categoria/table");
        PageWrapper<Categoria> paginaWrapper = new PageWrapper<>(categoriaService.filtrar(filter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @RequestMapping("/search")
    public ModelAndView search(CategoriaFilter filter, BindingResult result, @PageableDefault(size = 12) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/categoria/search");
        PageWrapper<Categoria> paginaWrapper = new PageWrapper<>(categoriaService.filtrar(filter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @RequestMapping("/nova")
    public ModelAndView nova(Categoria categoria) {
        ModelAndView mv = new ModelAndView("paginas/categoria/create");
        return mv;
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Categoria categoria, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(categoria);
        } else {
            if (categoria.getId() == null) {
                try {
                    categoriaService.create(categoria);
                } catch (NomeCategoriaJaCadastradoException e) {
                    result.rejectValue("nome", e.getMessage(), e.getMessage());
                    return nova(categoria);
                }

                attributes.addFlashAttribute("mensagem", "Categoria salva com sucesso!");
            } else {
                categoriaService.update(categoria.getId(), categoria);
                attributes.addFlashAttribute("mensagem", "Categoria atualizada com sucesso!");
            }
        }
        return new ModelAndView("redirect:/categorias");
    }

    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable("id") Categoria categoria) {
        ModelAndView mv = nova(categoria);
        mv.addObject(categoria);
        return mv;
    }

    @RequestMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") Categoria categoria) {
        ModelAndView mv = new ModelAndView("paginas/categoria/view");
        mv.addObject("categoria", categoria);
        return mv;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        try {
            categoriaService.delete(id);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
