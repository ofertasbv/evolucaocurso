package com.br.oferta.api.util.converter;

import com.br.oferta.api.model.Cidade;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CidadeConverter implements Converter<String, Cidade> {

    @Override
    public Cidade convert(String id) {
        if (!StringUtils.isEmpty(id)) {
            Cidade cidade = new Cidade();
            cidade.setId(Long.valueOf(id));
            return cidade;
        }

        return null;
    }

}
