package com.viraj.sportify.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Stats for a single player's performance in a single game.
 *
 * Stats are stored as a flexible JSONB map rather than fixed columns,
 * since different sports track fundamentally different stat types
 * (e.g. basketball: points/rebounds/assists, soccer: goals/cards).
 * This lets one schema support any sport without per-sport tables
 * or a sparse, ever-growing set of nullable columns.
 */
@Entity
public class PlayerGameStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> stats = new HashMap<>();

    protected PlayerGameStat() {
    }

    public PlayerGameStat(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Map<String, Object> getStats() {
        return stats;
    }

    public void setStats(Map<String, Object> stats) {
        this.stats = stats;
    }

    public void putStat(String key, Object value) {
        this.stats.put(key, value);
    }
}
