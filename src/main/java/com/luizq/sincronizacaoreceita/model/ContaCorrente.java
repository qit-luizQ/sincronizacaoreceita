package com.luizq.sincronizacaoreceita.model;

/**
 * @author Luiz Quirino
 * DESCRIÇÃO DA CLASSE
 *  - ContaCorrente.java define a estrutura dos campos da Conta Conrrente utilizados na manipulação dos dados
 *    pela classe ContaProcessor.
 */

public class ContaCorrente {
    private String agencia;
    private String conta;
    private String saldo;
    private String status;
    private String resultado;

    public ContaCorrente() {
    }

    public ContaCorrente(String agencia, String conta, String saldo, String status, String resultado) {
        this.agencia = agencia;
        this.conta = conta;
        this.saldo = saldo;
        this.status = status;
        this.resultado = resultado;
    
    }

    public String getAgencia(){
        return agencia;
    }

    public void setAgencia(String agencia){
        this.agencia = agencia;
    }

    public String getConta(){
        return conta;
    }

    public void setConta(String conta){
        this.conta = conta;
    }

    public String getSaldo(){
        return saldo;
    }

    public void setSaldo(String saldo){
        this.saldo = saldo;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String toString(){
        return "AGENCIA: " +agencia+", CONTA: "+conta+", SALDO: "+saldo+", STATUS: "+status;
    }

}
