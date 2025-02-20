package com.estacionamento.estacionamento.models;

public enum VacancyType {

    COMUM("C", 10.0),
    VIP("V", 20.0),
    DEFICIENTE("D", 15.0),
    IDOSO("I", 12.0);

    private final String prefixo;
    private final double valorPorHora; // Novo atributo para armazenar o preço

    VacancyType(String prefixo, double valorPorHora) {
        this.prefixo = prefixo;
        this.valorPorHora = valorPorHora;
    }
    
    public String getPrefixo() {
        return prefixo;
    }

    public double getValorPorHora() {
        return valorPorHora;
    }
}