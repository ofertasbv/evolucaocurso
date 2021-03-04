/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.model.CursoModalidade;
import com.br.oferta.api.service.CategoriaService;
import com.br.oferta.api.service.ContatoService;
import com.br.oferta.api.service.CursoService;
import com.br.oferta.api.service.GrupoService;
import com.br.oferta.api.service.MatriculaService;
import com.br.oferta.api.service.PermissaoService;
import com.br.oferta.api.service.TurmaService;
import com.br.oferta.api.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author frc
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContatoService contatoService;

    private final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @GetMapping
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView("paginas/dashboard/dashboard");
        mv.addObject("totalCursos", cursoService.count());
        mv.addObject("totalCategorias", categoriaService.count());
        mv.addObject("totalGrupos", grupoService.count());
        mv.addObject("totalPermissoes", permissaoService.count());
        mv.addObject("totalMatriculas", matriculaService.count());
        mv.addObject("totalTurmas", turmaService.count());
        mv.addObject("totalUsuarios", usuarioService.count());
        mv.addObject("totalContatos", contatoService.count());
        return mv;
    }

    @RequestMapping("/nova")
    public ModelAndView nova(Curso curso) {
        ModelAndView mv = new ModelAndView("paginas/curso/create");
        mv.addObject("modalidades", CursoModalidade.values());
        mv.addObject("categorias", categoriaService.findBySort());
        return mv;
    }
}
