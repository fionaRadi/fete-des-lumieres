/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.circuit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Comparator;
import model.coord.CoordGeo;

/**
 *
 * @author ugola
 */
public class CircuitGeo extends Circuit<CoordGeo> {
    @Override
    protected void loadData(Scanner scanner) throws IOException {
        System.out.println("=> Chargement des lieux");
        do {
            int id = scanner.nextInt();
            double latitude = scanner.nextFloat();
            double longitude = scanner.nextFloat();

            coords.add(new CoordGeo(id, latitude, longitude));

        } while (!scanner.hasNext("EOF"));
    }

    @Override
    public ArrayList<CoordGeo> randomCircuit() {
        Random random = new Random();
        ArrayList<CoordGeo> circuit = new ArrayList<>();

        for (int i = 0; i < coords.size(); i++) {
            CoordGeo coord;
            do {
                coord = (CoordGeo) coords.get(random.nextInt(coords.size()));
            } while (circuit.contains(coord));

            circuit.add(coord);
        }
        circuit.add(circuit.get(0));
        return circuit;
    };

    @Override
    public double calculateDistance(CoordGeo a, CoordGeo b) {
        final double EARTH_RADIUS_KM = 6371.0;
        
        double radianLat1 = Math.toRadians(a.getLatitude());
        double radianLat2 = Math.toRadians(b.getLatitude());
        
        double radianLong1 = Math.toRadians(a.getLongitude());
        double radianLong2 = Math.toRadians(b.getLongitude());

        double distance = EARTH_RADIUS_KM * Math.acos(Math.sin(radianLat1) * Math.sin(radianLat2) + Math.cos(radianLat1) *  Math.cos(radianLat2) * Math.cos(radianLong1 - radianLong2));

        return distance ;
    }

    @Override
    public double calculateCircuitLength(List<CoordGeo> circuit) {
        double total = 0.0;
        
        for (int i = 0; i < circuit.size() - 1; i++) {
            CoordGeo a = circuit.get(i);
            CoordGeo b = circuit.get(i + 1);
            total += calculateDistance(a, b);
        }
        
        return total;
    }

    @Override
    public Object[][] createMatrix(List<CoordGeo> list) {
        int n = list.size();
        Object[][] matrice = new Object[n + 1][n + 1];


        // Colonnes 0 (entêtes de lignes) + distances
        for (int i = 1; i < n; i++) {
            matrice[i][0] = "Lieu " + i;
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    matrice[i][j] = 0.0;
                } else {
                    matrice[i][j] = calculateDistance(list.get(i-1), list.get(j-1));
                }
            }
        }
        return matrice;
    }

    @Override
    public List<CoordGeo> bestGreedyAlgorithm() {
        Random random = new Random();
        double bestLength = Double.MAX_VALUE;
        List<CoordGeo> possibleStarts = new ArrayList<>(coords);
        List<CoordGeo> bestCircuit = null ;

        while (!possibleStarts.isEmpty()) {
            CoordGeo depart = possibleStarts.get(random.nextInt(possibleStarts.size()));
            possibleStarts.remove(depart);
            List<CoordGeo> circuit = greedyAlgorithmFrom(depart);
            double length = calculateCircuitLength(circuit);

            if (length < bestLength) {
                bestLength = length;
                bestCircuit = circuit;
            }
        }
        return bestCircuit ;
    }

    @Override
    public List<CoordGeo> greedyAlgorithmFrom(CoordGeo start) {
        List<CoordGeo> toVisit = new ArrayList<>(coords);
        List<CoordGeo> circuit = new ArrayList<>();

        CoordGeo current = start;
        circuit.add(current);
        toVisit.remove(current);

        while(!toVisit.isEmpty()) {
            double minDistance = Double.MAX_VALUE;
            CoordGeo nearestCoord = null ;

            for (CoordGeo c : toVisit) {
                double distance = calculateDistance(current, c) ;
                if (distance< minDistance) {
                    minDistance = distance ;
                    nearestCoord = c ;
                }
            }
            toVisit.remove(nearestCoord);
            circuit.add(nearestCoord);
        }
        circuit.add(start);
        return circuit ;
    }

    @Override
    public List<CoordGeo> bestInsertionAlgorithm() {
        double bestLength = Double.MAX_VALUE;
        List<CoordGeo> bestCircuit = null ;

        for (CoordGeo start : coords){
            List<CoordGeo> circuit = insertionAlgorithmFrom(start);
            double length = calculateCircuitLength(circuit);

            if (length < bestLength) {
                bestLength = length;
                bestCircuit = circuit;
            }
        }
        return bestCircuit ;
    }

    @Override
    public List<CoordGeo> insertionAlgorithmFrom(CoordGeo start) {
        CoordGeo nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (CoordGeo c : coords) {
            if (!c.equals(start)) {
                double d = calculateDistance(start, c);
                if (d < minDistance) {
                    minDistance = d;
                    nearest = c;
                }
            }
        }

        // Initialiser le circuit avec v1 → v2 → v1
        List<CoordGeo> circuit = new ArrayList<>();
        circuit.add(start);
        circuit.add(nearest);
        circuit.add(start);

        // Récupérer les autres points triés par ID
        List<CoordGeo> coordsLeft = new ArrayList<>(coords);
        coordsLeft.remove(start);
        coordsLeft.remove(nearest);
        coordsLeft.sort(Comparator.comparingInt(CoordGeo::getId));  // ID croissant

        // Insérer les points un par un à la meilleure position
        for (CoordGeo c : coordsLeft) {
            int bestIndex = 1;
            double bestCost = Double.MAX_VALUE;

            for (int i = 0; i < circuit.size() - 1; i++) {
                CoordGeo a = circuit.get(i);
                CoordGeo b = circuit.get(i + 1);
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
}