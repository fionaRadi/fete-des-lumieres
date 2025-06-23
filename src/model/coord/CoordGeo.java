package model.coord;

import org.jxmapviewer.viewer.GeoPosition;

/**
 * Coordonnée géographique qui possède une latitude et une longitude
 * 
 * @author ugola
 */
public class CoordGeo extends Coord {    
    private GeoPosition position;

    /**
     * Constructeur de la coordonnée à partir d'une latitude et longitude
     * 
     * @param id L'id de la coordonnée
     * @param latitude La latitude en degré minute
     * @param longitude La longitude en degré minute
     */
    public CoordGeo(int id, double latitude, double longitude) {
        super(id);

        this.position = new GeoPosition(convertToDecimal(latitude), convertToDecimal(longitude));
    }
    
    /**
     * Constructeur de la coordonnée à partir d'une position
     * 
     * @param position 
     */
    public CoordGeo(GeoPosition position) {
        super(maxId + 1);
        
        this.position = position;
    }

    /**
     * Convertit une valeur en degré minute vers une valeur en degré décimal
     * 
     * @param degreeMinute La valeur en degré minute à convertir
     * @return La valeur convertie en degré décimal
     */
    private static double convertToDecimal(double degreeMinute) {
        int deg = (int) degreeMinute;
        double min = Math.abs((degreeMinute - deg) * 100);

        if (degreeMinute < 0) {
            return deg - (min / 60.0);
        } else {
            return deg + (min / 60.0);
        }
    }
    
    /**
     * Convertit une valeur en degré décimal vers une valeur en degré minute
     * 
     * @param decimalDegree La valeur en degré décimal à convertir
     * @return La valeur convertie en degré minute
     */
    private static double convertToDegreeMinute(double decimalDegree) {
        int deg = (int) decimalDegree;
        double fractional = Math.abs(decimalDegree - deg);
        int min = (int) Math.round(fractional * 60);

        return deg >= 0 ? deg + (min / 100.0) : deg - (min / 100.0);
    }

    public CoordGeo(double latitude, double longitude) {
        this(maxId + 1, latitude, longitude);
    }

    public double getDecimalLatitude() { return position.getLatitude(); }
    public double getDecimalLongitude() { return position.getLongitude(); }
    
    public double getDegreeMinuteLatitude() { return convertToDegreeMinute(position.getLatitude()); }
    public double getDegreeMinuteLongitude() { return convertToDegreeMinute(position.getLongitude()); }
    
    public GeoPosition getPosition() { return position; }
}
