/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra;

import fibonacciHeap.FibonacciHeap;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Babak Alipour (babak.ap@ufl.edu)(babak@cise.ufl.edu)
 */
public class Dijkstra {

    private static FibonacciHeap<Vertex> fiboHeap;
    private static HashMap<Vertex, FibonacciHeap.Node> V2N;

    public static void Dijkstra(HashMap<Vertex, ArrayList<AdjacencyNode>> adjacencyList, Vertex source, Vertex destination) {
        fiboHeap = new FibonacciHeap<>();
        V2N = new HashMap<>();
        //Initialization, insert all vertices into heap
        for (Vertex k : adjacencyList.keySet()) {
            FibonacciHeap.Node fibNode = new FibonacciHeap.Node(k.tentative_distance, k);
            V2N.put(k, fibNode);
            fiboHeap.insert(fibNode);
        }
        //Initialization, insert all vertices into heap with dist[source]=0, all else = infinity
//        adjacencyList.forEach((k, v) -> fiboHeap.insert(k == source ? 0 : Integer.MAX_VALUE, k));

        while (!fiboHeap.isEmpty()) {
            Vertex u = fiboHeap.removeMin();
            for (AdjacencyNode v : adjacencyList.get(u)) {
                int alt = u.tentative_distance + v.weight;
                if (alt < v.vertex.tentative_distance) {
                    v.vertex.tentative_distance = alt;
                    v.vertex.previous = u;
                    fiboHeap.decreaseKey(V2N.get(v.vertex), alt);
                }
            }
        }

        //print output
        System.out.println(destination.tentative_distance);
        Vertex v = destination;
        String result = "";
        while (v != source) {
            result = " " + (v.number ) + result;
            v = v.previous;
        }
        result = (source.number ) + result;
        System.out.println(result);
    }

    private Dijkstra() {
    }

    /**
     * Vertex to be used for Dijkstra's algorithm
     */
    public static class Vertex {

        /**
         * Vertex number
         */
        int number;
        /**
         * Tentative distance from source to this vertex
         */
        int tentative_distance;
        /**
         * Previous vertex on shortest path to source
         */
        Vertex previous;

        /**
         * Creates a new vertex, defaults the tentative distance to
         * Integer.MAX_VALUE
         * <p>
         * @param vertexNumber
         */
        public Vertex(int vertexNumber) {
            number = vertexNumber;
            tentative_distance = Integer.MAX_VALUE;
            previous = null;
        }

        @Override
        public String toString() {
            return "Number: " + number + " Tentative distance: " + tentative_distance;
        }
        
        

    }

    /**
     * Node to be saved in adjacency list
     */
    public static class AdjacencyNode {

        int weight;
        Vertex vertex;

        public AdjacencyNode(int weight, Vertex vertex) {
            this.weight = weight;
            this.vertex = vertex;
        }

    }

}
