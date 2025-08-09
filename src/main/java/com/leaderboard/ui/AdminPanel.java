package com.leaderboard.ui;

import com.leaderboard.model.Game;
import com.leaderboard.model.User;
import com.leaderboard.service.AuthService;
import com.leaderboard.service.GameService;
import com.leaderboard.service.ReportService;
import com.leaderboard.service.ScoreService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AdminPanel extends JPanel {
    private final MainFrame frame;
    private final GameService gameService;
    private final ScoreService scoreService;
    private final AuthService authService;

    private JTable gameTable;

    public AdminPanel(MainFrame frame, GameService gameService, ScoreService scoreService, AuthService authService) {
        this.frame = frame;
        this.gameService = gameService;
        this.scoreService = scoreService;
        this.authService = authService;
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Games", createGamesTab());
        tabs.addTab("Reports", createReportsTab());

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> frame.showCard("mainMenu"));

        add(tabs, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }

    private JPanel createGamesTab() {
        JPanel panel = new JPanel(new BorderLayout(8,8));
        String[] cols = {"Name", "Description"};
        gameTable = new JTable(new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
        refreshGamesTable();

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(12);
        JTextField descField = new JTextField(20);
        JButton addBtn = new JButton("Add Game");
        JButton delBtn = new JButton("Delete Selected");
        controls.add(new JLabel("Name:"));
        controls.add(nameField);
        controls.add(new JLabel("Description:"));
        controls.add(descField);
        controls.add(addBtn);
        controls.add(delBtn);

        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            String desc = descField.getText();
            if (name.isBlank()) {
                JOptionPane.showMessageDialog(this, "Name required");
                return;
            }
            try {
                gameService.createGame(name, desc);
                refreshGamesTable();
                nameField.setText("");
                descField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        delBtn.addActionListener(e -> {
            int row = gameTable.getSelectedRow();
            if (row < 0) return;
            String name = (String) gameTable.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete game '" + name + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                gameService.deleteGameByName(name);
                refreshGamesTable();
            }
        });

        panel.add(controls, BorderLayout.NORTH);
        panel.add(new JScrollPane(gameTable), BorderLayout.CENTER);
        return panel;
    }

    private void refreshGamesTable() {
        DefaultTableModel model = (DefaultTableModel) gameTable.getModel();
        model.setRowCount(0);
        for (Game g : gameService.getAllGames()) {
            model.addRow(new Object[]{g.getName(), g.getDescription()});
        }
    }

    private JPanel createReportsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Game> gameBox = new JComboBox<>();
        for (Game g : gameService.getAllGames()) gameBox.addItem(g);
        JTextField fromDate = new JTextField(10);
        JTextField toDate = new JTextField(10);
        fromDate.setText(LocalDate.now().minusDays(7).toString());
        toDate.setText(LocalDate.now().toString());
        JButton exportCsv = new JButton("Export CSV");
        JButton exportTxt = new JButton("Export TXT");

        gbc.gridx=0; gbc.gridy=0; panel.add(new JLabel("Game:"), gbc);
        gbc.gridx=1; panel.add(gameBox, gbc);
        gbc.gridx=0; gbc.gridy=1; panel.add(new JLabel("From (yyyy-MM-dd):"), gbc);
        gbc.gridx=1; panel.add(fromDate, gbc);
        gbc.gridx=0; gbc.gridy=2; panel.add(new JLabel("To (yyyy-MM-dd):"), gbc);
        gbc.gridx=1; panel.add(toDate, gbc);
        gbc.gridx=0; gbc.gridy=3; panel.add(exportCsv, gbc);
        gbc.gridx=1; panel.add(exportTxt, gbc);

        ReportService reportService = new ReportService(scoreService, authService);

        exportCsv.addActionListener(e -> doExport(gameBox, fromDate, toDate, reportService, true));
        exportTxt.addActionListener(e -> doExport(gameBox, fromDate, toDate, reportService, false));
        return panel;
    }

    private void doExport(JComboBox<Game> gameBox, JTextField fromDate, JTextField toDate, ReportService reportService, boolean csv) {
        User user = frame.getCurrentUser();
        if (user == null || !user.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Admin only.");
            return;
        }
        Game game = (Game) gameBox.getSelectedItem();
        if (game == null) {
            JOptionPane.showMessageDialog(this, "Select a game");
            return;
        }
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            long fromTs = LocalDate.parse(fromDate.getText(), df).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long toTs = LocalDate.parse(toDate.getText(), df).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(csv ? "top_players_" + game.getName() + ".csv" : "top_players_" + game.getName() + ".txt"));
            int res = chooser.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (csv) {
                    reportService.exportToCsv(game.getName(), fromTs, toTs, 100, file);
                } else {
                    reportService.exportToTxt(game.getName(), fromTs, toTs, 100, file);
                }
                JOptionPane.showMessageDialog(this, "Exported: " + file.getAbsolutePath());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}