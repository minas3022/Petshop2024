package com.petshop.petshop.service;

import com.petshop.petshop.model.Pet;
import com.petshop.petshop.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PetService{

    @Autowired
    private PetRepository repository;


    public List<Pet> getAll() {
        return repository.findAll();
    }

    public Optional<Pet> getById(Long id) {
        return repository.findById(id);
    }

    public Pet create(Pet pet) {
        return repository.save(pet);
    }

    public Optional<Pet> update(Long id, Pet pet) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        pet.setId(id);
       Pet updatedPet = repository.save(pet);
        return Optional.of(updatedPet);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
