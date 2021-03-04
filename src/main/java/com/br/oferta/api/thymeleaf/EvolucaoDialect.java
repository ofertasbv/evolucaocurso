package com.br.oferta.api.thymeleaf;

import com.br.oferta.api.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import com.br.oferta.api.thymeleaf.processor.MenuAttributeTagProcessor;
import com.br.oferta.api.thymeleaf.processor.MessageElementTagProcessor;
import com.br.oferta.api.thymeleaf.processor.OrderElementTagProcessor;
import com.br.oferta.api.thymeleaf.processor.PaginationElementTagProcessor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

@Component
public class EvolucaoDialect extends AbstractProcessorDialect {

    public EvolucaoDialect() {
        super("Gdados evolucao", "evolucao", StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processadores = new HashSet<>();
        processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
        processadores.add(new MessageElementTagProcessor(dialectPrefix));
        processadores.add(new OrderElementTagProcessor(dialectPrefix));
        processadores.add(new PaginationElementTagProcessor(dialectPrefix));
        processadores.add(new MenuAttributeTagProcessor(dialectPrefix));
        return processadores;
    }

}
