import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BaseballElimination {
    private final int numberOfteams;
    private final Map<String, Integer> teamMap = new HashMap<>();
    private final List<Integer> wins = new ArrayList<>();
    private final List<Integer> losses = new ArrayList<>();
    private final List<Integer> remains = new ArrayList<>();
    private final List<List<Integer>> games = new ArrayList<>();
    private int s, t;

    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException("invalid argument");
        In in = new In(filename);
        numberOfteams = in.readInt();
        in.readLine(); // start a new line.
        for (int i = 0; i < numberOfteams; i++) {
           
            String line = in.readLine().trim();
            Scanner lineScanner = new Scanner(line);
            teamMap.put(lineScanner.next(), i);
            wins.add(lineScanner.nextInt());
            losses.add(lineScanner.nextInt());
            remains.add(lineScanner.nextInt());
            List<Integer> pairs = new ArrayList<>();
            for (int j = 0; j < numberOfteams; j++) {
                pairs.add(lineScanner.nextInt());
            }
            games.add(pairs);


        }
    } // create a baseball division from given filename in format specified below

    public int numberOfTeams() {
        return numberOfteams;
    } // number of teams

    public Iterable<String> teams() {
        return teamMap.keySet();
    } // all teams

    public int wins(String team) {
        validate(team);
        int id = teamMap.get(team);
        return wins.get(id);
    } // number of wins for given team

    public int losses(String team) {
        validate(team);
        int id = teamMap.get(team);
        return losses.get(id);
    } // number of losses for given team

    public int remaining(String team) {
        validate(team);
        int id = teamMap.get(team);
        return remains.get(id);
    } // number of remaining games for given team

    public int against(String team1, String team2) {
        validate(team1);
        validate(team2);
        int id1 = teamMap.get(team1);
        int id2 = teamMap.get(team2);
        return games.get(id1).get(id2);
    } // number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        if (team == null || !teamMap.containsKey(team))
            throw new IllegalArgumentException("invalid argument");

        return certificateOfElimination(team) != null;

    } // is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        Queue<String> setR = new Queue<>();
        int teamId = teamMap.get(team);
        int maxWin = wins.get(teamId) + remains.get(teamId);
        // trivial elimination
        for (int win : wins) {
            if (maxWin < win) {
                int eliminateId = wins.indexOf(win); // get the team id from @wins List
                for (String str : teamMap.keySet()) {
                    if (teamMap.get(str) == eliminateId) {
                        setR.enqueue(str); // iterate teamMap and match team name with id
                    }
                }
            }
        }
        if (setR.isEmpty()) {
            FlowNetwork G = createGraph(team);

            FordFulkerson fordFulkerson = new FordFulkerson(G, s, t);
            for (String str : teamMap.keySet()) {
                int teamIndex = teamMap.get(str);
                if (fordFulkerson.inCut(teamIndex)) {
                    setR.enqueue(str);
                    // if a vertex is in the source side, it eliminates the specified team
                }
            }
        }

        return (setR.isEmpty()) ? null : setR;


    } // subset R of teams that eliminates given team; null if not eliminated


    private FlowNetwork createGraph(String team) {
        validate(team);
        int teamId = teamMap.get(team);
        int teams = numberOfteams - 1;
        int pairs = teams * (teams - 1) / 2;
        int v = teams + pairs + 2;
        this.s = v - 1;
        this.t = teamId; // target vertex take the index of the team passed in the argument

        FlowNetwork G = new FlowNetwork(v);
        // rival vertexes take the indices above team numbers.
        int start = numberOfteams;
        for (int i = 0; i < games.size(); i++) {
            for (int j = 0; j < games.get(0).size(); j++) {
                if (i != teamId && j != teamId && i < j) {
                    G.addEdge(new FlowEdge(s, start, games.get(i).get(j)));
                    G.addEdge(new FlowEdge(start, i, Double.POSITIVE_INFINITY));
                    G.addEdge(new FlowEdge(start, j, Double.POSITIVE_INFINITY));
                    start++;
                }
            }
        }
        for (int id : teamMap.values()) {
            if (id != teamId) {
                G.addEdge(new FlowEdge(id, t, wins(team) + remaining(team) - wins.get(id)));
            }
        }
        return G;
    }

    private void validate(String team) {
        if (team == null || !teamMap.containsKey(team))
            throw new IllegalArgumentException("invalid argument");
    }

    public static void main(String[] args) {
        // BaseballElimination division = new BaseballElimination(args[0]);
        // for (String team : division.teams()) {
        //     StdOut.println(team + "  wins: " + division.wins(team));
        //
        // }
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
