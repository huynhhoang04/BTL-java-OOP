package controller;

import model.GameMap;
import model.Level;
import model.Tower;
import model.Wave;
import model.EnemyType;

import java.sql.*;
import java.util.*;

public class GamePlayController {

    private final Connection conn;

    public GamePlayController(Connection conn) {
        this.conn = conn;
    }

    public Level loadLevel(int levelId) throws Exception {
        String sql = """
            SELECT id, name, map_id, difficulty, base_hp_override,
                   start_gold, description
            FROM levels WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, levelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new Exception("Level not found: " + levelId);

                return new Level(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("map_id"),
                        rs.getInt("difficulty"),
                        (Integer) rs.getObject("base_hp_override"),
                        rs.getInt("start_gold"),
                        rs.getString("description")
                );
            }
        }
    }

    public GameMap loadMap(int mapId) throws Exception {
        String sql = """
            SELECT id, name, background_url, base_hp_default,
                   path_points, tower_slots
            FROM maps WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mapId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new Exception("Map not found: " + mapId);

                return new GameMap(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("background_url"),
                        rs.getInt("base_hp_default"),
                        rs.getString("path_points"),
                        rs.getString("tower_slots")
                );
            }
        }
    }

    public List<Wave> loadWaves(int levelId) throws Exception {
        List<Wave> list = new ArrayList<>();

        String sql = """
            SELECT id, level_id, order_index, start_time,
                   enemy_type_id, enemy_count, spawn_interval
            FROM waves
            WHERE level_id = ? ORDER BY order_index
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, levelId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Wave(
                            rs.getInt("id"),
                            rs.getInt("level_id"),
                            rs.getInt("order_index"),
                            rs.getDouble("start_time"),
                            rs.getInt("enemy_type_id"),
                            rs.getInt("enemy_count"),
                            rs.getDouble("spawn_interval")
                    ));
                }
            }
        }

        return list;
    }

    public Map<Integer, EnemyType> loadEnemyTypes(Set<Integer> ids) throws Exception {
        Map<Integer, EnemyType> map = new HashMap<>();
        if (ids.isEmpty()) return map;

        String sql = """
            SELECT id, name, max_hp, move_speed,
                   damage_to_base, reward_gold, sprite_url
            FROM enemy_types
            WHERE id = ANY (?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setArray(1, conn.createArrayOf("integer", ids.toArray()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EnemyType e = new EnemyType(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("max_hp"),
                            rs.getDouble("move_speed"),
                            rs.getInt("damage_to_base"),
                            rs.getInt("reward_gold"),
                            rs.getString("sprite_url")
                    );
                    map.put(e.getId(), e);
                }
            }
        }

        return map;
    }

    public List<Tower> loadTowers(long accountId) throws SQLException {
    String sql = """
        SELECT tc.id, tc.name, tc.tier, tc.base_dmg, tc.tower_img_url, tc.is_enabled
        FROM account_towers at
        JOIN tower_catalog tc ON at.tower_id = tc.id
        WHERE at.account_id = ?
          AND tc.is_enabled = TRUE
    """;

    List<Tower> list = new ArrayList<>();

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setLong(1, accountId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tower t = new Tower(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("tier"),
                        rs.getInt("base_dmg"),
                        rs.getString("tower_img_url")
                );
                // nếu sau này ông muốn dùng field enable thì có thể thêm setter:
                // t.setEnable(rs.getBoolean("is_enabled"));
                list.add(t);
            }
        }
    }

    return list;
}




}







