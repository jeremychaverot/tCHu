package ch.epfl.tchu.net;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.regex.Pattern;

import ch.epfl.tchu.game.*;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Player.TurnKind;

/**
 * A class that contains all the useful serdes of the project as attributes.
 * @author Cristian Safta (324694)
 * @author Jérémy Chaverot (315858)
 */
public final class Serdes {

    /**
     * Serde that serves to (de)encode an integer value (Integer).
     */
    public static final Serde<Integer> integerSerde = Serde.<Integer>of(t -> Integer.toString(t),
        k -> Integer.parseInt(k));

    /**
     * Serde that serves to (de)encode a string (String).
     */
    public static final Serde<String> stringSerde = Serde.<String>of(a -> {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(a.getBytes(StandardCharsets.UTF_8));
    }, b -> {
        Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(b), StandardCharsets.UTF_8);
    });

    /**
     * Serde that serves to (de)encode a player id (PlayerId).
     */
    public static final Serde<PlayerId> playerIdSerde = Serde.<PlayerId>oneOf(PlayerId.ALL);

    /**
     * Serde that serves to (de)encode a turn kind (TurnKind).
     */
    public static final Serde<TurnKind> turnKindSerde = Serde.<TurnKind>oneOf(TurnKind.ALL);

    /**
     * Serde that serves to (de)encode a card (Card).
     */
    public static final Serde<Card> cardSerde = Serde.<Card>oneOf(Card.ALL);

    /**
     * Serde that serves to (de)encode a route (Route).
     */
    public static final Serde<Route> routeSerde = Serde.<Route>oneOf(UsaMap.routes());

    /**
     * Serde that serves to (de)encode a station (Station).
     */
    public static final Serde<Station> stationSerde = Serde.<Station>oneOf(UsaMap.stations());

    /**
     * Serde that serves to (de)encode a ticket (Ticket).
     */
    public static final Serde<Ticket> ticketSerde = Serde.<Ticket>oneOf(UsaMap.tickets());

    /**
     * Serde that serves to (de)encode a list of strings (List<String>).
     */
    public static final Serde<List<String>> stringListSerde = Serde.<String>listOf(stringSerde, ",");

    /**
     * Serde that serves to (de)encode a list of cards (List<Card>).
     */
    public static final Serde<List<Card>> cardListSerde = Serde.<Card>listOf(cardSerde, ",");

    /**
     * Serde that serves to (de)encode a list of routes (List<Route>).
     */
    public static final Serde<List<Route>> routeListSerde = Serde.<Route>listOf(routeSerde, ",");

    /**
     * Serde that serves to (de)encode a sorted bag of cards (SortedBag<Card>).
     */
    public static final Serde<SortedBag<Card>> cardSortedBagSerde = Serde.<Card>bagOf(cardSerde, ",");

    /**
     * Serde that serves to (de)encode a sorted bag of tickets (SortedBag<Ticket>).
     */
    public static final Serde<SortedBag<Ticket>> ticketSortedBagSerde = Serde.<Ticket>bagOf(ticketSerde, ",");

    /**
     * Serde that serves to (de)encode a list of sorted bags of tickets (List<SortedBag<Card>>).
     */
    public static final Serde<List<SortedBag<Card>>> listOfSortedBagOfCardsSerde = Serde.<SortedBag<Card>>listOf(cardSortedBagSerde, ";");

    /**
     * Serde that serves to (de)encode a trail (Trail).
     */
    public static final Serde<Trail> trailSerde = Serde.<Trail>of(a -> routeListSerde.serialize(a.routes()) + "*"
                    + stationSerde.serialize(a.station1()) + "*" + stationSerde.serialize(a.station2()),
            b -> {
                String[] s = b.split(Pattern.quote("*"), -1);
                return new Trail(routeListSerde.deserialize(s[0]), stationSerde.deserialize(s[1]), stationSerde.deserialize(s[2]));
            }
    );

    /**
     * Serde that serves to (de)encode a public card state (PublicCardState).
     */
    public static final Serde<PublicCardState> publicCardStateSerde = Serde.<PublicCardState>of(a ->
        cardListSerde.serialize(a.faceUpCards())+ ";" + integerSerde.serialize(a.deckSize()) + ";"
                + integerSerde.serialize(a.discardsSize()), b -> {
            String[] s = b.split(Pattern.quote(";"), -1);
            return new PublicCardState(cardListSerde.deserialize(s[0]), integerSerde.deserialize(s[1]),
                    integerSerde.deserialize(s[2]));
    });

    /**
     * Serde that serves to (de)encode a public player state (PublicPlayerState).
     */
    public static final Serde<PublicPlayerState> publicPlayerStateSerde = Serde.<PublicPlayerState>of(a -> {
        String serialized = integerSerde.serialize(a.ticketCount()) + ";" + integerSerde.serialize(a.cardCount()) + ";"
                + routeListSerde.serialize(a.routes()) + ";";
        return (a.longestTrail() != null)
                ? serialized + trailSerde.serialize(a.longestTrail())
                : serialized;
        }, b -> {
                String[] s = b.split(Pattern.quote(";"), -1);
                if(!s[3].isEmpty())
                    return new PublicPlayerState(integerSerde.deserialize(s[0]), integerSerde.deserialize(s[1]),
                            routeListSerde.deserialize(s[2]), trailSerde.deserialize(s[3]));
            return new PublicPlayerState(integerSerde.deserialize(s[0]), integerSerde.deserialize(s[1]),
                    routeListSerde.deserialize(s[2]), null); // In case longestTrail is null
        }
    );

    /**
     * Serde that serves to (de)encode a player state (PlayerState).
     */
    public static final Serde<PlayerState> playerStateSerde = Serde.<PlayerState>of(a -> {
        String serialized = ticketSortedBagSerde.serialize(a.tickets()) + ";" + cardSortedBagSerde.serialize(a.cards()) + ";"
                + routeListSerde.serialize(a.routes()) + ";";
        return (a.longestTrail() != null)
                ? serialized + trailSerde.serialize(a.longestTrail())
                : serialized;
        }, b -> {
                String[] s = b.split(Pattern.quote(";"), -1);
                if(!s[3].isEmpty())
                    return new PlayerState(ticketSortedBagSerde.deserialize(s[0]), cardSortedBagSerde.deserialize(s[1]),
                            routeListSerde.deserialize(s[2]), trailSerde.deserialize(s[3]));
            return new PlayerState(ticketSortedBagSerde.deserialize(s[0]), cardSortedBagSerde.deserialize(s[1]),
                    routeListSerde.deserialize(s[2]), null); // In case longestTrail is null
        }
    );

    /**
     * Serde that serves to (de)encode a public game state (PublicGameState).
     */
    public static final Serde<PublicGameState> publicGameStateSerde = Serde.<PublicGameState>of(a -> {
        String playerStates = "";
            for (int i = 0 ; i < a.numberOfPlayers() ; i++)
                playerStates = playerStates.concat(publicPlayerStateSerde.serialize(a.playerState(PlayerId.ALL.get(i))) + ":");

        String serialized = integerSerde.serialize(a.numberOfPlayers()) + ":"
                + integerSerde.serialize(a.ticketsCount()) + ":"
                + publicCardStateSerde.serialize(a.cardState()) + ":"
                + playerIdSerde.serialize(a.currentPlayerId()) + ":"
                + playerStates;
        return (a.lastPlayer() != null)
                ? serialized + playerIdSerde.serialize(a.lastPlayer())
                : serialized;
        }, b -> {
        String[] s = b.split(Pattern.quote(":"), -1);
        int numberOfPlayers = Integer.parseInt(s[0]);
        Map<PlayerId, PublicPlayerState> playerState = new HashMap<>();
            for (int i = 0 ; i < numberOfPlayers; i++)
                playerState.put(PlayerId.ALL.get(i), publicPlayerStateSerde.deserialize(s[4 + i]));
        if (!s[4 + numberOfPlayers].isEmpty())
            return new PublicGameState(integerSerde.deserialize(s[1]), publicCardStateSerde.deserialize(s[2]),
                    playerIdSerde.deserialize(s[3]), playerState, playerIdSerde.deserialize(s[4 + numberOfPlayers]));
        return new PublicGameState(integerSerde.deserialize(s[1]), publicCardStateSerde.deserialize(s[2]),
                playerIdSerde.deserialize(s[3]), playerState, null); // In case lastPlayerId is null
    });

    /**
     * Private empty constructor because irrelevant.
     */
    private Serdes() {}

}

