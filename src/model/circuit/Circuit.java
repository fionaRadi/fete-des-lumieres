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
 * Représente un circuit constitué d'une liste de coordonnées.
 * Peut être chargé via un fichier qui contient les données au format TSP.
 * Permet de calculer des circuits de poids faible à partir de divers algorithmes.
 * Peut être sauvegardé.
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

    /**
     * Charge l'en-tête du circuit / fichier (soient les infos générales)
     * 
     * @param scanner Le scanner qui effectue les opérations de lecture sur le fichier
     * @throws IOException
     */
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

    /**
     * Charge les données du circuit / fichier (soient les coordonnées)
     * 
     * @param scanner Le scanner qui effectue les opérations de lecture sur le fichier
     * @throws IOException
     */
    protected abstract void loadData(Scanner scanner) throws IOException;

    /**
     * Charge le fichier passé en paramètre en tant que circuit (liste de coordonnées et autres infos)
     * 
     * @param file Le fichier à charger
     */
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
    
    /**
     * Renvoie la liste des coordonnées du circuit
     * 
     * @return La liste des coordonnées
     */
    public List<T> getCoords() { return coords; }

    /**
     * Ajoute une coordonnée au circuit
     * 
     * @param coord La coordonnée à ajouter
     */
    public void addCoord(T coord) {
        if (coord != null) {
            coords.add(coord);
        }
    }
    
    /**
     * Supprime une coordonnée du circuit
     * 
     * @param coord La coordonnée à supprimer
     */
    public void removeCoord(T coord) {
        coords.remove(coord);
    }

    /**
     * Calcule un circuit aléatoire
     */
    abstract public void calculateRandomAlgorithm();
    
    /**
     * Calcule la distance entre deux coordonnées
     * 
     * @param a La première coordonnée
     * @param b La seconde coordonnée
     * @return La distance entre a et b
     */
    abstract public double calculateDistance(T a, T b);
    
    /**
     * Calcule le poids du circuit passé en paramètre
     * 
     * @param circuit Le circuit dont on doit calculer le poids
     * @return Le poids du circuit
     */
    abstract public double calculateCircuitLength(List<T> circuit);
    
    /**
     * Renvoie une matrice correspondant aux distances entre chaque lieu
     * 
     * @return La matrice des distances
     */
    abstract public Object[][] createDistanceMatrix();
    
    /**
     * Calcule le meilleur circuit possible à partir d'un algorithme glouton
     */
    abstract public void calculateGreedyAlgorithm();
    
    /**
     * Calcule un circuit avec un algorithme glouton, en prenant comme point de départ (et d'arrivée) la coordonnée passée en paramètre
     * 
     * @param start La coordonnée de départ (et d'arrivée)
     * @return La liste de coordonnées ordonnées (circuit glouton)
     */
    abstract public List<T> greedyAlgorithmFrom(T start);
    
    /**
     * Calcule le meilleur circuit possible à partir d'un algorithme par insertion
     */
    abstract public void calculateBestInsertionAlgorithm();
    
    /**
     * Calcule un circuit avec un algorithme par insertion, en prenant comme point de départ (et d'arrivée) la coordonnée passée en paramètre
     * 
     * @param start La coordonnée de départ (et d'arrivée)
     * @return La liste de coordonnées ordonnées (circuit insertion)
     */
    abstract public List<T> insertionAlgorithmFrom(T start);
    
    /**
     * Renvoie le dernier meilleur circuit glouton calculé.
     * 
     * @return Une liste de coordonnées ordonnée
     */
    public List<T> getGreedyCircuit() {
        return greedyCircuit;
    }
    
    /**
     * Renvoie le dernier circuit glouton aléatoire calculé.
     * 
     * @return Une liste de coordonnées ordonnée
     */
    public List<T> getRandomCircuit() {
        return randomCircuit;
    }
    
    /**
     * Renvoie le dernier meilleur circuit insertion calculé.
     * 
     * @return Une liste de coordonnées ordonnée
     */
    public List<T> getInsertionCircuit() {
        return insertionCircuit;
    }
    
    /**
     * Génère un fichier de résultat .csv dans le dossier attendu (outputPath).
     * Le fichier résultat consiste en un fichier qui contient une ligne pour 
     * chaque fichier passé en paramètre : 
     * nomDuFichier;distanceCircuitGlouton;distanceCircuitInsertion;distanceMeilleurCircuit
     * 
     * @param inputFiles Les fichiers à prendre en compte dans le fichier résultat
     * @param outputPath Le dossier qui recevra le fichier résultat
     */
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
                
                circuit.calculateGreedyAlgorithm();
                circuit.calculateBestInsertionAlgorithm();
                        
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
    
    /**
     * Ecris dans le fichier souhaité la liste des coordonnées dans l'ordre correspondant au meilleur circuit (un id pour une coordonnée par ligne)
     * 
     * @param outputPath Le chemin du fichier
     * @param fileName Le nom du fichier
     */
    protected abstract void exportBestCircuit(String outputPath, String fileName);
    
    /**
     * Renvoie le type EDGE_WEIGHT_TYPE du fichier se trouvant au chemin passé en paramètre.
     * 
     * @param path Le chemin du fichier
     * @return Le type du fichier : EUC_2D ou GEO ou ERREUR
     */
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
    
    /**
     * Sauvegarde dans le fichier attendu le header correspondant au infos du circuit (nom, description)
     * 
     * @param writer Le FileWriter portant sur fichier qui doit être rempli
     * @throws IOException 
     */
    protected abstract void saveHeader(FileWriter writer) throws IOException;
    
    /**
     * Sauvegarde dans le fichier la liste de coordonnées
     * 
     * @param writer Le FileWriter portant sur fichier qui doit être rempli
     * @throws IOException 
     */
    protected abstract void saveData(FileWriter writer) throws IOException;
    
    /**
     * Sauvegarde les données du circuit dans le fichier actuel
     * 
     * @return renvoie true si l'opération a été une réussite, false sinon
     */
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
    
    /**
     * Sauvegarde les données du circuit dans un nouveau fichier (pas celui actuel)
     * 
     * @param newFile Le fichier dans lequel le circuit doit être sauvegarder
     * @return Renvoie true si l'opération a été une réussite, false sinon
     */
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
