package com.br.oferta.api.util.converter;

import com.br.oferta.api.model.Categoria;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CategoriaConverter implements Converter<String, Categoria> {

    @Override
    public Categoria convert(String id) {
        if (!StringUtils.isEmpty(id)) {
            Categoria categoria = new Categoria();
            categoria.setId(Long.valueOf(id));
            return categoria;
        }

        return null;
    }

}
