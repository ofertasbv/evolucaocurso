/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.model.CursoModalidade;
import com.br.oferta.api.model.Ementa;
import com.br.oferta.api.service.CategoriaService;
import com.br.oferta.api.service.CursoService;
import com.br.oferta.api.util.filter.CursoFilter;
import com.br.oferta.api.util.page.PageWrapper;
import com.br.oferta.api.util.storage.Disco;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author frc
 */
@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private Disco disco;

    private final Logger logger = LoggerFactory.getLogger(CursoController.class);

    @GetMapping
    public ModelAndView pesquisar(CursoFilter cursoFilter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/curso/table");

        mv.addObject("modalidades", CursoModalidade.values());
        mv.addObject("categorias", categoriaService.findAll());
        PageWrapper<Curso> paginaWrapper = new PageWrapper<>(cursoService.filtrar(cursoFilter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @RequestMapping("/nova")
    public ModelAndView nova(Curso curso) {
        ModelAndView mv = new ModelAndView("paginas/curso/create");
        mv.addObject("modalidades", CursoModalidade.values());
        mv.addObject("categorias", categoriaService.findAll());
        return mv;
    }

    @RequestMapping(value = "/nova", method = RequestMethod.POST)
    public ModelAndView salvar(@ModelAttribute(value = "curso") @Valid Curso curso, BindingResult brCurso, RedirectAttributes attributes) {

        if (brCurso.hasErrors()) {
            System.err.println("Erros: " + brCurso.hasErrors());
            return nova(curso);
        }

        System.out.println("Nome: " + curso.getNome());
        System.out.println("Descrição: " + curso.getDescricao());
        System.out.println("Foto: " + curso.getNome());
        System.out.println("Data Registro: " + curso.getDataRegistro());
        System.out.println("Categoria: " + curso.getCategoria().getNome());
        System.out.println("Status: " + curso.isStatus());
        System.out.println("Novo: " + curso.isNovo());
        System.out.println("Destaque: " + curso.isDestaque());
        System.out.println("Ementa: " + curso.getEmenta().getDescricaoEmenta());

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

    @RequestMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") Curso curso) {
        ModelAndView mv = new ModelAndView("paginas/curso/view");
        mv.addObject("curso", curso);
        return mv;
    }

    @PostMapping(value = "/teste/upload")
    public void upload(@RequestParam MultipartFile foto) {
        disco.salvarFoto(foto);
    }

//    @PostMapping(value = "/nova", params = "enviarEmail")
//    public ModelAndView enviarEmail(Curso curso, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//        
//        if (result.hasErrors()) {
//            return nova(curso);
//        }
//
//        venda.setUsuario(usuarioSistema.getUsuario());
//
//        venda = cadastroVendaService.salvar(venda);
//        mailer.enviar(venda);
//
//        attributes.addFlashAttribute("mensagem", String.format("Venda nº %d salva com sucesso e e-mail enviado", venda.getCodigo()));
//        return new ModelAndView("redirect:/vendas/nova");
//    }
}
