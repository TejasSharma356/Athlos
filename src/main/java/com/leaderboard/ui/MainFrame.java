package com.leaderboard.ui;

import com.leaderboard.model.User;
import com.leaderboard.service.AuthService;
import com.leaderboard.service.GameService;
import com.leaderboard.service.ScoreService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private final AuthService authService;
    private final GameService gameService;
    private final ScoreService scoreService;

    private User currentUser;

    public MainFrame(AuthService authService, GameService gameService, ScoreService scoreService) {
        super("Real-Time Leaderboard System");
        this.authService = authService;
        this.gameService = gameService;
        this.scoreService = scoreService;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);

        initUi();
    }

    private void initUi() {
        LoginPanel loginPanel = new LoginPanel(this, authService);
        RegisterPanel registerPanel = new RegisterPanel(this, authService);
        MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
        SubmitScorePanel submitScorePanel = new SubmitScorePanel(this, gameService, scoreService);
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel(this, gameService, scoreService);
        MyRankPanel myRankPanel = new MyRankPanel(this, gameService, scoreService);
        AdminPanel adminPanel = new AdminPanel(this, gameService, scoreService, authService);

        cards.add(loginPanel, "login");
        cards.add(registerPanel, "register");
        cards.add(mainMenuPanel, "mainMenu");
        cards.add(submitScorePanel, "submitScore");
        cards.add(leaderboardPanel, "leaderboard");
        cards.add(myRankPanel, "myRank");
        cards.add(adminPanel, "admin");

        setContentPane(cards);
        showCard("login");
    }

    public void showCard(String name) {
        cardLayout.show(cards, name);
    }

    public void onLoginSuccess(User user) {
        this.currentUser = user;
        showCard("mainMenu");
    }

    public void logout() {
        this.currentUser = null;
        showCard("login");
    }

    public User getCurrentUser() { return currentUser; }
}