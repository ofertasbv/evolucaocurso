/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Contato;
import com.br.oferta.api.model.Curso;
import com.br.oferta.api.service.ContatoService;
import com.br.oferta.api.service.exception.ImpossivelExcluirEntidadeException;
import com.br.oferta.api.util.exception.NomeCategoriaJaCadastradoException;
import com.br.oferta.api.util.filter.ContatoFilter;
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
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @GetMapping
    public ModelAndView pesquisar(ContatoFilter filter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/contato/table");
        PageWrapper<Contato> paginaWrapper = new PageWrapper<>(contatoService.filtrar(filter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @RequestMapping("/nova")
    public ModelAndView nova(Contato contato) {
        ModelAndView mv = new ModelAndView("paginas/contato/create");
        return mv;
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Contato contato, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(contato);
        } else {
            if (contato.getId() == null) {
                try {
                    contatoService.create(contato);
                } catch (Exception e) {
                    result.rejectValue(e.getMessage(), e.getMessage());
                    return nova(contato);
                }
                attributes.addFlashAttribute("mensagem", "Contato salva com sucesso!");
            } else {
                contatoService.update(contato.getId(), contato);
                attributes.addFlashAttribute("mensagem", "Contato atualizada com sucesso!");
            }
        }
        return new ModelAndView("redirect:/contatos");
    }

    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable("id") Contato contato) {
        ModelAndView mv = nova(contato);
        mv.addObject(contato);
        return mv;
    }

    @RequestMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") Contato contato) {
        ModelAndView mv = new ModelAndView("paginas/contato/view");
        mv.addObject("contato", contato);
        return mv;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        try {
            contatoService.delete(id);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
