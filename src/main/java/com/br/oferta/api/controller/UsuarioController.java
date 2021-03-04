/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.controller;

import com.br.oferta.api.model.Contato;
import com.br.oferta.api.model.Curso;
import com.br.oferta.api.model.Usuario;
import com.br.oferta.api.service.CursoService;
import com.br.oferta.api.service.GrupoService;
import com.br.oferta.api.service.UsuarioService;
import com.br.oferta.api.util.exception.CpfCnpjClienteJaCadastradoException;
import com.br.oferta.api.util.exception.EmailUsuarioJaCadastradoException;
import com.br.oferta.api.util.exception.SenhaObrigatoriaUsuarioException;
import com.br.oferta.api.util.filter.UsuarioFilter;
import com.br.oferta.api.util.page.PageWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author frc
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ModelAndView pesquisarByFilter(UsuarioFilter filter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("paginas/usuario/table");
        mv.addObject("usuarios", usuarioService.findBySort());
        PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarioService.filtrar(filter, pageable), httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @RequestMapping("/nova")
    public ModelAndView nova(Usuario usuario) {
        ModelAndView mv = new ModelAndView("paginas/usuario/create");
        mv.addObject("grupos", grupoService.findBySort());
        return mv;
    }

    @PostMapping({"/nova", "{\\+d}"})
    public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(usuario);
        }
        try {
            usuarioService.create(usuario);
        } catch (EmailUsuarioJaCadastradoException e) {
            result.rejectValue("email", e.getMessage(), e.getMessage());
            return nova(usuario);
        } catch (SenhaObrigatoriaUsuarioException e) {
            result.rejectValue("senha", e.getMessage(), e.getMessage());
            return nova(usuario);
        }

        attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
        return new ModelAndView("redirect:/usuarios");
    }

    @RequestMapping(value = "/inscricao", method = RequestMethod.POST)
    public ModelAndView inscricao(Curso curso, @Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novaInscricao(curso, usuario);
        }
        try {
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Cpf: " + usuario.getCpf());
            System.out.println("Foto: " + usuario.getFoto());
            System.out.println("Data Nascimento: " + usuario.getDataNascimento());
            System.out.println("Data Registro: " + usuario.getDataRegistro());
            System.out.println("Telefone: " + usuario.getTelefone());
            System.out.println("Status: " + usuario.isStatus());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Senha: " + usuario.getSenha());
            System.out.println("ConfirmaSenha: " + usuario.getConfirmaSenha());
            usuarioService.create(usuario);
        } catch (EmailUsuarioJaCadastradoException e) {
            result.rejectValue("email", e.getMessage(), e.getMessage());
            return novaInscricao(curso, usuario);
        } catch (SenhaObrigatoriaUsuarioException e) {
            result.rejectValue("senha", e.getMessage(), e.getMessage());
            return novaInscricao(curso, usuario);
        }

        return new ModelAndView("redirect:/usuarios/comprovante");
    }

    @GetMapping("/inscricao/{id}")
    public ModelAndView novaInscricao(@PathVariable("id") Curso curso, Usuario usuario) {

        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Cpf: " + usuario.getCpf());
        System.out.println("Foto: " + usuario.getFoto());
        System.out.println("Data Nascimento: " + usuario.getDataNascimento());
        System.out.println("Data Registro: " + usuario.getDataRegistro());
        System.out.println("Telefone: " + usuario.getTelefone());
        System.out.println("Status: " + usuario.isStatus());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Senha: " + usuario.getSenha());
        System.out.println("ConfirmaSenha: " + usuario.getConfirmaSenha());

        ModelAndView mv = new ModelAndView("paginas/inscricao/inscricao");
        curso = cursoService.findById(curso.getId()).get();
        mv.addObject(usuario);
        mv.addObject("curso", curso);
        return mv;
    }

    @RequestMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") Usuario usuario) {
        ModelAndView mv = new ModelAndView("paginas/usuario/view");
        mv.addObject("usuario", usuario);
        return mv;
    }

    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarComGrupos(id);
        ModelAndView mv = nova(usuario);
        mv.addObject(usuario);
        return mv;
    }

    @GetMapping("/recuperar_email")
    public ModelAndView recuperarEmail(Usuario usuario) {
        ModelAndView mv = new ModelAndView("paginas/inscricao/recuperar_email");
        return mv;
    }

    @GetMapping("/buscar_email")
    public ModelAndView buscarEmail(Usuario usuario) {
        Usuario u = usuarioService.findById(usuario.getId()).get();
        return new ModelAndView("redirect:/usuarios/recuperar_email");
    }

    @GetMapping("/recuperar_cpf")
    public ModelAndView recuperarCpf() {
        ModelAndView mv = new ModelAndView("paginas/inscricao/recuperar_cpf");
        return mv;
    }

    @GetMapping("/recuperar_senha")
    public ModelAndView recuperarSenha() {
        ModelAndView mv = new ModelAndView("paginas/inscricao/recuperar_senha");
        return mv;
    }

    @RequestMapping("/comprovante")
    public ModelAndView comprovante(Usuario usuario) {
        ModelAndView mv = new ModelAndView("paginas/usuario/comprovante");
        return mv;
    }

}
