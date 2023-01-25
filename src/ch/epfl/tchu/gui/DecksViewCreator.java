package ch.epfl.tchu.gui;

import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Package-private class that contains methods used to create the view of the hand of cards and the deck along
 * with the face-up cards.
 * @author Cristian Safta (324694)
 */
final class DecksViewCreator {
    private static SimpleBooleanProperty animationRunning = new SimpleBooleanProperty(false);
    private static ArrayList<Integer> indexesOfRunningAnimations = new ArrayList<Integer>();

    private static String styleClassToAdd = "";
    private static String styleClassToRemove = "";
    private static boolean styleClassChangeNeeded = false;

    private static List<StackPane> faceUpCards = new ArrayList<StackPane>();
    private static List<StackPane> handCards = new ArrayList<StackPane>();

    /**
     * Method that creates the visual representation of the hand of the player.
     * @param gameState (ObservableGameState) : the observable game state we create the view of.
     * @return pane (Pane) : the visual representation of the hand of the player.
     */
    public static Pane createHandView(ObservableGameState gameState) {
        HBox handView = new HBox();
        handView.getStylesheets().addAll("decks.css", "colors.css");

        ListView<Ticket> ticketListView = new ListView<>(gameState.tickets());
        ticketListView.setMaxHeight(100);

        HBox handPane = new HBox();
        handPane.setId("hand-pane");

        for (Card c : Card.ALL) {
            StackPane cardPane = cardPane(gameState, c);
            handPane.getChildren().add(cardPane);
            handCards.add(cardPane);
        }

        handView.getChildren().addAll(ticketListView, handPane);

        return handView;
    }

    /**
     * Method that returns the visual representation of the view of cards.
     * @param gameState (ObservableGameState) : the observable game state we create the view of.
     * @param drawTickets (ObjectProperty<DrawTicketsHandler>) : the handler called when the player draws tickets.
     * @param drawCard (ObjectProperty<DrawCardHandler>) : the handler that is called when the player draws cards.
     * @return pane (Pane) : the visual representation of the view of cards.
     */
    public static Pane createCardsView(ObservableGameState gameState, ObjectProperty<DrawTicketsHandler> drawTickets,
                                       ObjectProperty<DrawCardHandler> drawCard) {
        VBox cardsView = new VBox();
        cardsView.setId("card-pane");
        cardsView.getStylesheets().addAll("decks.css", "colors.css");

        Button ticketButton = new Button(StringsFr.TICKETS);
        ticketButton.getStyleClass().add("gauged");
        Rectangle ticketRect1 = new Rectangle(50, 5);
        ticketRect1.getStyleClass().add("background");
        Rectangle ticketRect2 = new Rectangle(50, 5);
        ticketRect2.getStyleClass().add("foreground");
        ticketRect2.widthProperty().bind(gameState.ticketsPercentage().multiply(50).divide(100));
        ticketButton.setGraphic(new Group(ticketRect1, ticketRect2));
        cardsView.getChildren().add(ticketButton);
        ticketButton.disableProperty().bind(drawTickets.isNull());
        ticketButton.setOnMouseClicked(e -> {
            Sounds.playTickSound();
            DrawTicketsHandler drawTicketsH = drawTickets.get();
            drawTicketsH.onDrawTickets();
        });

        for (int i = 0 ; i < Constants.FACE_UP_CARDS_COUNT ; i++) {
            StackPane cardPane = cardPane(gameState, i, drawCard);
            cardsView.getChildren().add(cardPane);
            faceUpCards.add(cardPane);
        }

        Button cardButton = new Button(StringsFr.CARDS);
        cardButton.getStyleClass().add("gauged");
        Rectangle cardRect1 = new Rectangle(50, 5);
        cardRect1.getStyleClass().add("background");
        Rectangle cardRect2 = new Rectangle(50, 5);
        cardRect2.getStyleClass().add("foreground");
        cardRect2.widthProperty().bind(gameState.cardsPercentage().multiply(50).divide(100));
        cardButton.setGraphic(new Group(cardRect1, cardRect2));
        cardsView.getChildren().add(cardButton);
        cardButton.disableProperty().bind(drawCard.isNull());
        cardButton.setOnMouseClicked(e -> {
            Sounds.playTickSound();
            DrawCardHandler drawCardH = drawCard.get();
            drawCardH.onDrawCard(Constants.DECK_SLOT);
        });

        return cardsView;
    }


    // Helper methods to build the cards' stack pane

