package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * Class that contains five nested functional interfaces representing different action managers.
 * @author Cristian Safta (324694)
 */
public interface ActionHandlers {
    
    @FunctionalInterface
    interface DrawTicketsHandler {

        /**
         * Method called when the player wished to draw tickets.
         */
        abstract void onDrawTickets();
    }
    
    @FunctionalInterface
    interface DrawCardHandler {

        /**
         * Method being called when the player wishes to draw a card form a given slot.
         * @param slot (int) : the slot from which the player wishes to draw the card(0 to 4, or -1 for the draw pile).
         */
        abstract void onDrawCard(int slot);
    }
    
    @FunctionalInterface
    interface ClaimRouteHandler {

        /**
         * Method being called when the player tries to claim a route.
         * @param route (Route) : the route the player tries to claim.
         * @param initialClaimCards (SortedBag<Card>) : the initial cards the player uses to claim the route.
         */
        abstract void onClaimRoute(Route route, SortedBag<Card> initialClaimCards);
    }
    
    @FunctionalInterface
    interface ChooseTicketsHandler {

        /**
         * Method being called when the player chooses to keep some tickets.
         * @param tickets (SortedBag<Ticket>) : the tickets the player chooses to keep.
         */
        abstract void onChooseTickets(SortedBag<Ticket> tickets);
    }
    
    @FunctionalInterface
    interface ChooseCardsHandler {

        /**
         * Method that is called when the player has chosen to use the given cards as initial or additional cards
         * when taking possession of a route.
         * @param cards (SortedBag<Card>) : the cards the player uses.
         */
        abstract void onChooseCards(SortedBag<Card> cards);
    }
    
}
