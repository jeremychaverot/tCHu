package ch.epfl.tchu.game;

import ch.epfl.tchu.game.Route.Level;

import java.util.List;

/**
 * @author Jérémy Chaverot (315858)
 */
public final class UsaMap {
    private UsaMap() { }

    public static List<Station> stations() {
        return ALL_STATIONS;
    }

    public static List<Route> routes() {
        return ALL_ROUTES;
    }

    public static List<Ticket> tickets() {
        return ALL_TICKETS;
    }

    // Stations - cities
    private static final Station ATL = new Station(0, "Atlanta");
    private static final Station BOS = new Station(1, "Boston");
    private static final Station CAL = new Station(2, "Calgary");
    private static final Station CHA = new Station(3, "Charleston");
    private static final Station CHI = new Station(4, "Chicago");
    private static final Station DAL = new Station(5, "Dallas");
    private static final Station DEN = new Station(6, "Denver");
    private static final Station DUL = new Station(7, "Duluth");
    private static final Station ELP = new Station(8, "El Paso");
    private static final Station HEL = new Station(9, "Helena");
    private static final Station HOU = new Station(10, "Houston");
    private static final Station KAN = new Station(11, "Kansas City");
    private static final Station LAS = new Station(12, "Las Vegas");
    private static final Station LIT = new Station(13, "Little Rock");
    private static final Station LOS = new Station(14, "Los Angeles");
    private static final Station MIA = new Station(15, "Miami");
    private static final Station MON = new Station(16, "Montréal");
    private static final Station NAS = new Station(17, "Nashville");
    private static final Station NEW = new Station(18, "New York");
    private static final Station NOU = new Station(19, "Nouvelle Orléans");
    private static final Station OKL = new Station(20, "Oklahoma City");
    private static final Station OMA = new Station(21, "Omaha");
    private static final Station PHO = new Station(22, "Phoenix");
    private static final Station PIT = new Station(23, "Pittsburgh");
    private static final Station POR = new Station(24, "Portland");
    private static final Station RAL = new Station(25, "Raleigh");
    private static final Station SAI = new Station(26, "Saint Louis");
    private static final Station SAL = new Station(27, "Salt Lake City");
    private static final Station SANF = new Station(28, "San Francisco");
    private static final Station SANT = new Station(29, "Santa Fe");
    private static final Station SAU = new Station(30, "Sault Ste Marie");
    private static final Station SEA = new Station(31, "Seattle");
    private static final Station TOR = new Station(32, "Toronto");
    private static final Station VAN = new Station(33, "Vancouver");
    private static final Station WAS = new Station(34, "Washington");
    private static final Station WIN = new Station(35, "Winnipeg");


    private static final List<Station> ALL_STATIONS = List.of(
            ATL, BOS, CAL, CHA, CHI, DAL, DEN, DUL, ELP, HEL, HOU, KAN,
            LAS, LIT, LOS, MIA, MON, NAS, NEW, NOU, OKL, OMA, PHO, PIT,
            POR, RAL, SAI, SAL, SANF, SANT, SAU, SEA, TOR, VAN, WAS, WIN);

