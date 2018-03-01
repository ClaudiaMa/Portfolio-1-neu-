/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package middlemarkt.jpa;

/**
 * Statuswerte einer Aufgabe.
 */
public enum TaskStatus {
    BIETE, VERKAUFE, SUCHE;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case BIETE:
                return "Biete";
            case VERKAUFE:
                return "Verkaufe";
            case SUCHE:
                return "Suche";
            default:
                return this.toString();
        }
    }
}
