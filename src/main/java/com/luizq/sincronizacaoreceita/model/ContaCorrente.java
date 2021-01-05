package com.luizq.sincronizacaoreceita.model;

public class ContaCorrente {
    private String agencia;
    private String conta;
    private String saldo;
    private String status;

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

    public String toString(){
        return "AGENCIA: " +agencia+", CONTA: "+conta+", SALDO: "+saldo+", STATUS: "+status;
    }

}
