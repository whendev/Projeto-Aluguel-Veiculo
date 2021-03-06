package com.ufpb.projetoAluguelVeiculo.entities;

import com.google.gson.Gson;

public class Veiculo {
    private String id;
    private ModeloVeiculo modeloVeiculo;

    public Veiculo(String id, ModeloVeiculo modeloVeiculo) {
        this.id = id;
        this.modeloVeiculo = modeloVeiculo;
    }

    public ModeloVeiculo getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(ModeloVeiculo modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Veiculo veiculo() {
        return this;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}