/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.circuit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import model.coord.Coord;

/**
 *
 * @author ugola
 * @param <T>
 */
public abstract class Circuit<T extends Coord> {
    protected String name;
    protected String description;

    protected final List<T> coords;
    
    protected List<T> greedyCircuit;
    protected List<T> insertionCircuit;
    protected List<T> randomCircuit;
    
    protected File file;

    protected Circuit() {
        this.coords = new ArrayList<>();
    }

    void loadHeader(Scanner scanner) throws IOException {
        System.out.println("=> Chargement de l'en-tete");

        String[] line;

        do {
            line = scanner.nextLine().split("\\s*:\\s+");
            if (line.length == 0) {
                throw new IOException();
            }

            switch (line[0]) {
                case "NAME":
                    if (name == null)
                        name = line[1];
                    else
                        throw new IOException("Champ NAME trouvé plusieurs fois dans le fichier");
                    break;

                case "COMMENT":
                    if (description == null)
                        description = line[1];
                    else
                        throw new IOException("Champ COMMENT trouvé plusieurs fois dans le fichier");
                    break;
            }
        } while (!line[0].equals("NODE_COORD_SECTION") && !line[0].equals("EOF"));

        if (line[0].equals("EOF"))
            throw new IOException("Champ NODE_COORD_SECTION attendu dans le fichier");
    };

    protected abstract void loadData(Scanner scanner) throws IOException;

    public void loadFile(File file) {        
        coords.clear();
        
        this.file = file;

        try (Scanner scanner = new Scanner(new FileInputStream(file.getAbsolutePath()))) {
            System.out.println("=> Chargement du fichier");

            scanner.useLocale(Locale.US);

            loadHeader(scanner);
            loadData(scanner);

            System.out.println("=> Chargement termine");

        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    public List<T> getCoords() { return coords; }

    public void addCoord(T coord) {
        if (coord != null) {
            coords.add(coord);
        }
    }
    
    public void removeCoord(T coord) {
        coords.remove(coord);
    }

    abstract public void randomAlgorithm();
    
    abstract public double calculateDistance(T a, T b);
    abstract public double calculateCircuitLength(List<T> circuit);
    abstract public Object[][] createDistanceMatrix();
    
    abstract public void bestGreedyAlgorithm();
    abstract public List<T> greedyAlgorithmFrom(T start);
    
    abstract public void bestInsertionAlgorithm();
    abstract public List<T> insertionAlgorithmFrom(T start);
    
    public List<T> getGreedyCircuit() {
        return greedyCircuit;
    }
    
    public List<T> getRandomCircuit() {
        return randomCircuit;
    }
    
    public List<T> getInsertionCircuit() {
        return insertionCircuit;
    }
    
    public static void exportResultFile(File[] inputFiles, String outputPath) {
        File file = new File(outputPath, "resultatsX_Y.csv");
        
        try (FileWriter writer = new FileWriter(file)){
                     
            Circuit circuit;
            
            for (int i = 0; i < inputFiles.length; i++) {
                File inputFile = inputFiles[i];
                
                writer.write(inputFile.getName() + ";");
                
                String fileType = Circuit.getFileType(inputFile.getAbsolutePath());
                
                switch (fileType) {                 
                    case "EUC_2D":
                        circuit = new CircuitEuc();
                        circuit.loadFile(inputFile);           
                        break;
                        
                    case "GEO":
                        circuit = new CircuitGeo();
                        circuit.loadFile(inputFile);
                        break;
                        
                    default:
                        System.out.println("Erreur, le type du fichier est inconnu");
                        continue;
                }
                
                circuit.bestGreedyAlgorithm();
                circuit.bestInsertionAlgorithm();
                        
                double greedyLength = circuit.calculateCircuitLength(circuit.getGreedyCircuit());
                double insertionLength = circuit.calculateCircuitLength(circuit.getInsertionCircuit());
                double best = Math.max(greedyLength, insertionLength);
           
                writer.write(greedyLength + ";" + insertionLength + ";" + best + "\r\n");
                
                circuit.exportBestCircuit(outputPath, "voyage" + (i + 1) + ".txt");
            }
            
        } catch (IOException ex) {
            System.out.println("Erreur lors de la sauvegarde du fichier résultat");
        }
    }
    
    protected abstract void exportBestCircuit(String outputPath, String fileName);
    
    public static String getFileType(String path) {
        String[] line;
        try (Scanner scanner = new Scanner(new FileInputStream(path))) {
            do {
                line = scanner.nextLine().split("\\s*:\\s+");
                if (line.length == 0) {
                    throw new IOException();
                }

                if (line[0].equals("EDGE_WEIGHT_TYPE"))
                    return line[1];
            }
            while (!line[0].equals("NODE_COORD_SECTION") && !line[0].equals("EOF"));

        } catch (IOException e) {
            return "ERREUR";
        }

        return "ERREUR";
    }
    
    protected abstract void saveHeader(FileWriter writer) throws IOException;
    
    protected abstract void saveData(FileWriter writer) throws IOException;
    
    public boolean saveFile() {        
        try (FileWriter writer = new FileWriter(file)) {
            System.out.println("=> Sauvegarde du fichier");
            
            saveHeader(writer);
            saveData(writer);

            System.out.println("=> Sauvegarde terminée");
            return true;

        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
            return false;
        }
    }
    
    public boolean saveFileAs(File newFile) {        
        try (FileWriter writer = new FileWriter(newFile)) {
            System.out.println("=> Sauvegarde du fichier");
            
            saveHeader(writer);
            saveData(writer);

            System.out.println("=> Sauvegarde terminée");
            return true;

        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
            return false;
        }
    }
}
