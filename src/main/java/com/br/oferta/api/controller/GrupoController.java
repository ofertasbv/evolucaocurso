/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Grupo;
import com.br.oferta.api.service.GrupoService;
import com.br.oferta.api.service.PermissaoService;
import com.br.oferta.api.service.exception.ImpossivelExcluirEntidadeException;
import com.br.oferta.api.util.exception.NomeGrupoJaCadastradoException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private PermissaoService permissaoService;

    @GetMapping
    public ModelAndView pesquisar() {
        ModelAndView mv = new ModelAndView("paginas/grupo/table");
        mv.addObject("grupos", grupoService.findBySort());
        return mv;
    }

    @RequestMapping(value = "/nova")
    public ModelAndView nova(Grupo grupo) {
        ModelAndView mv = new ModelAndView("paginas/grupo/create");
        mv.addObject("permissoes", permissaoService.findBySort());
        return mv;
    }

    @RequestMapping(value = "/nova", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Grupo grupo, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(grupo);
        } else {
            if (grupo.getId() == null) {
                try {
                    grupoService.create(grupo);
                } catch (NomeGrupoJaCadastradoException e) {
                    result.rejectValue("nome", e.getMessage(), e.getMessage());
                    return nova(grupo);
                }
                attributes.addFlashAttribute("mensagem", "Grupo salvo com sucesso!");
            } else {
                grupoService.update(grupo.getId(), grupo);
                attributes.addFlashAttribute("mensagem", "Grupo atualizado com sucesso!");
            }
        }
        return new ModelAndView("redirect:/grupos");
    }

    @GetMapping(value = "/{id}")
    public ModelAndView editar(@PathVariable("id") Grupo grupo) {
        ModelAndView mv = nova(grupo);
        mv.addObject(grupo);
        return mv;
    }

    @DeleteMapping(value = "/excluir/{id}")
    @ResponseBody
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        try {
            grupoService.delete(id);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
