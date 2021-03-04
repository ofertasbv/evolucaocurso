/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Matricula;
import com.br.oferta.api.service.MatriculaService;
import com.br.oferta.api.service.TurmaService;
import com.br.oferta.api.service.UsuarioService;
import com.br.oferta.api.service.exception.ImpossivelExcluirEntidadeException;
import com.br.oferta.api.util.exception.DescricaoMatriculaJaCadastradoException;
import com.br.oferta.api.util.filter.MatriculaFilter;
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
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ModelAndView pesquisar(MatriculaFilter filter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/matricula/table");
        mv.addObject("matriculas", matriculaService.findBySort());
        mv.addObject("turmas", turmaService.findBySort());
        mv.addObject("usuarios", usuarioService.findBySort());
        PageWrapper<Matricula> paginaWrapper = new PageWrapper<>(matriculaService.filtrar(filter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @GetMapping("/turma/{id}")
    public ModelAndView findByTurmaById(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("paginas/turma/turma_matricula");
        mv.addObject("matriculas_turma", matriculaService.findByTurmaById(id));
        mv.addObject("turma", turmaService.findById(id));
        return mv;
    }

    @RequestMapping("/nova")
    public ModelAndView nova(Matricula matricula) {
        ModelAndView mv = new ModelAndView("paginas/matricula/create");
        mv.addObject("turmas", turmaService.findBySort());
        mv.addObject("usuarios", usuarioService.findBySort());
        return mv;
    }

    @RequestMapping(value = "/nova", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Matricula matricula, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(matricula);
        } else {
            if (matricula.getId() == null) {
                try {
                    matriculaService.create(matricula);
                } catch (DescricaoMatriculaJaCadastradoException e) {
                    result.rejectValue("descricao", e.getMessage(), e.getMessage());
                    return nova(matricula);
                }
                attributes.addFlashAttribute("mensagem", "Matricula salvo com sucesso!");
            } else {
                matriculaService.update(matricula.getId(), matricula);
                attributes.addFlashAttribute("mensagem", "Matricula atualizado com sucesso!");
            }
        }
        return new ModelAndView("redirect:/matriculas");
    }

    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable("id") Matricula matricula) {
        ModelAndView mv = nova(matricula);
        mv.addObject(matricula);
        return mv;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        try {
            matriculaService.delete(id);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
