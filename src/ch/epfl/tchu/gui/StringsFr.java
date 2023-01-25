package ch.epfl.tchu.gui;

public final class StringsFr {
    private StringsFr() {}

    // Name of the cards
    public static final String BLACK_CARD = "noire";
    public static final String BLUE_CARD = "bleue";
    public static final String GREEN_CARD = "verte";
    public static final String ORANGE_CARD = "orange";
    public static final String RED_CARD = "rouge";
    public static final String VIOLET_CARD = "violette";
    public static final String WHITE_CARD = "blanche";
    public static final String YELLOW_CARD = "jaune";
    public static final String LOCOMOTIVE_CARD = "locomotive";

    // Label of the buttons
    public static final String TICKETS = "Billets";
    public static final String CARDS = "Cartes";
    public static final String CHOOSE = "Choisir";

    // Title of the windows
    public static final String TICKETS_CHOICE = "Choix de billets";
    public static final String CARDS_CHOICE = "Choix de cartes";

    // Invites
    public static final String CHOOSE_TICKETS =
            "Choisissez au moins %s billet%s parmi ceux-ci :";
    public static final String CHOOSE_CARDS =
            "Choisissez les cartes à utiliser pour vous emparer de cette route :";
    public static final String CHOOSE_ADDITIONAL_CARDS =
            "Choisissez les cartes supplémentaires à utiliser pour vous" +
            " emparer de ce tunnel (ou aucune pour annuler et passer votre tour) :";
    public static final String SEND_MESSAGE =
            "Pour échanger des messages avec les autres joueurs :";

    // Information about the game progress
    public static final String WILL_PLAY_FIRST =
            "%s jouera en premier.\n\n";
    public static final String KEPT_N_TICKETS =
            "%s a gardé %s billet%s.\n";
    public static final String CAN_PLAY =
            "\nC'est à %s de jouer.\n";
    public static final String DREW_TICKETS =
            "%s a tiré %s billet%s...\n";
    public static final String DREW_BLIND_CARD =
            "%s a tiré une carte de la pioche.\n";
    public static final String DREW_VISIBLE_CARD =
            "%s a tiré une carte %s visible.\n";
    public static final String CLAIMED_ROUTE =
            "%s a pris possession de la route %s au moyen de %s.\n";
    public static final String ATTEMPTS_TUNNEL_CLAIM =
            "%s tente de s'emparer du tunnel %s au moyen de %s !\n";
    public static final String ADDITIONAL_CARDS_ARE =
            "Les cartes supplémentaires sont %s. ";
    public static final String NO_ADDITIONAL_COST =
            "Elles n'impliquent aucun coût additionnel.\n";
    public static final String SOME_ADDITIONAL_COST =
            "Elles impliquent un coût additionnel de %s carte%s.\n";
    public static final String DID_NOT_CLAIM_ROUTE =
            "%s n'a pas pu (ou voulu) s'emparer de la route %s.\n";
    public static final String LAST_TURN_BEGINS =
            "\n%s n'a plus que %s wagon%s, le dernier tour commence !\n";
    public static final String GETS_BONUS =
            "\n%s reçoit un bonus de 10 points pour le plus long trajet (%s).\n";
    public static final String WINS =
            "\n%s remporte la victoire avec un score de %s point%s !\n";
    public static final String DRAW =
            "\n%s sont ex æqo avec %s points !\n";
    public static final String POINTS =
            "%s a obtenu %s point%s !\n";

    // Statistics of the players
    public static final String PLAYER_STATS =
            " %s :\n– %s billets,\n– %s cartes,\n– %s wagons,\n– %s points.";

    // Text separators
    public static final String AND_SEPARATOR = " et ";
    public static final String EN_DASH_SEPARATOR = " – ";

    /**
     * Returns a string stating the plural, or the empty string.
     * @param value (int) : the value decisive for the returned string.
     * @return string (String) : the empty string if the value is ±1 or 0, the string "s" otherwise.
     */
    public static String plural(int value) {
        return (Math.abs(value) == 1 || value == 0) ? "" : "s";
    }
}
