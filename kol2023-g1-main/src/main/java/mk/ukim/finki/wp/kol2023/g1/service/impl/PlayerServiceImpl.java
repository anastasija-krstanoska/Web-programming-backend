package mk.ukim.finki.wp.kol2023.g1.service.impl;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.Team;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidPlayerIdException;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidTeamIdException;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.repository.TeamRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Player> listAllPlayers() {
        return this.playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return this.playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Team team1 = this.teamRepository.findById(team).orElseThrow(InvalidTeamIdException::new);
        return this.playerRepository.save(new Player(name, bio, pointsPerGame, position, team1));
    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Team team1 = this.teamRepository.findById(team).orElseThrow(InvalidTeamIdException::new);
        Player player = this.playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
        player.setName(name);
        player.setBio(bio);
        player.setPointsPerGame(pointsPerGame);
        player.setPosition(position);
        player.setTeam(team1);

        return this.playerRepository.save(player);
    }

    @Override
    public Player delete(Long id) {
        Player player = this.playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
        this.playerRepository.delete(player);
        return player;
    }

    @Override
    public Player vote(Long id) {
        Player player = this.playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
        if (player != null) {
            player.setVotes(player.getVotes() + 1);
            this.playerRepository.save(player);
        }
        return player;
    }

    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {
        List<Player> players;

        if (pointsPerGame == null && position == null) {
            players = this.playerRepository.findAll();
        } else if (pointsPerGame == null) {
            players = this.playerRepository.findPlayerByPosition(position);
        } else if (position == null) {
            players = this.playerRepository.findPlayersByPointsPerGameLessThan(pointsPerGame);
        } else players = this.playerRepository.findPlayersByPointsPerGameLessThanAndPosition(pointsPerGame, position);


        return players;
    }
}
