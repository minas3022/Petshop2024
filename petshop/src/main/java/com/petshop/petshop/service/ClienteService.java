
package com.petshop.petshop.service;


import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

        @Autowired
        private ClienteRepository clienteRepository;

        public List<Cliente> getAll() {
            return clienteRepository.findAll();
        }

        public Optional<Cliente> getById(Long id) {
            return clienteRepository.findById(id);
        }

        public Cliente create(Cliente cliente) {
            return clienteRepository.save(cliente);
        }

        public Optional<Cliente> update(Long id, Cliente cliente) {
            if (!clienteRepository.existsById(id)) {
                return Optional.empty();
            }
            cliente.setId(id);
            Cliente updatedCliente = clienteRepository.save(cliente);
            return Optional.of(updatedCliente);
        }

        public void delete(Long id) {
            clienteRepository.deleteById(id);
        }

        public List<Cliente> searchByNome(String nome) {
            return clienteRepository.findByNomeContainingIgnoreCase(nome);
        }
                //mostrar todos clientes(carlos)
                public List<Cliente> findAll() {
                    return clienteRepository.findAll();
                }
                //pesquisar clientes (carlos)
        
}
