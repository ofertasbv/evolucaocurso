/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Turma;
import com.br.oferta.api.model.TurmaStatus;
import com.br.oferta.api.model.TurmaTurno;
import com.br.oferta.api.service.CursoService;
import com.br.oferta.api.service.MatriculaService;
import com.br.oferta.api.service.TurmaService;
import com.br.oferta.api.service.UsuarioService;
import com.br.oferta.api.service.exception.ImpossivelExcluirEntidadeException;
import com.br.oferta.api.util.exception.NomeTurmaJaCadastradoException;
import com.br.oferta.api.util.filter.TurmaFilter;
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
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MatriculaService matriculaService;

    @GetMapping
    public ModelAndView pesquisar(TurmaFilter filter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/turma/table");
        mv.addObject("turmas", turmaService.findBySort());
        mv.addObject("cursos", cursoService.findAll());
        mv.addObject("turnos", TurmaTurno.values());
        PageWrapper<Turma> paginaWrapper = new PageWrapper<>(turmaService.filtrar(filter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @RequestMapping("/nova")
    public ModelAndView nova(Turma turma) {
        ModelAndView mv = new ModelAndView("paginas/turma/create");
        mv.addObject("estatus", TurmaStatus.values());
        mv.addObject("turnos", TurmaTurno.values());
        mv.addObject("cursos", cursoService.findAll());
        mv.addObject("usuarios", usuarioService.findBySort());
        return mv;
    }
    
    @RequestMapping("/teste")
    public ModelAndView teste() {
        ModelAndView mv = new ModelAndView("paginas/turma/teste");
        return mv;
    }

    @RequestMapping(value = "/nova", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Turma turma, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(turma);
        } else {
            if (turma.getId() == null) {
                try {
                    turmaService.create(turma);
                } catch (NomeTurmaJaCadastradoException e) {
                    result.rejectValue("nome", e.getMessage(), e.getMessage());
                    return nova(turma);
                }
                attributes.addFlashAttribute("mensagem", "Turma salva com sucesso!");
            } else {
                turmaService.update(turma.getId(), turma);
                attributes.addFlashAttribute("mensagem", "Turma atualizada com sucesso!");
            }
        }
        return new ModelAndView("redirect:/turmas");
    }

    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable("id") Turma turma) {
        ModelAndView mv = nova(turma);
        mv.addObject(turma);
        return mv;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        try {
            turmaService.delete(id);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
