package com.luizq.sincronizacaoreceita.processor;

import com.luizq.sincronizacaoreceita.model.ContaCorrente;
import com.luizq.sincronizacaoreceita.service.ReceitaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author Luiz Quirino
 * DESCRIÇAO DA CLASSE:
 *  - SincronizacaoReceitaApplication.java é a classe processadora utilizada por ConfiguraBatch para:
 *      - conversão de dados
 *      - Enviar dados para atualizaConta ReceitaService.java
 *      - Retorna o resultado e campos preenchidos para o ItemWriter da classe ConfiguraBatch.java
 */
public class ContaProcessor implements ItemProcessor<ContaCorrente, ContaCorrente> {
    boolean resultado;

    private static final Logger log = LoggerFactory.getLogger(ContaCorrente.class);

    @Override
    public ContaCorrente process(final ContaCorrente contaCorrente) throws Exception {
        final String agencia = contaCorrente.getAgencia();
        final String conta = contaCorrente.getConta();
        final String saldo = contaCorrente.getSaldo();
        final String status = contaCorrente.getStatus();

        //Conversão dos dados recebidos do ".csv" para ser processado por ReceitaService.
        String contaFormated = conta.replace("-","");
        double doubleSaldo = Double.parseDouble(saldo.replace(",","."));
        
        //Execução do atualiza conta com os dados convertidos para o tipo correto.
        try{
            ReceitaService rService = new ReceitaService();
            if(rService.atualizaConta(agencia, contaFormated, doubleSaldo, status)){
                log.info(contaCorrente +" RESULTADO => Enviado com Sucesso"); //TODO: Implementar habilitar/desabilitar o visualização de log no terminal
                return new ContaCorrente(agencia,conta,saldo,status,"Sucesso");
            } else {
                log.info("[WARNING] " + contaCorrente + " RESULTADO => Enviado fora do formato");
                return new ContaCorrente(agencia,conta,saldo,status,"Erro de Formato");
            }
        }catch(RuntimeException e){
            return new ContaCorrente(agencia,conta,saldo,status,"Erro de Formato");
        }
    }
}
