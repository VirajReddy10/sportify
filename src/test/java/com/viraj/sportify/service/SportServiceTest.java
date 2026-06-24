package com.viraj.sportify.service;

import com.viraj.sportify.dto.SportRequest;
import com.viraj.sportify.dto.SportResponse;
import com.viraj.sportify.model.Sport;
import com.viraj.sportify.repository.SportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SportServiceTest {

    @Mock
    private SportRepository sportRepository;

    @InjectMocks
    private SportService sportService;

    @Test
    void findAll_returnsAllSportsMappedToResponses() {
        Sport basketball = new Sport("Basketball");
        Sport soccer = new Sport("Soccer");
        when(sportRepository.findAll()).thenReturn(List.of(basketball, soccer));

        List<SportResponse> result = sportService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Basketball");
        assertThat(result.get(1).name()).isEqualTo("Soccer");
    }

    @Test
    void findById_whenSportExists_returnsIt() {
        Sport basketball = new Sport("Basketball");
        when(sportRepository.findById(1L)).thenReturn(Optional.of(basketball));

        SportResponse result = sportService.findById(1L);

        assertThat(result.name()).isEqualTo("Basketball");
    }

    @Test
    void findById_whenSportDoesNotExist_throwsException() {
        when(sportRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> sportService.findById(999L));
    }

    @Test
    void create_savesAndReturnsNewSport() {
        SportRequest request = new SportRequest("Hockey");

        SportResponse result = sportService.create(request);

        assertThat(result.name()).isEqualTo("Hockey");
        verify(sportRepository).save(org.mockito.ArgumentMatchers.any(Sport.class));
    }
}
