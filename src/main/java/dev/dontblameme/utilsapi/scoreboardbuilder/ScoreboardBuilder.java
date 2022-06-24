package dev.dontblameme.utilsapi.scoreboardbuilder;

import dev.dontblameme.utilsapi.main.Main;
import dev.dontblameme.utilsapi.utils.TextParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ScoreboardBuilder {

    @Getter
    private Scoreboard scoreboard;
    private final Objective objective;
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

        if(content.length() > 64)
            throw new IllegalStateException("A line may not be longer than 64 chars. Your length: " + content.length());

        Team team = scoreboard.registerNewTeam(lineNumber + "");

        team.setPrefix(content.isEmpty() ? "" : TextParser.parseHexAndCodes(content));
        team.addEntry(getPrefix());
        objective.getScore(getPrefix()).setScore(lineNumber);

        teamCount++;
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

        if(content.length() > 64)
            throw new IllegalStateException("A line may not be longer than 64 chars. Your length: " + content.length());

        Team team = scoreboard.getTeam(lineNumber+"");

        if(team == null) return this;

        team.setPrefix(content.isEmpty() ? "" : TextParser.parseHexAndCodes(content));
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
    public ScoreboardBuilder start(int delayBetweenUpdates) {
        if(scoreboard == null) return this;

        for(Player player : players)
            player.setScoreboard(scoreboard);

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

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
    public ScoreboardBuilder send() {
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
