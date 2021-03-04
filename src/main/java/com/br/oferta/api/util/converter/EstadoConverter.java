package com.br.oferta.api.util.converter;

import com.br.oferta.api.model.Estado;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class EstadoConverter implements Converter<String, Estado> {

    @Override
    public Estado convert(String id) {
        if (!StringUtils.isEmpty(id)) {
            Estado estado = new Estado();
            estado.setId(Long.valueOf(id));
            return estado;
        }

        return null;
    }

}
