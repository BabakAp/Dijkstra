/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra;

import dijkstra.Dijkstra.Vertex;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Babak
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        long startTime = System.nanoTime();
        File f = null;
        if (args.length > 0) {
            //Initialize file
            f = new File(args[0]);
        } else {
            System.out.println("NO ARGUMENTS PROVIDED");
        }
        int sourceNumber = Integer.parseInt(args[1]) ;
        int destinationNumber = Integer.parseInt(args[2]) ;

        try {
            //Create scanner on file
            Scanner sc = new Scanner(f);

            int numOfVertices = sc.nextInt();
            //Initialize graph's vertices
            HashMap<Dijkstra.Vertex, ArrayList<Dijkstra.AdjacencyNode>> adjacencyList = new HashMap<>();
            ArrayList<Vertex> adList = new ArrayList<>();
            Vertex source = null;
            Vertex destination = null;

            for (int i = 0; i < numOfVertices; i++) {
                Vertex v = new Vertex(i);
                if (i == sourceNumber) {
                    source = v;
                    source.tentative_distance = 0;
                }
                if (i == destinationNumber) {
                    destination = v;
                }
                adjacencyList.put(v, new ArrayList<>());
                adList.add(i, v);
            }

            int numOfEdges = sc.nextInt();
            //Initialize graph's edges
            for (int i = 0; i < numOfEdges; i++) {
                int v1 = sc.nextInt() ;
                int v2 = sc.nextInt() ;
                int w = sc.nextInt();

                ArrayList tmp = adjacencyList.get(adList.get(v1));
                tmp.add(new Dijkstra.AdjacencyNode(w, adList.get(v2)));
                adjacencyList.put(adList.get(v1), tmp);

                ArrayList tmp2 = adjacencyList.get(adList.get(v2));
                tmp2.add(new Dijkstra.AdjacencyNode(w, adList.get(v1)));
                adjacencyList.put(adList.get(v2), tmp2);
            }
//            System.out.println("RUNNING DIJKSTRA...");
            Dijkstra.Dijkstra(adjacencyList, source, destination);
//            System.out.println("DIJKSTRA FINISHED...");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
//        long endTime = System.nanoTime();
//        System.out.println("Time: " + (endTime-startTime)/1000000 + "ms");
    }

}
