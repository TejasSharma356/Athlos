package com.leaderboard.service;

import com.leaderboard.config.RedisConfig;
import com.leaderboard.model.Game;
import com.leaderboard.repository.GameRepository;

import java.util.List;

public class GameService {
    private final GameRepository gameRepository;

    public GameService(RedisConfig config) {
        this.gameRepository = new GameRepository(config);
    }

    public Game createGame(String name, String description) { return gameRepository.create(name, description); }

    public void createGameIfAbsent(Game game) {
        if (gameRepository.findByName(game.getName()) == null) {
            gameRepository.create(game.getName(), game.getDescription());
        }
    }

    public List<Game> getAllGames() { return gameRepository.findAll(); }

    public Game findByName(String name) { return gameRepository.findByName(name); }

    public void deleteGameByName(String name) { gameRepository.deleteByName(name); }
}