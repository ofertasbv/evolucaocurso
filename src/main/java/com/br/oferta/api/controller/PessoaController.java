///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.br.oferta.api.controller;
//
//import com.br.oferta.api.model.Curso;
//import com.br.oferta.api.model.Pessoa;
//import com.br.oferta.api.service.CursoService;
//import com.br.oferta.api.service.MatriculaService;
//import com.br.oferta.api.service.PessoaService;
//import com.br.oferta.api.service.TurmaService;
//import com.br.oferta.api.util.exception.CpfCnpjClienteJaCadastradoException;
//import com.br.oferta.api.util.filter.PessoaFilter;
//import com.br.oferta.api.util.page.PageWrapper;
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
///**
// *
// * @author frc
// */
//@Controller
//@RequestMapping("/pessoas")
//public class PessoaController {
//
//    @Autowired
//    private PessoaService pessoaService;
//
//    @Autowired
//    private CursoService cursoService;
//
//    @Autowired
//    private MatriculaService matriculaService;
//
//    @Autowired
//    private TurmaService turmaService;
//
//    @GetMapping
//    public ModelAndView pesquisarByFilter(PessoaFilter filter, BindingResult result, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
//        ModelAndView mv = new ModelAndView("paginas/pessoa/table");
//        mv.addObject("pessoas", pessoaService.findBySort());
//        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pessoaService.filtrar(filter, pageable), httpServletRequest);
//        mv.addObject("pagina", paginaWrapper);
//        return mv;
//    }
//
//    @GetMapping("/turma/{id}")
//    public ModelAndView findByTurmaById(@PathVariable("id") Long id) {
//        ModelAndView mv = new ModelAndView("paginas/turma/turma_aluno");
//        mv.addObject("alunos", pessoaService.findByTurmaById(id));
//        return mv;
//    }
//
//    @RequestMapping("/nova")
//    public ModelAndView nova(Pessoa pessoa) {
//        ModelAndView mv = new ModelAndView("paginas/pessoa/create");
//        return mv;
//    }
//
//    @RequestMapping("/confirmacao")
//    public ModelAndView confirmacao(Pessoa pessoa) {
//        ModelAndView mv = new ModelAndView("paginas/pessoa/confirmacao");
//        return mv;
//    }
//
//    @RequestMapping(value = {"/nova"}, method = RequestMethod.POST)
//    public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult result, Model model, RedirectAttributes attributes) {
//        if (result.hasErrors()) {
//            return nova(pessoa);
//        }
//
//        System.out.println("Nome: " + pessoa.getNome());
//        System.out.println("Cpf: " + pessoa.getCpf());
//        System.out.println("Foto: " + pessoa.getFoto());
//        System.out.println("Data Nascimento: " + pessoa.getDataNascimento());
//        System.out.println("Data Registro: " + pessoa.getDataRegistro());
//        System.out.println("Telefone: " + pessoa.getTelefone());
//        System.out.println("Ativo: " + pessoa.isAtivo());
//        System.out.println("Email: " + pessoa.getUsuario().getEmail());
//        System.out.println("Senha: " + pessoa.getUsuario().getSenha());
//        System.out.println("ConfirmaSenha: " + pessoa.getUsuario().getConfirmaSenha());
//        pessoaService.create(pessoa);
//
////        } catch (EmailUsuarioJaCadastradoException e) {
////            result.rejectValue("usuario.email", e.getMessage(), e.getMessage());
////            return nova(pessoa);
////        } catch (SenhaObrigatoriaUsuarioException e) {
////            result.rejectValue("usuario.senha", e.getMessage(), e.getMessage());
////            return nova(pessoa);
////        }
//        attributes.addFlashAttribute("mensagem", "Pessoa salva com sucesso!");
//        return new ModelAndView("redirect:/pessoas/confirmacao");
//    }
//
//    @RequestMapping(value = "/inscricao", method = RequestMethod.POST)
//    public ModelAndView inscricao(Curso curso, @Valid Pessoa pessoa, BindingResult result, Model model, RedirectAttributes attributes) {
//
//        if (result.hasErrors()) {
//            return novaInscricao(curso, pessoa);
//        } else {
//            if (pessoa.getId() == null) {
//                try {
//                    System.out.println("Nome: " + pessoa.getNome());
//                    System.out.println("Cpf: " + pessoa.getCpf());
//                    System.out.println("Foto: " + pessoa.getFoto());
//                    System.out.println("Data Nascimento: " + pessoa.getDataNascimento());
//                    System.out.println("Data Registro: " + pessoa.getDataRegistro());
//                    System.out.println("Telefone: " + pessoa.getTelefone());
//                    System.out.println("Ativo: " + pessoa.isAtivo());
//                    System.out.println("Email: " + pessoa.getUsuario().getEmail());
//                    System.out.println("Senha: " + pessoa.getUsuario().getSenha());
//                    System.out.println("ConfirmaSenha: " + pessoa.getUsuario().getConfirmaSenha());
//                    pessoaService.create(pessoa);
//                } catch (CpfCnpjClienteJaCadastradoException e) {
//                    result.rejectValue("cpf", e.getMessage(), e.getMessage());
//                    return novaInscricao(curso, pessoa);
//                }
//                attributes.addFlashAttribute("mensagem", "Pessoa salva com sucesso!");
//            } else {
//                System.out.println("Nome: " + pessoa.getNome());
//                System.out.println("Cpf: " + pessoa.getCpf());
//                System.out.println("Foto: " + pessoa.getFoto());
//                System.out.println("Data Nascimento: " + pessoa.getDataNascimento());
//                System.out.println("Data Registro: " + pessoa.getDataRegistro());
//                System.out.println("Telefone: " + pessoa.getTelefone());
//                System.out.println("Ativo: " + pessoa.isAtivo());
//                System.out.println("Email: " + pessoa.getUsuario().getEmail());
//                System.out.println("Senha: " + pessoa.getUsuario().getSenha());
//                System.out.println("ConfirmaSenha: " + pessoa.getUsuario().getConfirmaSenha());
//                pessoaService.update(pessoa.getId(), pessoa);
//                attributes.addFlashAttribute("mensagem", "Pessoa atualizada com sucesso!");
//            }
//        }
//        return new ModelAndView("redirect:/pessoas/confirmacao");
//    }
//
//    @GetMapping("/{id}")
//    public ModelAndView editar(@PathVariable("id") Pessoa p) {
//        Pessoa pessoa = pessoaService.findById(p.getId()).get();
//        ModelAndView mv = nova(pessoa);
//        mv.addObject(pessoa);
//        return mv;
//    }
//
//    @GetMapping("/inscricao/{id}")
//    public ModelAndView novaInscricao(@PathVariable("id") Curso curso, Pessoa pessoa) {
//        ModelAndView mv = new ModelAndView("paginas/pessoa/inscricao");
//        curso = cursoService.findById(curso.getId()).get();
//        mv.addObject(pessoa);
//        mv.addObject("curso", curso);
//        return mv;
//    }
//
//}
