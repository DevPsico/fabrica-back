package com.estacionamento.estacionamento.models;

import java.math.BigDecimal;

public enum VacancyType {

    COMUM("C", 10.0),
    VIP("V", 20.0),
    DEFICIENTE("D", 15.0),
    IDOSO("I", 12.0);

    private final String prefixo;
    private final BigDecimal valorPorHora; // Novo atributo para armazenar o preço

    VacancyType(String prefixo, double valorPorHora) {
        this.prefixo = prefixo;
        this.valorPorHora = BigDecimal.valueOf(valorPorHora);
    }
    
    public String getPrefixo() {
        return prefixo;
    }

    public BigDecimal getValorPorHora() {
        return valorPorHora;
    }
}