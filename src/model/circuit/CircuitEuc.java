/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.circuit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import model.coord.CoordEuc;

/**
 * Représente un circuit euclidien constitué d'une liste de coordonnées.
 * 
 * @author ugola
 */
public class CircuitEuc extends Circuit<CoordEuc> {
    @Override
protected void loadData(Scanner scanner) throws IOException {
        System.out.println("=> Chargement des lieux");

        List<Integer> ids = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.equals("EOF")) {
                break;
            }

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");

            if (parts.length != 3) {
                throw new IOException("La ligne suivante ne contient pas exactement 3 éléments : " + line);
            }

            int id;
            try {
                id = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                throw new IOException("ID invalide dans la ligne : " + line);
            }

            if (ids.contains(id)) {
                throw new IOException("L'ID d'un point existe en double : " + id);
            }
            ids.add(id);

            float x, y;
            try {
                x = Float.parseFloat(parts[1]);
                y = Float.parseFloat(parts[2]);
            } catch (NumberFormatException e) {
                throw new IOException("Coordonnées invalides dans la ligne : " + line);
            }

            coords.add(new CoordEuc(id, x, y));
        }
    }
    
    @Override
    public void improveCircuitBySwapping(List<CoordEuc> initialCircuit, String circuitType) {
        boolean improve = true;
        
        if (initialCircuit == null) {
            System.err.println("Erreur : Le circuit initial est null");
            return;
        }
        
        List<CoordEuc> circuit = new ArrayList<>(initialCircuit);
        double currentLength = calculateCircuitLength(circuit);

        while (improve) {
            improve = false;

            for (int i = 1; i < circuit.size() - 2; i++) {
                for (int j = i + 1; j < circuit.size() - 1; j++) {
                    List<CoordEuc> newCircuit = new ArrayList<>(circuit); // Échange les deux positions
                    CoordEuc temp = newCircuit.get(i);
                    newCircuit.set(i, newCircuit.get(j));
                    newCircuit.set(j, temp);

                    double newLength = calculateCircuitLength(newCircuit);
                    if (newLength < currentLength) {
                        circuit = newCircuit;
                        currentLength = newLength;
                        improve = true;
                    }
                }
            }
        }
        
        switch (circuitType) {
            case "GREEDY":
                greedyCircuit = circuit;
                break;
                
            case "INSERTION":
                insertionCircuit = circuit;
                break;
                
            case "RANDOM":
                randomCircuit = circuit;
                break;
                
            default:
                System.err.println("Erreur : Le type de circuit ne correspond à aucun algorithme existant");
                break;
        }
    }

    @Override
    public void calculateRandomCircuit() {
        Random random = new Random();
        ArrayList<CoordEuc> circuit = new ArrayList<>();

        for (CoordEuc coord : coords) {
            do {
                coord = coords.get(random.nextInt(coords.size()));
            } while (circuit.contains(coord));

            circuit.add(coord);
        }
        circuit.add(circuit.get(0));
        randomCircuit = circuit;
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
    public void calculateBestGreedyCircuit() {
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
        
        greedyCircuit = bestCircuit ;
    }

    @Override
    public List<CoordEuc> greedyAlgorithmFrom(CoordEuc start) {
        List<CoordEuc> toVisit = new ArrayList<>(coords);
        List<CoordEuc> circuit = new ArrayList<>();

        CoordEuc current = start;
        circuit.add(current);
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
            circuit.add(nearestCoord);
        }
        circuit.add(start);
        return circuit ;
    }

    @Override
    public void calculateBestInsertionCircuit() {
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
        insertionCircuit = bestCircuit ;
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
    public Object[][] createDistanceMatrix() {
        int n = coords.size();

        // Création des données
        Object[][] data = new Object[n][n + 1];
        for (int i = 0; i < n; i++) {
            data[i][0] = "Lieu " + (i + 1); // première colonne = nom de la ligne
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    data[i][j + 1] = "0.00";
                } else {
                    double distance = calculateDistance(coords.get(i), coords.get(j));
                    data[i][j + 1] = String.format("%.2f", distance);
                }
            }
        }

        
        return data ;
        
    }

    @Override
    protected void exportBestCircuit(String outputPath, String fileName) {
        File bestCircuitFile = new File(outputPath, fileName);
        
        try (FileWriter writer = new FileWriter(bestCircuitFile)) {
            if (calculateCircuitLength(getGreedyCircuit()) >= calculateCircuitLength(getInsertionCircuit())) {            
                for (CoordEuc coord : getGreedyCircuit()) {
                    writer.write(coord.getId() + "\r\n");
                }
            } 
            
            else {
                for (CoordEuc coord : getInsertionCircuit()) {
                    writer.write(coord.getId() + "\r\n");
                }
            }
        } catch (IOException ex) {
            System.out.println("Erreur lors de l'export du meilleur circuit");
        }
    }

    @Override
    protected void saveHeader(FileWriter writer) throws IOException {
        System.out.println("=> Sauvegarde de l'en-tete");

        writer.write("NAME : " + name + "\r\n");
        writer.write("COMMENT : " + description + "\r\n");
        writer.write("TYPE : TSP" + "\r\n");
        writer.write("DIMENSION : " + coords.size() + "\r\n");
        writer.write("EDGE_WEIGHT_TYPE : EUC_2D\r\n");
        writer.write("NODE_COORD_SECTION\r\n");
    }

    @Override
    protected void saveData(FileWriter writer) throws IOException {
        System.out.println("=> Sauvegarde des données");
        
        for (CoordEuc coord : coords) {
            writer.write(coord.getId() + " " + coord.getX() + " " + coord.getY() + "\r\n");
        }

        writer.write("EOF");
    }
}
