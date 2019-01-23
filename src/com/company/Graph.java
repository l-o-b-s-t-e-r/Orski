package com.company;

import java.util.*;

/**
 * Created by Lobster on 02.01.17.
 */
public class Graph {

    private List<Vertex> vertices;
    private int edgeCount;
    public int involvedVertices;

    public Graph(List<Edge> edges, Map<Vertex, Set<Edge>> vertices){
        edgeCount = edges.size();
        this.vertices = setVerices(edges, vertices);
    }

    private List<Vertex> setVerices(List<Edge> edges, Map<Vertex, Set<Edge>> vertices){
        for (Edge e: edges) {
            vertices.get(e.getLeftVertex()).add(e);
            vertices.get(e.getRightVertex()).add(e);
        }

        Set<Vertex> vertexSet = new HashSet<>();

        for (Map.Entry<Vertex, Set<Edge>> m: vertices.entrySet()){
            vertexSet.add(new Vertex(m.getKey().get(), m.getValue()));
        }

        return new ArrayList<>(vertexSet);
    }

    public void showGraph(){
        for (Vertex v: vertices){
            System.out.println(v.get() + ": " + v.getEdges());
        }
    }

    public int sizeVertex(){
        return vertices.size();
    }

    public int sizeEdges(){
        return edgeCount;
    }

    public int covered(List<Integer> solution){
        involvedVertices = 0;
        Set<Edge> edges = new HashSet<>();

        for (int i = 0; i < solution.size(); i++){
            if (solution.get(i) == 1){
                edges.addAll(vertices.get(i).getEdges());
                involvedVertices++;
            }
        }

        return edges.size();
    }

    /*public int getVertexPower(Vertex vertex){
        int power = 0;

        for (Edge e: edges){
            if (e.contain(vertex)){
                power++;
            }
        }

        return power;
    }

    public Set<Vertex> deleteIncidentEdges(Vertex vertex) {
        Set<Vertex> adjacentVertices = new HashSet<>();
        Collection<Edge> edgesForRemoving = new ArrayList<>();

        for (Edge e: edges){
            if (e.contain(vertex)){
                edgesForRemoving.add(e);
                adjacentVertices.add(e.getAdjacentVertex(vertex));
            }
        }

        edges.removeAll(edgesForRemoving);

        return adjacentVertices;
    }

    public Vertex maxPowerVertex(){
        Set<Vertex> vertices = new HashSet<>();
        for (Edge e: edges) {
            vertices.add(e.getLeftVertex());
            vertices.add(e.getRightVertex());
        }

        Vertex bestVertex = null;
        int maxPower = -1;

        for (Vertex v: vertices) {
            if (getVertexPower(v) > maxPower){
                bestVertex = v;
            }
        }

        return bestVertex;
    }

    public Set<Vertex> getRandomSolution(Random r){
        Set<Vertex> randomVertices = new HashSet<>();

        for (Vertex v: vertices) {
            if (r.nextBoolean()){
                randomVertices.add(v);
            }
        }

        return randomVertices;
    }

    public List<Vertex> getVicinityOfVertex(Vertex vertex, int epsilon, Set<Vertex> vertices){
        Set<Vertex> vicinity = getVicinity(vertex, epsilon, vertices);
        vicinity.remove(vertex);

        return new ArrayList<>(vicinity);
    }

    private Set<Vertex> getVicinity(Vertex vertex, int epsilon, Set<Vertex> vertices){
        Set<Vertex> currentVertices = new HashSet<>();

        for (Edge e: edges) {
            if (e.contain(vertex)) {
                currentVertices.add(e.getAdjacentVertex(vertex));
            }
        }

        epsilon--;
        vertices.addAll(currentVertices);

        if (epsilon == 0){
            return vertices;
        } else {
            for (Vertex v: currentVertices){
                getVicinityOfVertex(v, epsilon, vertices);
            }
        }

        return vertices;
    }*/
}
