/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Permissao;
import com.br.oferta.api.service.PermissaoService;
import com.br.oferta.api.service.exception.ImpossivelExcluirEntidadeException;
import com.br.oferta.api.util.exception.DescricaoPermissaoJaCadastradoException;
import com.br.oferta.api.util.filter.PermissaoFilter;
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
@RequestMapping("/permissoes")
public class PermissaoController {

    @Autowired
    private PermissaoService permissaoService;

    @GetMapping
    public ModelAndView pesquisar(PermissaoFilter filter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/permissao/table");
        mv.addObject("permissoes", permissaoService.findBySort());
        PageWrapper<Permissao> paginaWrapper = new PageWrapper<>(permissaoService.filtrar(filter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @RequestMapping(value = "/nova")
    public ModelAndView nova(Permissao permissao) {
        ModelAndView mv = new ModelAndView("paginas/permissao/create");
        return mv;
    }

    @RequestMapping(value = "/nova", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Permissao permissao, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(permissao);

        } else {
            if (permissao.getId() == null) {
                try {
                    permissaoService.create(permissao);
                } catch (DescricaoPermissaoJaCadastradoException e) {
                    result.rejectValue("descricao", e.getMessage(), e.getMessage());
                    return nova(permissao);
                }
                attributes.addFlashAttribute("mensagem", "Permissao salva com sucesso!");
            } else {
                permissaoService.update(permissao.getId(), permissao);
                attributes.addFlashAttribute("mensagem", "Permissao atualizada com sucesso!");
            }
        }
        return new ModelAndView("redirect:/permissoes");
    }

    @GetMapping(value = "/{id}")
    public ModelAndView editar(@PathVariable("id") Permissao permissao) {
        ModelAndView mv = nova(permissao);
        mv.addObject(permissao);
        return mv;
    }

    @DeleteMapping(value = "/excluir/{id}")
    @ResponseBody
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        try {
            permissaoService.delete(id);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