    // Routes
    private static final List<Route> ALL_ROUTES = List.of(
            new Route("ATL_CHA_1", ATL, CHA, 2, Level.UNDERGROUND, null),
            new Route("ATL_MIA_1", ATL, MIA, 5, Level.OVERGROUND, Color.BLUE),
            new Route("ATL_NAS_1", ATL, NAS, 1, Level.UNDERGROUND, null),
            new Route("ATL_NOU_1", ATL, NOU, 4, Level.OVERGROUND, Color.YELLOW),
            new Route("ATL_NOU_2", ATL, NOU, 4, Level.OVERGROUND, Color.ORANGE),
            new Route("ATL_RAL_1", ATL, RAL, 2, Level.OVERGROUND, Color.BLACK),
            new Route("ATL_RAL_2", ATL, RAL, 2, Level.OVERGROUND, null),
            new Route("BOS_MON_1", BOS, MON, 2, Level.UNDERGROUND, null),
            new Route("BOS_MON_2", BOS, MON, 2, Level.UNDERGROUND, null),
            new Route("BOS_NEW_1", BOS, NEW, 2, Level.OVERGROUND, Color.YELLOW),
            new Route("BOS_NEW_2", BOS, NEW, 2, Level.OVERGROUND, Color.RED),
            new Route("CAL_HEL_1", CAL, HEL, 4, Level.UNDERGROUND, null),
            new Route("CAL_SEA_1", CAL, SEA, 4, Level.UNDERGROUND, null),
            new Route("CAL_VAN_1", CAL, VAN, 3, Level.OVERGROUND, null),
            new Route("CAL_WIN_1", CAL, WIN, 6, Level.OVERGROUND, Color.WHITE),
            new Route("CHA_MIA_1", CHA, MIA, 4, Level.UNDERGROUND, Color.VIOLET),
            new Route("CHA_RAL_1", CHA, RAL, 2, Level.OVERGROUND, Color.BLUE),
            new Route("CHI_DUL_1", CHI, DUL, 3, Level.OVERGROUND, Color.RED),
            new Route("CHI_OMA_1", CHI, OMA, 4, Level.OVERGROUND, Color.BLUE),
            new Route("CHI_PIT_1", CHI, PIT, 3, Level.OVERGROUND, Color.ORANGE),
            new Route("CHI_PIT_2", CHI, PIT, 3, Level.OVERGROUND, Color.BLACK),
            new Route("CHI_SAI_1", CHI, SAI, 2, Level.OVERGROUND, Color.GREEN),
            new Route("CHI_SAI_2", CHI, SAI, 2, Level.OVERGROUND, Color.WHITE),
            new Route("CHI_TOR_1", CHI, TOR, 4, Level.UNDERGROUND, Color.WHITE),
            new Route("DAL_ELP_1", DAL, ELP, 4, Level.OVERGROUND, Color.RED),
            new Route("DAL_HOU_1", DAL, HOU, 1, Level.UNDERGROUND, null),
            new Route("DAL_HOU_2", DAL, HOU, 1, Level.UNDERGROUND, null),
            new Route("DAL_LIT_1", DAL, LIT, 2, Level.OVERGROUND, null),
            new Route("DAL_OKL_1", DAL, OKL, 2, Level.UNDERGROUND, null),
            new Route("DAL_OKL_2", DAL, OKL, 2, Level.UNDERGROUND, null),
            new Route("DEN_HEL_1", DEN, HEL, 4, Level.OVERGROUND, Color.GREEN),
            new Route("DEN_KAN_1", DEN, KAN, 4, Level.OVERGROUND, Color.BLACK),
            new Route("DEN_KAN_2", DEN, KAN, 4, Level.OVERGROUND, Color.ORANGE),
            new Route("DEN_OKL_1", DEN, OKL, 4, Level.OVERGROUND, Color.RED),
            new Route("DEN_OMA_1", DEN, OMA, 4, Level.OVERGROUND, Color.VIOLET),
            new Route("DEN_PHO_1", DEN, PHO, 5, Level.OVERGROUND, Color.WHITE),
            new Route("DEN_SAL_1", DEN, SAL, 3, Level.OVERGROUND, Color.RED),
            new Route("DEN_SAL_2", DEN, SAL, 3, Level.OVERGROUND, Color.YELLOW),
            new Route("DEN_SANT_1", DEN, SANT, 2, Level.UNDERGROUND, null),
            new Route("DUL_HEL_1", DUL, HEL, 6, Level.OVERGROUND, Color.ORANGE),
            new Route("DUL_OMA_1", DUL, OMA, 2, Level.UNDERGROUND, null),
            new Route("DUL_OMA_2", DUL, OMA, 2, Level.UNDERGROUND, null),
            new Route("DUL_SAU_1", DUL, SAU, 3, Level.UNDERGROUND, Color.YELLOW),
            new Route("DUL_TOR_1", DUL, TOR, 6, Level.UNDERGROUND, Color.VIOLET),
            new Route("DUL_WIN_1", DUL, WIN, 4, Level.UNDERGROUND, Color.BLACK),
            new Route("ELP_HOU_1", ELP, HOU, 6, Level.OVERGROUND, Color.GREEN),
            new Route("ELP_LOS_1", ELP, LOS, 6, Level.OVERGROUND, Color.BLACK),
            new Route("ELP_OKL_1", ELP, OKL, 5, Level.OVERGROUND, Color.YELLOW),
            new Route("ELP_PHO_1", ELP, PHO, 3, Level.UNDERGROUND, null),
            new Route("ELP_SANT_1", ELP, SANT, 2, Level.OVERGROUND, null),
            new Route("HEL_OMA_1", HEL, OMA, 5, Level.OVERGROUND, Color.RED),
            new Route("HEL_SAL_1", HEL, SAL, 3, Level.OVERGROUND, Color.VIOLET),
            new Route("HEL_SEA_1", HEL, SEA, 6, Level.OVERGROUND, Color.YELLOW),
            new Route("HEL_WIN_1", HEL, WIN, 4, Level.UNDERGROUND, Color.BLUE),
            new Route("HOU_NOU_1", HOU, NOU, 2, Level.UNDERGROUND, null),
            new Route("KAN_OKL_1", KAN, OKL, 2, Level.OVERGROUND, null),
            new Route("KAN_OKL_2", KAN, OKL, 2, Level.OVERGROUND, null),
            new Route("KAN_OMA_1", KAN, OMA, 1, Level.OVERGROUND, null),
            new Route("KAN_OMA_2", KAN, OMA, 1, Level.OVERGROUND, null),
            new Route("KAN_SAI_1", KAN, SAI, 2, Level.OVERGROUND, Color.BLUE),
            new Route("KAN_SAI_2", KAN, SAI, 2, Level.OVERGROUND, Color.VIOLET),
            new Route("LAS_LOS_1", LAS, LOS, 2, Level.UNDERGROUND, Color.GREEN),
            new Route("LAS_SAL_1", LAS, SAL, 3, Level.UNDERGROUND, Color.ORANGE),
            new Route("LIT_NAS_1", LIT, NAS, 3, Level.OVERGROUND, Color.WHITE),
            new Route("LIT_NOU_1", LIT, NOU, 3, Level.OVERGROUND, Color.GREEN),
            new Route("LIT_OKL_1", LIT, OKL, 2, Level.OVERGROUND, Color.BLACK),
            new Route("LIT_SAI_1", LIT, SAI, 2, Level.OVERGROUND, null),
            new Route("LOS_PHO_1", LOS, PHO, 3, Level.UNDERGROUND, null),
            new Route("LOS_SANF_1", LOS, SANF, 3, Level.OVERGROUND, Color.YELLOW),
            new Route("LOS_SANF_2", LOS, SANF, 3, Level.OVERGROUND, Color.VIOLET),
            new Route("MIA_NOU_1", MIA, NOU, 6, Level.OVERGROUND, Color.RED),
            new Route("MON_NEW_1", MON, NEW, 3, Level.UNDERGROUND, Color.BLUE),
            new Route("MON_SAU_1", MON, SAU, 5, Level.OVERGROUND, Color.BLACK),
            new Route("MON_TOR_1", MON, TOR, 3, Level.OVERGROUND, null),
            new Route("NAS_PIT_1", NAS, PIT, 4, Level.OVERGROUND, Color.YELLOW),
            new Route("NAS_RAL_1", NAS, RAL, 3, Level.OVERGROUND, Color.BLACK),
            new Route("NAS_SAI_1", NAS, SAI, 2, Level.UNDERGROUND, null),
            new Route("NEW_PIT_1", NEW, PIT, 2, Level.OVERGROUND, Color.WHITE),
            new Route("NEW_PIT_2", NEW, PIT, 2, Level.OVERGROUND, Color.GREEN),
            new Route("NEW_WAS_1", NEW, WAS, 2, Level.OVERGROUND, Color.ORANGE),
            new Route("NEW_WAS_2", NEW, WAS, 2, Level.OVERGROUND, Color.BLACK),
            new Route("OKL_SANT_1", OKL, SANT, 3, Level.OVERGROUND, Color.BLUE),
            new Route("PHO_SANT_1", PHO, SANT, 3, Level.OVERGROUND, null),
            new Route("PIT_RAL_1", PIT, RAL, 2, Level.OVERGROUND, null),
            new Route("PIT_SAI_1", PIT, SAI, 5, Level.OVERGROUND, Color.GREEN), // MAY CHANGE
            new Route("PIT_TOR_1", PIT, TOR, 2, Level.UNDERGROUND, null),
            new Route("PIT_WAS_1", PIT, WAS, 2, Level.OVERGROUND, null),
            new Route("POR_SAL_1", POR, SAL, 6, Level.OVERGROUND, Color.BLUE),
            new Route("POR_SANF_1", POR, SANF, 5, Level.OVERGROUND, Color.GREEN),
            new Route("POR_SANF_2", POR, SANF, 5, Level.OVERGROUND, Color.VIOLET),
            new Route("POR_SEA_1", POR, SEA, 1, Level.UNDERGROUND, null),
            new Route("POR_SEA_2", POR, SEA, 1, Level.UNDERGROUND, null),
            new Route("RAL_WAS_1", RAL, WAS, 2, Level.OVERGROUND, null),
            new Route("RAL_WAS_2", RAL, WAS, 2, Level.OVERGROUND, null),
            new Route("SAL_SANF_1", SAL, SANF, 5, Level.OVERGROUND, Color.ORANGE),
            new Route("SAL_SANF_2", SAL, SANF, 5, Level.OVERGROUND, Color.WHITE),
            new Route("SAU_TOR_1", SAU, TOR, 2, Level.UNDERGROUND, null),
            new Route("SAU_WIN_1", SAU, WIN, 6, Level.UNDERGROUND, null),
            new Route("SEA_VAN_1", SEA, VAN, 1, Level.UNDERGROUND, null),
            new Route("SEA_VAN_2", POR, SEA, 1, Level.UNDERGROUND, null));


