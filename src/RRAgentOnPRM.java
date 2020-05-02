import camera.QueasyCam;
import math.Vec;
import processing.core.PApplet;
import robot.acting.RRAgent;
import robot.planning.prm.PRM;

import java.util.List;

public class RRAgentOnPRM extends PApplet {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    private static final int SIZE = 100;
    private static String SEARCH_ALGORITHM = "";

    private static final Vec MIN_CORNER = new Vec(-SIZE, -SIZE);
    private static final Vec MAX_CORNER = new Vec(SIZE, SIZE);
    private static final Vec START_POSITION = new Vec(SIZE * (-9f / 10), SIZE * (9f / 10));
    private static final Vec GOAL_POSITION = new Vec(SIZE * (9f / 10), SIZE * (-9f / 10));
    private static final float L1 = 10;
    private static final float L2 = 7;
    private static final float MAX_EDGE_LEN = L1 + L2 - 1;
    private static final float MIN_EDGE_LEN = abs(L1 - L2) + 1;

    QueasyCam cam;
    PRM prm;
    RRAgent rrAgent;

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
    }

    public void setup() {
        surface.setTitle("Processing");
        colorMode(RGB, 1.0f);
        rectMode(CENTER);
        noStroke();

        cam = new QueasyCam(this);
        prm = new PRM(this);
        rrAgent = new RRAgent(this);
        int numMilestones = 500;
        int numEdges = prm.grow(numMilestones, MIN_CORNER, MAX_CORNER, MIN_EDGE_LEN, MAX_EDGE_LEN);
        PApplet.println("# milestones : " + numMilestones + " # edges : " + numEdges);
    }

    public void draw() {
        // Reset
        background(0);

        // Update
        for (int i = 0; i < 40; i++) {
            rrAgent.update(0.002f);
        }

        // Draw
        rrAgent.draw();
        prm.draw();

        surface.setTitle("Processing:"
                + " FPS: " + (int) frameRate
                + " Search: " + SEARCH_ALGORITHM
        );
    }

    @Override
    public void keyPressed() {
        if (key == 'c') {
            cam.controllable = !cam.controllable;
        }
        if (key == 'p') {
            rrAgent.isPaused = !rrAgent.isPaused;
        }
        if (key == 'k') {
            PRM.DRAW_MILESTONES = !PRM.DRAW_MILESTONES;
        }
        if (key == 'j') {
            PRM.DRAW_EDGES = !PRM.DRAW_EDGES;
        }
        if (key == '1') {
            List<Vec> path = prm.dfs(START_POSITION, GOAL_POSITION, MIN_EDGE_LEN, MAX_EDGE_LEN);
            rrAgent.spawn(path.get(0).get(0) - 10, path.get(0).get(1) + 10, L1, L2, 0, -90, path);
            SEARCH_ALGORITHM = "DFS";
        }
        if (key == '2') {
            List<Vec> path = prm.bfs(START_POSITION, GOAL_POSITION, MIN_EDGE_LEN, MAX_EDGE_LEN);
            rrAgent.spawn(path.get(0).get(0) - 10, path.get(0).get(1) + 10, L1, L2, 0, -90, path);
            SEARCH_ALGORITHM = "BFS";
        }
        if (key == '3') {
            List<Vec> path = prm.ucs(START_POSITION, GOAL_POSITION, MIN_EDGE_LEN, MAX_EDGE_LEN);
            rrAgent.spawn(path.get(0).get(0) - 10, path.get(0).get(1) + 10, L1, L2, 0, -90, path);
            SEARCH_ALGORITHM = "UCS";
        }
        if (key == '4') {
            List<Vec> path = prm.aStar(START_POSITION, GOAL_POSITION, MIN_EDGE_LEN, MAX_EDGE_LEN);
            rrAgent.spawn(path.get(0).get(0) - 10, path.get(0).get(1) + 10, L1, L2, 0, -90, path);
            SEARCH_ALGORITHM = "A*";
        }
        if (key == '5') {
            List<Vec> path = prm.weightedAStar(START_POSITION, GOAL_POSITION, MIN_EDGE_LEN, MAX_EDGE_LEN, 1.5f);
            rrAgent.spawn(path.get(0).get(0) - 10, path.get(0).get(1) + 10, L1, L2, 0, -90, path);
            SEARCH_ALGORITHM = "weighted A*";
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"RRAgentOnPRM"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}