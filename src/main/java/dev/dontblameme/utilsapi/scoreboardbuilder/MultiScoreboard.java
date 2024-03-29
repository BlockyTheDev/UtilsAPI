package dev.dontblameme.utilsapi.scoreboardbuilder;

import dev.dontblameme.utilsapi.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MultiScoreboard {

    private final List<ScoreboardBuilder> scoreboards = new ArrayList<>();
    private int scoreboard;
    private int taskId;
    private boolean shouldPause = false;
    private final List<Player> players = new ArrayList<>();

    /**
     *
     * @param scoreboard Scoreboards to add for cycling
     * @return Instance of this
     */
    public MultiScoreboard addScoreboards(ScoreboardBuilder... scoreboard) {
        scoreboards.addAll(Arrays.asList(scoreboard));
        return this;
    }

    /**
     *
     * @param player Player to display scoreboards
     * @return Instance of this
     */
    public MultiScoreboard addPlayer(Player player) {
        players.add(player);
        return this;
    }

    /**
     *
     * @param player Player to no longer display scoreboards
     * @return Instance of this
     */
    public MultiScoreboard removePlayer(Player player) {
        players.remove(player);
        return this;
    }


    /**
     *
     * @param delayBetweenUpdates Delay before cycling the scoreboard
     * @return Instance of this
     */
    public MultiScoreboard startSending(long delayBetweenUpdates) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getProvidingPlugin(Main.class), () -> {
            if(shouldPause) return;

            ScoreboardBuilder sb = getScoreboardBuilder();

            for(Player p : players)
                p.setScoreboard(sb.getScoreboard());
        }, 0, delayBetweenUpdates);
        return this;
    }

    private ScoreboardBuilder getScoreboardBuilder() {
        if(scoreboard >= scoreboards.size() - 1)
            scoreboard = -1;

        scoreboard++;
        return scoreboards.get(scoreboard);
    }

    /**
     *
     * @param shouldDestroyScoreboards Should the added scoreboards be destroyed
     * @return Instance of this
     */
    public MultiScoreboard stopSending(boolean shouldDestroyScoreboards) {
        Bukkit.getScheduler().cancelTask(taskId);

        if(shouldDestroyScoreboards) {
            for(ScoreboardBuilder sb : scoreboards)
                sb.destroy();

            for(Player player : players)
                player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
        }
        return this;
    }

    /**
     *
     * @return Instance of this
     * @apiNote Pauses the cycle of scoreboards
     */
    public MultiScoreboard pauseSending() {
        shouldPause = true;
        return this;
    }

    /**
     *
     * @return Instance of this
     * @apiNote Resumes the cycle of scoreboards
     */
    public MultiScoreboard unPauseSending() {
        shouldPause = false;
        return this;
    }

}
