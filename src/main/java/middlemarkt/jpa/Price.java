package middlemarkt.jpa;
/**
 * Preise eines Angebots
 */
public enum Price {
    VERHANDLUNGSBASIS, FESTPREIS, AUFANFRAGE;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case VERHANDLUNGSBASIS:
                return "Verhandlungsbasis";
            case FESTPREIS:
                return "Festpreis";
            case AUFANFRAGE:
                return "Auf Anfrage";
            default:
                return this.toString();
        }
    }
}
