package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Controller {

    private Graph graph;
    private Random r = new Random();
    private Float genGenerateProbability = 1.0f;
    private List<List<Integer>> combinations;
    private final Integer ALPHA = 4;
    private Integer covered, candidates, bestCandidatesCount, parentsCountPerChild, childCountFromParents, mutationGenCount, maxLifeCyclesNumber;

    private List<List<Integer>> bestCandidatesList;

    public void loadDataSet(String dataSetPath) {
        init();

        loadGraph(dataSetPath);

        startAlgorithm();
    }

    private void init() {
        parentsCountPerChild = 2;
        childCountFromParents = 1;
        candidates = 40;
        bestCandidatesCount = 4;
        mutationGenCount = 1;
        maxLifeCyclesNumber = 10;
    }

    private void loadGraph(String dataSetPath) {
        List<Edge> edges = new ArrayList<>();
        Map<Vertex, Set<Edge>> vertices = new HashMap<>();
        try {
            String s;
            BufferedReader in = new BufferedReader(new FileReader(dataSetPath));

            in.readLine();
            Edge edge;
            while ((s = in.readLine()) != null) {
                edge = new Edge(s);
                vertices.put(edge.getLeftVertex(), new HashSet<Edge>());
                vertices.put(edge.getRightVertex(), new HashSet<Edge>());
                edges.add(new Edge(s));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        graph = new Graph(edges, vertices);
    }

    private void startAlgorithm() {
        List<List<Integer>> candidatesList = generateCandidatesList();
        System.out.println("candidates list size: " + candidatesList.size());


        //bestCandidatesList = divers(candidatesList);
        bestCandidatesList = findBest(candidatesList);
        System.out.println("bestCandidatesCount list size: " + bestCandidatesList.size());

        combinations = combinations(parentsCountPerChild, bestCandidatesList.size());
        System.out.println("parentsCountPerChild: " + parentsCountPerChild + " bestCandidatesList: " + bestCandidatesList.size());
        System.out.println("combinations: " + combinations.size());

        int i = 0;
        List<List<Integer>> children = new ArrayList<>();
        while (i < maxLifeCyclesNumber) {
            System.out.println("Life cycle: " + ++i);

            if (!children.isEmpty()) {
                //children.addAll(new ArrayList<>(bestCandidatesList));
                bestCandidatesList = findBest(new ArrayList<>(children));
                children.clear();
            }

            for (List<Integer> combination : combinations) {
                children.addAll(crossover(getParents(combination)));
                mutation(children);
            }

            for (List<Integer> s : bestCandidatesList) {
                Integer f = F(s, graph);
                System.out.println("F: " + f + " covered/uncovered: " + covered + "/" + (graph.sizeEdges() - covered) + " - " + graph.involvedVertices + " solution: " + s);
            }
        }
    }

    private List<List<Integer>> generateCandidatesList() {
        List<List<Integer>> CL = new ArrayList<>();
        while (CL.size() < candidates) {
            CL.add(generateCandidate(graph));
        }

        return CL;
    }

    private List<Integer> generateCandidate(Graph graph) {
        List<Integer> candidate = new ArrayList<>();

        for (int i = 0; i < graph.sizeVertex(); i++) {
            if (getRandomBoolean()) {
                candidate.add(1);
            } else {
                candidate.add(0);
            }
        }

        return candidate;
    }

    private List<List<Integer>> findBest(List<List<Integer>> candidatesList) {
        Collections.sort(candidatesList, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                return F(o1, graph) - F(o2, graph);
            }
        });

        return candidatesList.subList(0, bestCandidatesCount);
    }

    private List<List<Integer>> combinations(int k, int n) {
        return createCombinations(new ArrayList<Integer>(), new ArrayList<List<Integer>>(), k, n, n);
    }

    private List<List<Integer>> createCombinations(List<Integer> current, List<List<Integer>> allCombinations, int k, int n, int size) {
        if (current.isEmpty()) {
            for (int i = 0; i < n; i++) {
                current.add(i);
                createCombinations(current, allCombinations, k - 1, n - 1, size);
                current.clear();
            }

            return allCombinations;
        } else {
            if (k > 0) {
                int initSize = current.size();
                for (int i = 0; i < size; i++) {
                    if (!current.contains(i)) {
                        current.add(i);
                        createCombinations(current, allCombinations, k - 1, n - 1, size);
                        current = current.subList(0, initSize);
                    }
                }
            } else {
                allCombinations.add(new ArrayList<>(current));
                return allCombinations;
            }

            return allCombinations;
        }
    }

    private boolean getRandomBoolean() {
        return r.nextFloat() < genGenerateProbability;
    }

    private List<List<Integer>> getParents(List<Integer> combination) {
        List<List<Integer>> parents = new ArrayList<>();

        for (int index : combination) {
            parents.add(bestCandidatesList.get(index));
        }

        return parents;
    }

    private List<List<Integer>> crossover(List<List<Integer>> parents) {
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < childCountFromParents; i++) {
            children.add(createChild(parents));
        }

        return children;
    }

    private List<Integer> createChild(List<List<Integer>> parents) {
        List<Integer> child = new ArrayList<>();
        int dividerIndex = r.nextInt(graph.sizeVertex());

        child.addAll(parents.get(0).subList(0, dividerIndex));
        child.addAll(parents.get(1).subList(dividerIndex, graph.sizeVertex()));

        return child;
    }

    private List<List<Integer>> mutation(List<List<Integer>> children) {
        int gen, genNumber;

        for (List<Integer> child : children) {
            for (int i = 0; i < mutationGenCount; i++) {
                genNumber = r.nextInt(graph.sizeVertex());
                gen = child.get(genNumber);
                child.set(genNumber, gen == 0 ? 1 : 0);
            }
        }

        return children;
    }

    private int F(List<Integer> solution, Graph graph) {
        covered = graph.covered(solution);
        return covered + ALPHA * (graph.sizeEdges() - covered) + graph.involvedVertices;
    }
}