    // Tickets
    private static final List<Ticket> ALL_TICKETS = List.of(
            // City-to-city tickets
            new Ticket(BOS, MIA, 12),
            new Ticket(CAL, PHO, 13),
            new Ticket(CAL, SAL, 7),
            new Ticket(CHI, NOU, 7),
            new Ticket(CHI, SANT, 9),
            new Ticket(DAL, NEW, 11),
            new Ticket(DEN, ELP, 4),
            new Ticket(DEN, PIT, 11),
            new Ticket(DUL, ELP, 10),
            new Ticket(DUL, HOU, 8),
            new Ticket(HEL, LOS, 8),
            new Ticket(KAN, HOU, 5),
            new Ticket(LOS, CHI, 16),
            new Ticket(LOS, MIA, 20),
            new Ticket(LOS, NEW, 21),
            new Ticket(MON, ATL, 9),
            new Ticket(MON, NOU, 13),
            new Ticket(NEW, ATL, 6),
            new Ticket(POR, NAS, 17),
            new Ticket(POR, PHO, 11),
            new Ticket(SANF, ATL, 11),
            new Ticket(SAU, NAS, 8),
            new Ticket(SAU, OKL, 9),
            new Ticket(SEA, LOS, 9),
            new Ticket(SEA, NEW, 22),
            new Ticket(TOR, MIA, 10),
            new Ticket(VAN, MON, 20),
            new Ticket(VAN, SANT, 13),
            new Ticket(WIN, HOU, 12),
            new Ticket(WIN, LIT, 11));

}
