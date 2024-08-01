package com.petshop.petshop.service;

import com.petshop.petshop.dto.ConsultaDTO;
import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.model.Pet;
import com.petshop.petshop.model.Servico;
import com.petshop.petshop.repository.AgendamentoRepository;
import com.petshop.petshop.repository.ClienteRepository;
import com.petshop.petshop.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<ConsultaDTO> realizarConsulta() {
        List<Object[]> resultados = agendamentoRepository.consultaJoinClientesPetsServicos();

        List<ConsultaDTO> consultas = new ArrayList<>();
        for (Object[] resultado : resultados) {
            Cliente cliente = (Cliente) resultado[0];
            Pet pet = (Pet) resultado[1];
            Servico servico = (Servico) resultado[2];
            LocalDateTime dataHora = (LocalDateTime) resultado[3];

            ConsultaDTO consulta = new ConsultaDTO(cliente, pet, servico, dataHora);
            consultas.add(consulta);
        }

        return consultas;
    }
}
