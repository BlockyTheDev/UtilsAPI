package dev.dontblameme.utilsapi.scoreboardbuilder;

import dev.dontblameme.utilsapi.main.Main;
import dev.dontblameme.utilsapi.utils.TextParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ScoreboardBuilder {

    @Getter
    private Scoreboard scoreboard;
    private final Objective objective;
    private final HashMap<Integer, Long> lines = new HashMap<>();
    @Getter
    private final ArrayList<Player> players = new ArrayList<>();
    private int teamCount = 0;
    private int taskId;

    /**
     *
     * @param displayName Title of the scoreboard
     */
    public ScoreboardBuilder(String displayName) {

        if(displayName.length() > 128)
            throw new IllegalStateException("Your Scoreboard DisplayName may not be longer than 128 chars. Your length: " + displayName.length());

        this.scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        String name = new Random().nextDouble() + "";
        Objective customObjective = scoreboard.registerNewObjective(name, "dummy", TextParser.parseHexAndCodes(displayName), RenderType.INTEGER);

        customObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective = customObjective;
    }

    /**
     *
     * @param content Content of the line
     * @param lineNumber line number starting at 0
     * @return Instance of this
     */
    public ScoreboardBuilder addLine(String content, int lineNumber) {
        if(scoreboard == null) return this;

        if(teamCount >= 15)
            throw new IllegalStateException("You may not add more than 15 lines to a scoreboard");

        long randomLong = new Random().nextLong();
        Team team = scoreboard.registerNewTeam(randomLong + "");

        if(content.length() > 64 && content.length() <= 120) {
            String[] split = content.split("(?<=\\G.{64})");
            String prefix = TextParser.parseHexAndCodes(split[0]);
            String suffix = TextParser.parseHexAndCodes(split[1]);

            team.setPrefix(prefix);
            team.setSuffix(ChatColor.getLastColors(prefix) + suffix);
        } else if(content.length() <= 64) {
            team.setPrefix(content.isEmpty() ? "" : TextParser.parseHexAndCodes(content));
            team.setSuffix("");
        } else {
            throw new IllegalStateException("A line may not be longer than 120 chars. Your length: " + content.length());
        }

        team.addEntry(getPrefix());
        objective.getScore(getPrefix()).setScore(lineNumber);
        lines.put(lineNumber, randomLong);

        teamCount++;
        return this;
    }

    /**
     *
     * @param displayName The new title of the scoreboard
     * @return Instance of this
     */
    public ScoreboardBuilder setDisplayName(String displayName) {
        this.objective.setDisplayName(TextParser.parseHexAndCodes(displayName));
        return this;
    }

    /**
     *
     * @param content Content of the line
     * @param lineNumber line number starting at 0
     * @return Instance of this
     */
    public ScoreboardBuilder updateLine(String content, int lineNumber) {
        if(scoreboard == null) return this;

        Team team = scoreboard.getTeam(lines.get(lineNumber) + "");

        if(team == null) return this;

        if(content.length() > 64 && content.length() <= 120) {
            String[] split = content.split("(?<=\\G.{64})");
            String prefix = TextParser.parseHexAndCodes(split[0]);
            String suffix = TextParser.parseHexAndCodes(split[1]);

            // Todo: suffix not taking hex code, prob. spigot issue with getlastcolors
            team.setPrefix(prefix);
            team.setSuffix(ChatColor.getLastColors(prefix) + suffix);
        } else if(content.length() <= 64) {
            team.setPrefix(content.isEmpty() ? "" : TextParser.parseHexAndCodes(content));
            team.setSuffix("");
        } else {
            throw new IllegalStateException("A line may not be longer than 120 chars. Your length: " + content.length());
        }
        return this;
    }

    /**
     *
     * @param player Player to display scoreboard
     * @return Instance of this
     */
    public ScoreboardBuilder addPlayer(Player player) {
        players.add(player);
        return this;
    }

    /**
     *
     * @param player Player to no longer display scoreboard
     * @return Instance of this
     */
    public ScoreboardBuilder removePlayer(Player player) {
        players.remove(player);
        return this;
    }

    /**
     * @apiNote Removing the scoreboard & showing every added player an empty, freshly generated scoreboard
     */
    public void destroy() {
        if(scoreboard == null) return;

        scoreboard.getTeams().forEach(Team::unregister);
        scoreboard = null;

        for(Player player : players)
            player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
    }

    /**
     *
     * @param delayBetweenUpdates Delay between refreshing the scoreboard
     * @return Instance of this
     */
    public ScoreboardBuilder startSending(long delayBetweenUpdates) {
        if(scoreboard == null) return this;

        for(Player player : players)
            player.setScoreboard(scoreboard);

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getProvidingPlugin(Main.class), () -> {

            if(scoreboard == null) {
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }

            for(Player player : players)
                player.setScoreboard(scoreboard);

        }, delayBetweenUpdates, delayBetweenUpdates);
        return this;
    }

    /**
     *
     * @return Instance of this
     * @apiNote Sends the scoreboard to every player provided, not updating / refreshing. If you want dynamic updates use the start() method
     */
    public ScoreboardBuilder sendOnce() {
        if(scoreboard == null) return this;

        for(Player player : players)
            player.setScoreboard(scoreboard);

        return this;
    }

    private String getPrefix() {
        if(teamCount <= 9) return "§" + teamCount;

        return switch(teamCount) {
            case 10 -> "§a";
            case 11 -> "§b";
            case 12 -> "§c";
            case 13 -> "§d";
            case 14 -> "§e";
            case 15 -> "§f";
            default -> "";
        };
    }

}
