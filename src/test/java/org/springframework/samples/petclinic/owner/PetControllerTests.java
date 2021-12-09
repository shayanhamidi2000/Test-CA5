package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
public class PetControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitRepository visitRepository;

    @Test
    public void addNewPetScenario() throws Exception {
        // Arrange
        PetService mockedPetService = mock(PetService.class);
        PetRepository mockedPetRepository = mock(PetRepository.class);
        OwnerRepository mockedOwnerRepository = mock(OwnerRepository.class);
        PetController sut = new PetController(mockedPetRepository, mockedOwnerRepository, mockedPetService);
        List<PetType> petTypes = new ArrayList<>();
        PetType petType = new PetType(); petType.setName("Cat"); petTypes.add(petType);
        when(mockedPetRepository.findPetTypes()).thenReturn(petTypes);
        Owner mockedOwner = mock(Owner.class);
        Pet pet = new Pet();
        pet.setOwner(mockedOwner);
        pet.setType(petType);
        pet.setName("Dude");
        when(mockedOwner.getPet("Dude", true)).thenReturn(pet);
        when(mockedPetService.findOwner(1)).thenReturn(mockedOwner);
        when(mockedPetService.newPet(mockedOwner)).thenReturn(pet);
        // Act
        this.mockMvc.perform(get("/pets/new")).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/pets/new")).andDo(print()).andExpect(status().isOk());

        // Assert
        verify(mockedPetRepository.findPetTypes(), times(1));
        verify(mockedOwner.getPet("Dude", true), times(1));
        verify(mockedPetService.findOwner(1), times(1));
        verify(mockedPetService.newPet(mockedOwner), times(1));
    }

}
