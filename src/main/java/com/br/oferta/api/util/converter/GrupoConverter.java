package com.br.oferta.api.util.converter;

import com.br.oferta.api.model.Grupo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
public class GrupoConverter implements Converter<String, Grupo> {

    @Override
    public Grupo convert(String id) {
        if (!StringUtils.isEmpty(id)) {
            Grupo grupo = new Grupo();
            grupo.setId(Long.valueOf(id));
            return grupo;
        }

        return null;
    }

}
