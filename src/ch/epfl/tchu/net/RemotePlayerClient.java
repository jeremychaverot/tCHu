package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.io.*;
import java.net.Socket;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * RemotePlayerClient creates client of a remote player, which is to be connected to the proxy.
 * @author Jérémy Chaverot (315858)
 */
public final class RemotePlayerClient {

    private final Player player;
    private final Socket socket;

    /**
     * Public constructor.
     * @param player (Player) : player who is given remote access.
     * @param name (String) : name used to connect to the proxy.
     * @param port (int) : port number used to connect to the proxy.
     * @throws UncheckedIOException if an unchecked exception is found.
     */
    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        try {
            socket = new Socket(name, port);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * When this method is run, allows to realize the exchanges with the proxy.
     * @throws UncheckedIOException if an unchecked exception is found.
     */
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII))) {

            String message;
            while ((message = reader.readLine()) != null) {
                String[] splitString = message.split(Pattern.quote(" "), -1);

                switch (MessageId.valueOf(splitString[0])) {

                    case INIT_PLAYERS:
                        PlayerId ownId = Serdes.playerIdSerde.deserialize(splitString[1]);
                        List<String> names = Serdes.stringListSerde.deserialize(splitString[2]);
                        List<PlayerId> allPlayers = PlayerId.ALL.subList(0, names.size());
                        Map<PlayerId, String> playerNames = new HashMap<>();
                            for (int i = 0 ; i < allPlayers.size() ; i++)
                                    playerNames.put(allPlayers.get(i), names.get(i));
                        player.initPlayers(ownId, playerNames);
                        break;

                    case RECEIVE_INFO:
                        String info = Serdes.stringSerde.deserialize(splitString[1]);
                        player.receiveInfo(info);
                        break;

                    case UPDATE_STATE:
                        PublicGameState newState = Serdes.publicGameStateSerde.deserialize(splitString[1]);
                        PlayerState ownState = Serdes.playerStateSerde.deserialize(splitString[2]);
                        player.updateState(newState, ownState);
                        break;

                    case SET_INITIAL_TICKETS:
                        SortedBag<Ticket> allInitialTickets = Serdes.ticketSortedBagSerde.deserialize(splitString[1]);
                        player.setInitialTicketChoice(allInitialTickets);
                        break;

                    case CHOOSE_INITIAL_TICKETS:
                        SortedBag<Ticket> chosenInitialTickets = player.chooseInitialTickets();
                        sendMessage(writer, Serdes.ticketSortedBagSerde.serialize(chosenInitialTickets));
                        break;

                    case NEXT_TURN:
                        Player.TurnKind nextTurn = player.nextTurn();
                        sendMessage(writer, Serdes.turnKindSerde.serialize(nextTurn));
                        break;

                    case CHOOSE_TICKETS:
                        SortedBag<Ticket> allTickets = Serdes.ticketSortedBagSerde.deserialize(splitString[1]);
                        SortedBag<Ticket> chosenTickets = player.chooseTickets(allTickets);
                        sendMessage(writer, Serdes.ticketSortedBagSerde.serialize(chosenTickets));
                        break;

                    case DRAW_SLOT:
                        int slot = player.drawSlot();
                        sendMessage(writer, Serdes.integerSerde.serialize(slot));
                        break;

                    case ROUTE:
                        Route route = player.claimedRoute();
                        sendMessage(writer, Serdes.routeSerde.serialize(route));
                        break;

                    case CARDS:
                        SortedBag<Card> cards = player.initialClaimCards();
                        sendMessage(writer, Serdes.cardSortedBagSerde.serialize(cards));
                        break;

                    case CHOOSE_ADDITIONAL_CARDS:
                        List<SortedBag<Card>> allCards = Serdes.listOfSortedBagOfCardsSerde.deserialize(splitString[1]);
                        SortedBag<Card> chosenCards = player.chooseAdditionalCards(allCards);
                        sendMessage(writer, Serdes.cardSortedBagSerde.serialize(chosenCards));
                        break;

                    default:
                        throw new Error();
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // Helper method to send a message through the buffered writer.
    private void sendMessage(BufferedWriter w, String serializedMessage) {
        try {
            w.write(serializedMessage);
            w.write('\n');
            w.flush();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
