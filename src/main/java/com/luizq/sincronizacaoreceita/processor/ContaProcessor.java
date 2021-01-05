package com.luizq.sincronizacaoreceita.processor;

import com.luizq.sincronizacaoreceita.model.ContaCorrente;
import com.luizq.sincronizacaoreceita.service.ReceitaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ContaProcessor implements ItemProcessor<ContaCorrente,ContaCorrente>{
    boolean resultado;

    private static final Logger log = LoggerFactory.getLogger(ContaCorrente.class);

    @Override
    public ContaCorrente process(final ContaCorrente contaCorrente) throws Exception {
        final String agencia = contaCorrente.getAgencia();
        final String conta = contaCorrente.getConta();
        final String saldo = contaCorrente.getSaldo();
        final String status = contaCorrente.getStatus();

        String contaFormated = conta.replace("-","");
        double doubleSaldo = Double.parseDouble(saldo.replace(",","."));
   
        ReceitaService rService = new ReceitaService();
        if(rService.atualizaConta(agencia, contaFormated, doubleSaldo, status)){
            log.info(contaCorrente +"=> Enviado com Sucesso");
        } else {
            log.info(contaCorrente + "=> Enviado fora do formato");
        }

        return contaCorrente;
    }
}
