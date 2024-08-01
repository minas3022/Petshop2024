package com.petshop.petshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.petshop.petshop.model.Pet;
import com.petshop.petshop.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PetServiceTest {

    @Mock
    private PetRepository repository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Pet pet1 = new Pet();
        pet1.setId(1l);
        pet1.setNome("Toto");
        pet1.setEspecie("normal");
        pet1.setRaca("Pitbull");
        pet1.setIdade(1);

        Pet pet2 = new Pet();
        pet2.setId(1l);
        pet2.setNome("Toto");
        pet2.setEspecie("normal");
        pet2.setRaca("Pitbull");
        pet2.setIdade(1);
        when(repository.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        List<Pet> pets = petService.getAll();

        assertEquals(2, pets.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        Pet pet = new Pet();
        pet.setId(1l);
        pet.setNome("Toto");
        pet.setEspecie("normal");
        pet.setRaca("Pitbull");
        pet.setIdade(1);
        when(repository.findById(1L)).thenReturn(Optional.of(pet));

        Optional<Pet> foundPet = petService.getById(1L);

        assertTrue(foundPet.isPresent());
        assertEquals("Toto", foundPet.get().getNome());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testCreate() {
        Pet pet = new Pet();
        pet.setId(1l);
        pet.setNome("Rex");
        pet.setEspecie("normal");
        pet.setRaca("Pitbull");
        pet.setIdade(1);
        when(repository.save(pet)).thenReturn(pet);

        Pet createdPet = petService.create(pet);

        assertNotNull(createdPet.getId());
        assertEquals("Rex", createdPet.getNome());
        verify(repository, times(1)).save(pet);
    }

    @Test
    void testUpdate() {
        Pet existingPet = new Pet();
        existingPet.setId(1l);
        existingPet.setNome("Rex");
        existingPet.setEspecie("normal");
        existingPet.setRaca("Pitbull");
        existingPet.setIdade(1);
        Pet updatedPet = new Pet();
        updatedPet.setId(1l);
        updatedPet.setNome("Rex");
        updatedPet.setEspecie("normal");
        updatedPet.setRaca("Pitbull");
        updatedPet.setIdade(1);

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(updatedPet)).thenReturn(updatedPet);

        Optional<Pet> result = petService.update(1L, updatedPet);

        assertTrue(result.isPresent());
        assertEquals("Rex", result.get().getNome());
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).save(updatedPet);
    }

    @Test
    void testUpdatePetNotFound() {
        Pet updatedPet = new Pet();
        updatedPet.setId(1l);
        updatedPet.setNome("Toto");
        updatedPet.setEspecie("normal");
        updatedPet.setRaca("Pitbull");
        updatedPet.setIdade(1);
        when(repository.existsById(1L)).thenReturn(false);

        Optional<Pet> result = petService.update(1L, updatedPet);

        assertFalse(result.isPresent());
        verify(repository, times(1)).existsById(1L);
        verify(repository, never()).save(any(Pet.class));
    }

    @Test
    void testDelete() {
        Long petId = 1L;

        petService.delete(petId);

        verify(repository, times(1)).deleteById(petId);
    }
}
