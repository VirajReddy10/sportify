package com.viraj.sportify.service;

import org.springframework.test.util.ReflectionTestUtils;
import com.viraj.sportify.dto.GameRequest;
import com.viraj.sportify.model.Sport;
import com.viraj.sportify.model.Team;
import com.viraj.sportify.repository.GameRepository;
import com.viraj.sportify.repository.SportRepository;
import com.viraj.sportify.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private SportRepository sportRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void create_whenHomeAndAwayTeamAreTheSame_throwsException() {
        Sport sport = new Sport("Basketball");
        Team team = new Team("Lakers", sport);
        ReflectionTestUtils.setField(team, "id", 1L);

        lenient().when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        GameRequest request = new GameRequest(1L, 1L, 1L, LocalDateTime.now());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gameService.create(request)
        );

        assertThat(exception.getMessage()).isEqualTo("Home team and away team cannot be the same");
    }

    @Test
    void create_whenTeamsAreDifferent_succeeds() {
        Sport sport = new Sport("Basketball");
        Team homeTeam = new Team("Lakers", sport);
        Team awayTeam = new Team("Celtics", sport);
        ReflectionTestUtils.setField(homeTeam, "id", 1L);
        ReflectionTestUtils.setField(awayTeam, "id", 2L);

        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(awayTeam));

        GameRequest request = new GameRequest(1L, 1L, 2L, LocalDateTime.now());

        var result = gameService.create(request);

        assertThat(result.homeTeamName()).isEqualTo("Lakers");
        assertThat(result.awayTeamName()).isEqualTo("Celtics");
    }
}