    // For the cards inside the player's hand
    private static StackPane cardPane(ObservableGameState gameState, Card cardType) {
        StackPane stackPane = new StackPane();
        ReadOnlyIntegerProperty count = gameState.cards(cardType);

        gameState.cards(cardType).addListener((o, oV, nV) -> {
            Sounds.playDrawCardSound();
            var index = Card.ALL.indexOf(cardType);
            if (!indexesOfRunningAnimations.contains(index)) {
                indexesOfRunningAnimations.add(index);
                animateHandCard(index);
            }
        });

        stackPane.getStyleClass().add("card");
        stackPane.getStyleClass().add(cardType == Card.LOCOMOTIVE ? "NEUTRAL" : cardType.toString());

        Text txt = new Text(Integer.toString(count.get()));
        txt.getStyleClass().add("count");
        txt.textProperty().bind(Bindings.convert(gameState.cards(cardType)));
        txt.visibleProperty().bind(Bindings.greaterThan(count, 1));

        addCardLayout(stackPane);
        stackPane.getChildren().add(txt);
        stackPane.visibleProperty().bind(Bindings.greaterThan(count, 0));

        return stackPane;
    }

    // For the cards inside the deck of face up cards
    private static StackPane cardPane(ObservableGameState gameState, int index, ObjectProperty<DrawCardHandler> drawCard) {
        StackPane stackPane = new StackPane();

        stackPane.getStyleClass().add("card");
        gameState.faceUpCard(index).addListener(
                (o, oV, nV) -> {

                    if (!animationRunning.getValue()) {
                        stackPane.getStyleClass().add(nV == Card.LOCOMOTIVE ? "NEUTRAL" : nV.toString());
                        if (oV != null)
                            stackPane.getStyleClass().remove(oV == Card.LOCOMOTIVE ? "NEUTRAL" : oV.toString());
                    } else {
                        styleClassChangeNeeded = true;
                        styleClassToAdd = nV == Card.LOCOMOTIVE ? "NEUTRAL" : nV.toString();
                        if (oV != null)
                            styleClassToRemove = oV == Card.LOCOMOTIVE ? "NEUTRAL" : oV.toString();
                        else styleClassToRemove = "";

                    }
                });
        addCardLayout(stackPane);
        stackPane.disableProperty().bind(Bindings.or(drawCard.isNull(), animationRunning));
        stackPane.setOnMouseClicked(e -> {
            DrawCardHandler drawCardH = drawCard.get();
            drawCardH.onDrawCard(index);
        });

        return stackPane;
    }

    /**
     * Private method that helps to add a card layout.
     * @param pane (StackPane) : the stack pane we want to add the card layout to.
     */
    private static void addCardLayout(StackPane pane) {
        Rectangle outRectangle = new Rectangle(60, 90);
        outRectangle.getStyleClass().add("outside");
        Rectangle inRectangle = new Rectangle(40, 70);
        inRectangle.getStyleClass().addAll("filled", "inside");
        Rectangle pngRectangle = new Rectangle(40, 70);
        pngRectangle.getStyleClass().add("train-image");

        pane.getChildren().addAll(outRectangle, inRectangle, pngRectangle);
    }

    //Animation methods
    public static void animateHandCard(int index) {
        FadeTransition t1 = new FadeTransition(Duration.millis(500));
        t1.setFromValue(0);
        t1.setToValue(1);
        t1.setCycleCount(1);
        t1.setAutoReverse(false);

        ScaleTransition t2 = new ScaleTransition(Duration.millis(300));
        t2.setByX(0.5);
        t2.setByY(0.5);
        t2.setCycleCount(2);
        t2.setAutoReverse(true);

        ParallelTransition animation = new ParallelTransition(t1,t2);
        animation.setNode(handCards.get(index));
        animation.setOnFinished(a->{
            indexesOfRunningAnimations.remove(Integer.valueOf(index));
        });

        animation.play();

    }


    public static void animateCard(int index) {
        //transition1(moving to the right)
        TranslateTransition t1 = new TranslateTransition();
        t1.setDuration(Duration.millis(1000));
        t1.setByX(100);
        t1.setCycleCount(1);
        t1.setAutoReverse(false);

        FadeTransition t2 = new FadeTransition(Duration.seconds(0.1),faceUpCards.get(index));
        t2.setFromValue(1.0);
        t2.setToValue(0.0);
        t2.setAutoReverse(false);

        TranslateTransition t3 = new TranslateTransition();
        t3.setDuration(Duration.seconds(0.1));
        t3.setByX(-100);
        t3.setCycleCount(1);
        t3.setAutoReverse(false);



        SequentialTransition animation1 = new SequentialTransition(t1,t2,t3);
        animation1.setNode(faceUpCards.get(index));
        animation1.setOnFinished(a->{

            animationRunning.set(false);
            if(styleClassChangeNeeded) {
                faceUpCards.get(index).getStyleClass().add(styleClassToAdd);
                if(!styleClassToRemove.isEmpty()) {
                    faceUpCards.get(index).getStyleClass().remove(styleClassToRemove);
                }
            }
            styleClassChangeNeeded = false;
            FadeTransition t4 = new FadeTransition();
            t4.setDuration(Duration.millis(2000));
            t4.setNode(faceUpCards.get(index));
            t4.setFromValue(0);
            t4.setToValue(1);
            t4.setCycleCount(1);
            t4.setAutoReverse(false);
            t4.play();
        });
        animationRunning.set(true);
        animation1.play();

    }

    /**
     * Empty private constructor because irrelevant.
     */
    private DecksViewCreator() {}

}
