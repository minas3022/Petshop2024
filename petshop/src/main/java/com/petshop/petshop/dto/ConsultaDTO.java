package com.petshop.petshop.dto;

import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.model.Pet;
import com.petshop.petshop.model.Servico;

import java.time.LocalDateTime;

public class ConsultaDTO {
    private Cliente cliente;
    private Pet pet;
    private Servico servico;
    private LocalDateTime dataHora;

    public ConsultaDTO() {
    }

    public ConsultaDTO(Cliente cliente, Pet pet, Servico servico, LocalDateTime dataHora) {
        this.cliente = cliente;
        this.pet = pet;
        this.servico = servico;
        this.dataHora = dataHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
