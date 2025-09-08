// Player : id, name, position, constructor(), toString()
// Team : id, name, wins, losses, roster, constructor(), addPlayer(), recordResult(win)
// Game : home, away, date, scores, played, constructor(), play(homeScore,awayScore)
// League : teams(map), games(list), addTeam(), addPlayer(), scheduleGame(), playGame(), printStandings()

import java.util.*;

// ----- Player -----
class Player {
    int id;
    String name;
    String position;

    public Player(int id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    @Override
    public String toString() {
        return name + " (" + position + ")";
    }
}

// ----- Team -----
class Team {
    int id;
    String name;
    List<Player> roster = new ArrayList<>();
    int wins = 0, losses = 0;

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addPlayer(Player p) {
        roster.add(p);
    }

    public void recordResult(boolean win) {
        if (win) wins++;
        else losses++;
    }

    @Override
    public String toString() {
        return name + " W-L: " + wins + "-" + losses;
    }
}

// ----- Game -----
class Game {
    Team home, away;
    String date;
    Integer homeScore, awayScore;
    boolean played = false;

    public Game(Team home, Team away, String date) {
        this.home = home;
        this.away = away;
        this.date = date;
    }

    public void play(int hs, int as) {
        if (played) return;
        this.homeScore = hs;
        this.awayScore = as;
        this.played = true;

        if (hs > as) {
            home.recordResult(true);
            away.recordResult(false);
        } else {
            home.recordResult(false);
            away.recordResult(true);
        }
    }

    @Override
    public String toString() {
        if (!played) return date + "  " + away.name + " @ " + home.name;
        return date + "  " + away.name + " " + awayScore + " - " + home.name + " " + homeScore;
    }
}

// ----- League -----
class League {
    Map<Integer, Team> teams = new HashMap<>();
    List<Game> games = new ArrayList<>();
    int nextTeamId = 1, nextPlayerId = 1;

    public int addTeam(String name) {
        Team t = new Team(nextTeamId++, name);
        teams.put(t.id, t);
        return t.id;
    }

    public void addPlayer(int teamId, String name, String pos) {
        teams.get(teamId).addPlayer(new Player(nextPlayerId++, name, pos));
    }

    public void schedule(int homeId, int awayId, String date) {
        games.add(new Game(teams.get(homeId), teams.get(awayId), date));
    }

    public void playGame(int index, int hs, int as) {
        games.get(index).play(hs, as);
    }

    public void printSchedule() {
        System.out.println("=== SCHEDULE ===");
        for (int i = 0; i < games.size(); i++) {
            System.out.println("#" + i + " " + games.get(i));
        }
        System.out.println();
    }

    public void printStandings() {
        System.out.println("=== STANDINGS ===");
        List<Team> list = new ArrayList<>(teams.values());
        list.sort((a, b) -> Integer.compare(b.wins, a.wins)); // sort by wins desc
        for (Team t : list) {
            System.out.println(t);
        }
        System.out.println();
    }
}

// ----- Demo -----
public class BasketballLeagueDemo {
    public static void main(String[] args) {
        League league = new League();

        int lakers = league.addTeam("Lakers");
        int warriors = league.addTeam("Warriors");

        league.addPlayer(lakers, "James", "F");
        league.addPlayer(warriors, "Curry", "G");

        league.schedule(lakers, warriors, "2025-10-01");

        league.printSchedule();

        league.playGame(0, 100, 110);

        league.printSchedule();
        league.printStandings();
    }
}
