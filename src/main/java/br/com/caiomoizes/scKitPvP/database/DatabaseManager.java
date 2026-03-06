package br.com.caiomoizes.scKitPvP.database;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import br.com.caiomoizes.scKitPvP.player.PlayerData;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager {
    private Connection connection;

    public void init() {
        try {
            File dataFolder = new File(SCKitPvP.getInstance().getDataFolder(), "database.db");
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);

            createTable();
        } catch (Exception e) {
            SCKitPvP.getInstance().getLogger().log(Level.SEVERE, "Erro ao conectar ao SQLite!", e);
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "name VARCHAR(16)," +
                "kills INT DEFAULT 0," +
                "deaths INT DEFAULT 0," +
                "coins INT DEFAULT 0," +
                "max_streak INT DEFAULT 0" +
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------ SALVAR DADOS ------
    public void savePlayerData(PlayerData data) {
        String sql = "REPLACE INTO players (uuid, name, kills, deaths, coins, max_streak) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, data.getUuid().toString());
            pstmt.setString(2, data.getName());
            pstmt.setInt(3, data.getKills());
            pstmt.setInt(4, data.getDeaths());
            pstmt.setInt(5, data.getCoins());
            pstmt.setInt(6, data.getMaxKillstreak());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------ CARREGAR DADOS ------
    public void loadPlayerData(PlayerData data) {
        String sql = "SELECT * FROM players WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, data.getUuid().toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                data.setKills(rs.getInt("kills"));
                data.setDeaths(rs.getInt("deaths"));
                data.setCoins(rs.getInt("coins"));
                data.setMaxKillstreak(rs.getInt("max_streak"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------ Obter TOP Kills ------
    public List<PlayerData> getTopKills(int limit) {
        List<PlayerData> topList = new ArrayList<>();
        // Removida a vírgula extra após 'kills'
        String sql = "SELECT * FROM players ORDER BY kills DESC LIMIT ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // 1. Pegamos o UUID e o Nome para criar o objeto
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String name = rs.getString("name");

                PlayerData data = new PlayerData(uuid, name);

                // 2. Preenchemos as estatísticas guardadas no banco
                data.setKills(rs.getInt("kills"));
                data.setDeaths(rs.getInt("deaths"));
                data.setCoins(rs.getInt("coins"));
                data.setMaxKillstreak(rs.getInt("max_streak"));

                topList.add(data);
            }
        } catch (SQLException e) {
            SCKitPvP.getInstance().getLogger().severe("Erro ao carregar TOP Kills: " + e.getMessage());
        }

        return topList;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
