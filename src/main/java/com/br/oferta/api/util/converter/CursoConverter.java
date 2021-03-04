package com.br.oferta.api.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.br.oferta.api.model.Curso;

@Component
public class CursoConverter implements Converter<String, Curso> {

    @Override
    public Curso convert(String id) {
        if (!StringUtils.isEmpty(id)) {
            Curso curso = new Curso();
            curso.setId(Long.valueOf(id));
            return curso;
        }

        return null;
    }

}
