/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.circuit;

import java.io.IOException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import model.coord.CoordEuc;

/**
 *
 * @author ugola
 */
public class CircuitEuc extends Circuit<CoordEuc> {
    @Override
    protected void loadData(Scanner scanner) throws IOException {
        System.out.println("=> Chargement des lieux");
        do {
            int id = scanner.nextInt();
            float x = scanner.nextFloat();
            float y = scanner.nextFloat();

            coords.add(new CoordEuc(id, x, y));

        } while (!scanner.hasNext("EOF"));
    }

    @Override
    public ArrayList<CoordEuc> randomCircuit() {
        Random random = new Random();
        ArrayList<CoordEuc> circuit = new ArrayList<>();

        for (CoordEuc coord : coords) {
            do {
                coord = coords.get(random.nextInt(coords.size()));
            } while (circuit.contains(coord));

            circuit.add(coord);
        }
        
        return circuit;
    }

    @Override
    public double calculateDistance(CoordEuc a, CoordEuc b) {
        CoordEuc aEuc = (CoordEuc) a;
        CoordEuc bEuc = (CoordEuc) b;

        double xa = aEuc.getX();
        double xb = bEuc.getX();
        double ya = aEuc.getY();
        double yb = bEuc.getY();

        return Math.sqrt(Math.pow(xb-xa, 2) + Math.pow(yb-ya, 2)) ;
    }

    @Override
    public double calculateCircuitLength(List<CoordEuc> circuit) {
        double total = 0.0;
        
        for (int i = 0; i < circuit.size() - 1; i++) {
            CoordEuc a = circuit.get(i);
            CoordEuc b = circuit.get(i + 1);
            total += calculateDistance(a, b);
        }
        
        return total;
    }

    @Override
    public List<CoordEuc> bestGreedyAlgorithm() {
        Random random = new Random();
        double bestLength = Double.MAX_VALUE;
        List<CoordEuc> possibleStarts = new ArrayList<>(coords);
        List<CoordEuc> bestCircuit = null ;

        while (!possibleStarts.isEmpty()) {
            CoordEuc start = possibleStarts.get(random.nextInt(possibleStarts.size()));
            possibleStarts.remove(start);
            List<CoordEuc> circuit = greedyAlgorithmFrom(start);
            double length = calculateCircuitLength(circuit);

            if (length < bestLength) {
                bestLength = length;
                bestCircuit = circuit;
            }
        }
        return bestCircuit ;
    }

    @Override
    public List<CoordEuc> greedyAlgorithmFrom(CoordEuc start) {
        List<CoordEuc> toVisit = new ArrayList<>(coords);
        List<CoordEuc> circuits = new ArrayList<>();

        CoordEuc current = start;
        circuits.add(current);
        toVisit.remove(current);

        while(!toVisit.isEmpty()) {
            double minDistance = Double.MAX_VALUE;
            CoordEuc nearestCoord = null ;

            for (CoordEuc c : toVisit) {
                double distance = calculateDistance(current, c) ;
                if (distance< minDistance) {
                    minDistance = distance ;
                    nearestCoord = c ;
                }
            }
            toVisit.remove(nearestCoord);
            circuits.add(nearestCoord);
        }
        circuits.add(start);
        return circuits ;
    }

    @Override
    public List<CoordEuc> bestInsertionAlgorithm() {
        double bestLength = Double.MAX_VALUE;
        List<CoordEuc> bestCircuit = null ;

        for (CoordEuc start : coords){
            List<CoordEuc> circuit = insertionAlgorithmFrom(start);
            double length = calculateCircuitLength(circuit);

            if (length < bestLength) {
                bestLength = length;
                bestCircuit = circuit;
            }
        }
        return bestCircuit ;
    }

    @Override
    public List<CoordEuc> insertionAlgorithmFrom(CoordEuc start) {
        CoordEuc nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (CoordEuc c : coords) {
            if (!c.equals(start)) {
                double d = calculateDistance(start, c);
                if (d < minDistance) {
                    minDistance = d;
                    nearest = c;
                }
            }
        }

        // Initialiser le circuit avec v1 → v2 → v1
        List<CoordEuc> circuit = new ArrayList<>();
        circuit.add(start);
        circuit.add(nearest);
        circuit.add(start);

        // Récupérer les autres points triés par ID
        List<CoordEuc> coordsLeft = new ArrayList<>(coords);
        coordsLeft.remove(start);
        coordsLeft.remove(nearest);
        coordsLeft.sort(Comparator.comparingInt(CoordEuc::getId));  // ID croissant

        // Insérer les points un par un à la meilleure position
        for (CoordEuc c : coordsLeft) {
            int bestIndex = 1;
            double bestCost = Double.MAX_VALUE;

            for (int i = 0; i < circuit.size() - 1; i++) {
                CoordEuc a = circuit.get(i);
                CoordEuc b = circuit.get(i + 1);
                double cost = calculateDistance(a, c) + calculateDistance(c, b) - calculateDistance(a, b);
                if (cost < bestCost) {
                    bestCost = cost;
                    bestIndex = i + 1;
                }
            }

            circuit.add(bestIndex, c);
        }

        return circuit;
    }

    @Override
    public Object[][] createMatrix(List<CoordEuc> list) {
        int n = list.size() ;
        Object[][] matrice = new Object[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matrice[i][j] = 0 ;
                }
                else {
                    matrice[i][j] = calculateDistance(list.get(i), list.get(j)) ;
                }
            }
        }
        return matrice ;
    }
}
