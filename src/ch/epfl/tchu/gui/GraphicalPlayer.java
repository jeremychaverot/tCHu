package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.ChatSystem;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GraphicalPlayer represents the graphical user interface (GUI) of a player of tCHu.
 * @author Jérémy Chaverot (315858)
 */
public final class GraphicalPlayer {

    private final ObservableGameState gameState;
    private final ObservableList<Text> info;
    private final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerP;
    private final ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerP;
    private final ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerP;
    private final Stage ticketChoiceStage;
    private final Stage cardChoiceStage;
    private final Stage addCardChoiceStage;
    private final List<Stage> stageList;
    private final ListView<Ticket> ticketListView;
    private final ListView<SortedBag<Card>> cardListView;
    private final ListView<SortedBag<Card>> addCardListView;
    private final Button ticketButton;
    private final Button cardButton;
    private final Button addCardButton;
    private final Text ticketText;
    private final Text cardText;
    private final Text addCardText;
    private final TextArea chatTextArea;
    private final List<PlayerId> allPlayers;
    private final String name;
    private ChatSystem client;

    /**
     * To create the graphical interface of a player.
     * @param ownId (PlayerId) : identity of the player that will possess the graphical interface.
     * @param playerNames (Map<PlayerId, String>) : name of the players.
     */
    public GraphicalPlayer(PlayerId ownId, Map<PlayerId, String> playerNames) {
        assert Platform.isFxApplicationThread();

        allPlayers = PlayerId.ALL.subList(0, playerNames.size());
        name = playerNames.get(ownId);
        gameState = new ObservableGameState(ownId, allPlayers);
        info = FXCollections.observableArrayList();
        claimRouteHandlerP = new SimpleObjectProperty<>();
        drawTicketsHandlerP = new SimpleObjectProperty<>();
        drawCardHandlerP = new SimpleObjectProperty<>();

        Pane infoView = InfoViewCreator.createInfoView(ownId, playerNames, gameState, info);
        Pane mapView = MapViewCreator.createMapView(gameState, claimRouteHandlerP, (o, h) -> chooseClaimCards(o, h));
        Pane cardsView = DecksViewCreator.createCardsView(gameState, drawTicketsHandlerP, drawCardHandlerP);
        Pane handView = DecksViewCreator.createHandView(gameState);

        BorderPane mainPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("tCHu" + " \u2014 " + name);
        primaryStage.show();


        ticketText = new Text();
        TextFlow ticketTextFlow = new TextFlow(ticketText);
        ticketListView = new ListView<>();
        ticketListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ticketButton = new Button(StringsFr.CHOOSE);
        VBox ticketChoice = new VBox(ticketTextFlow, ticketListView, ticketButton);
        Scene ticketChoiceScene = new Scene(ticketChoice);
        ticketChoiceScene.getStylesheets().add("chooser.css");
        ticketChoiceStage = new Stage(StageStyle.UTILITY);
        ticketChoiceStage.setScene(ticketChoiceScene);
        ticketChoiceStage.setTitle(StringsFr.TICKETS_CHOICE);

        cardText = new Text(StringsFr.CHOOSE_CARDS);
        TextFlow cardTextFlow = new TextFlow(cardText);
        cardListView = new ListView<>();
        cardListView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        cardButton = new Button(StringsFr.CHOOSE);
        cardButton.disableProperty().bind(
                Bindings.lessThan(Bindings.size(cardListView.getSelectionModel().getSelectedItems()), 1));
        VBox cardChoice = new VBox(cardTextFlow, cardListView, cardButton);
        Scene cardChoiceScene = new Scene(cardChoice);
        cardChoiceScene.getStylesheets().add("chooser.css");
        cardChoiceStage = new Stage(StageStyle.UTILITY);
        cardChoiceStage.setScene(cardChoiceScene);
        cardChoiceStage.setTitle(StringsFr.CARDS_CHOICE);

        addCardText = new Text(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        TextFlow addCardTextFlow = new TextFlow(addCardText);
        addCardListView = new ListView<>();
        addCardListView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        addCardButton = new Button(StringsFr.CHOOSE);
        VBox addCardChoice = new VBox(addCardTextFlow, addCardListView, addCardButton);
        Scene addCardChoiceScene = new Scene(addCardChoice);
        addCardChoiceScene.getStylesheets().add("chooser.css");
        addCardChoiceStage = new Stage(StageStyle.UTILITY);
        addCardChoiceStage.setScene(addCardChoiceScene);
        addCardChoiceStage.setTitle(StringsFr.CARDS_CHOICE);

        stageList = List.of(ticketChoiceStage, cardChoiceStage, addCardChoiceStage);
        stageList.forEach(s -> {
            s.initOwner(primaryStage);
            s.initModality(Modality.WINDOW_MODAL);
            s.setOnCloseRequest(e -> e.consume());
        });

        chatTextArea = new TextArea();
        chatTextArea.setMaxHeight(100);
        chatTextArea.setEditable(false);
        Text chatText = new Text(StringsFr.SEND_MESSAGE);
        TextFlow chatTextFlow = new TextFlow(chatText);
        TextField chatTextField = new TextField();
        chatTextField.setPromptText("entre ici ton message ....");
        Button chatButton = new Button("Envoyer");
        chatButton.disableProperty().bind(Bindings.isEmpty(chatTextField.textProperty()));
        VBox chatBox = new VBox(chatTextFlow, chatTextArea, chatTextField, chatButton);
        Scene chatScene = new Scene(chatBox);
        chatScene.getStylesheets().add("chooser.css");
        Stage chatStage = new Stage(StageStyle.UTILITY);
        chatStage.setScene(chatScene);
        chatStage.setTitle("Chat");
        chatStage.initOwner(primaryStage);
        Separator sep = new Separator();
        sep.setOrientation(Orientation.HORIZONTAL);
        Button openChatButton = new Button("Chat");
        BooleanProperty isShowing = new SimpleBooleanProperty();
        chatScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                var text = chatTextField.getText();
                if (!text.isEmpty()) {
                    Sounds.playSendMessageSound();
                    client.send(createMessage(text));
                    chatTextField.clear();
                }
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                chatTextField.clear();
                chatStage.hide();
                isShowing.set(false);
            }
        });
        chatButton.setOnAction(e -> {
            Sounds.playSendMessageSound();
            var text = chatTextField.getText();
            client.send(createMessage(text));
            chatTextField.clear();
        });
        HBox chatOpenButtonHbox = new HBox(openChatButton);
        chatOpenButtonHbox.setAlignment(Pos.CENTER);
        chatOpenButtonHbox.setPrefHeight(70);
        openChatButton.setPrefSize(100, 25);
        infoView.getChildren().add(2, chatOpenButtonHbox);
        infoView.getChildren().add(3, sep);
        openChatButton.setOnAction(e -> {
            Sounds.playTickSound();
            chatStage.show();
            isShowing.set(true);
        });
        openChatButton.disableProperty().bind(isShowing);
        chatStage.setOnCloseRequest(e -> {
            chatTextField.clear();
            isShowing.set(false);
        });

    }

    /**
     * To set the chat system.
     * @param chatSystem (ChatSystem) : the chat system of the graphical player.
     */
    public void setChatSystem(ChatSystem chatSystem) {
        this.client = chatSystem;
    }

    /**
     * To print a message in the chat box.
     * @param message (String) : the message to be printed.
     */
    public void receiveMessage(String message) {
        chatTextArea.appendText(message);
    }

    /**
     * To update the game state, and therefore all the properties.
     * @param newPublicGameState (PublicGameState) : the new public game state.
     * @param newPlayerState     (PlayerState) : the new player state of the instance's possessor.
     */
    public void setState(PublicGameState newPublicGameState, PlayerState newPlayerState) {
        assert Platform.isFxApplicationThread();

        gameState.setState(newPublicGameState, newPlayerState);
    }

    /**
     * To add the given message at the bottom of the info list that prints the 5 last infos.
     * @param message (String) : the message we want to add
     */
    public void receiveInfo(String message) {
        assert Platform.isFxApplicationThread();

        if (info.size() == allPlayers.size() + 3) info.remove(info.get(0));
        info.add(new Text(message));
    }

    /**
     * Allows the player to realize one of the given actions.
     * @param claimRouteHandler   (ActionHandlers.ClaimRouteHandler) : the action of claiming a route.
     * @param drawTicketsHandler (ActionHandlers.DrawTicketsHandler) : the action of drawing tickets.
     * @param drawCardHandler       (ActionHandlers.DrawCardHandler) : the action of drawing a card.
     */
    public void startTurn(ActionHandlers.DrawTicketsHandler drawTicketsHandler, ActionHandlers.DrawCardHandler drawCardHandler,
                          ActionHandlers.ClaimRouteHandler claimRouteHandler) {
        assert Platform.isFxApplicationThread();

        if (gameState.canDrawTickets())
            drawTicketsHandlerP.set(
                    () -> {
                        drawTicketsHandler.onDrawTickets();
                        resetActions();
                    });

        if (gameState.canDrawCards())
            drawCardHandlerP.set(
                    slot -> {
                        if (slot != Constants.DECK_SLOT)
                            DecksViewCreator.animateCard(slot);
                        drawCardHandler.onDrawCard(slot);
                        resetActions();
                    });

        claimRouteHandlerP.set(
                (route, initialClaimCards) -> {
                    Sounds.playClaimRouteSound();
                    claimRouteHandler.onClaimRoute(route, initialClaimCards);
                    resetActions();
                });
    }

    /**
     * Allows the player to choose tickets from the given ones.
     * @param tickets (SortedBag<Ticket>) : set of tickets the player has to choose from.
     * @param chooser (ActionHandlers.ChooseTicketsHandler>) : the action of choosing tickets.
     */
    public void chooseTickets(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler chooser) {
        assert Platform.isFxApplicationThread();
        Preconditions.checkArgument(tickets.size() == Constants.IN_GAME_TICKETS_COUNT
                || tickets.size() == Constants.INITIAL_TICKETS_COUNT);

        var drawnTicketCount = tickets.size() - Constants.DISCARDABLE_TICKETS_COUNT;
        ticketText.setText(String.format(StringsFr.CHOOSE_TICKETS, drawnTicketCount, StringsFr.plural(drawnTicketCount)));
        ticketListView.getItems().addAll(tickets.toList());
        ticketButton.disableProperty().bind(
                Bindings.lessThan(Bindings.size(ticketListView.getSelectionModel().getSelectedItems()),
                drawnTicketCount));
        ticketButton.setOnAction(e -> {
            Sounds.playTrivialSound();
            ticketChoiceStage.hide();
            chooser.onChooseTickets(SortedBag.of(ticketListView.getSelectionModel().getSelectedItems()));
            ticketListView.getItems().clear();
        });
        ticketChoiceStage.show();
    }

    /**
     * Allows the player to draw a card from the either the deck, or the face up ones.
     * Method called when the player has already drawn his first card and needs now to draw his second.
     * @param drawCardHandler (ActionHandlers.DrawCardHandler) : the action of drawing a card.
     */
    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler) {
        assert Platform.isFxApplicationThread();

        drawCardHandlerP.set(
                slot -> {
                    if (slot != Constants.DECK_SLOT)
                        DecksViewCreator.animateCard(slot);
                    Sounds.playDrawCardSound();
                    drawCardHandler.onDrawCard(slot);
                    resetActions();
                });
    }

    /**
     * Allows the player to choose a set of cards from the list of the set of possible claiming cards.
     * @param options (List<SortedBag<Card>>) : list of the set of all the possible cards to claim the route.
     * @param chooser (ActionHandlers.ChooseCardsHandler) : the action of choosing a set of cards.
     */
    public void chooseClaimCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler chooser) {
        assert Platform.isFxApplicationThread();
        Preconditions.checkArgument(!options.isEmpty());

        cardListView.getItems().addAll(options);
        cardButton.setOnAction(e -> {
            Sounds.playTrivialSound();
            cardChoiceStage.hide();
            chooser.onChooseCards(cardListView.getSelectionModel().getSelectedItem());
            cardListView.getItems().clear();
        });
        cardChoiceStage.show();
    }

    /**
     * Allows the player to choose a set of cards from the list of set of possible cards to claim a tunnel.
     * @param options (List<SortedBag<Card>>) : list of the set of all the possible cards to claim the tunnel.
     * @param chooser (ActionHandlers.ChooseCardsHandler) : the action of choosing a set of cards.
     */
    public void chooseAdditionalCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler chooser) {
        assert Platform.isFxApplicationThread();
        Preconditions.checkArgument(!options.isEmpty());

        addCardListView.getItems().addAll(options);
        addCardButton.setOnAction(e -> {
            Sounds.playTrivialSound();
            addCardChoiceStage.hide();
            SortedBag<Card> cards = addCardListView.getSelectionModel().getSelectedItem();
            chooser.onChooseCards(cards == null ? SortedBag.of() : cards);
            addCardListView.getItems().clear();
        });
        addCardChoiceStage.show();
    }

    /**
     * To get a more friendly-readable string for a set of cards.
     */
    private static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        @Override
        public String toString(SortedBag<Card> object) {
            return Info.cardsToString(object);
        }

        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Method used to reset to null the action handlers, after the player did one of them for example.
     */
    private void resetActions() {
        drawTicketsHandlerP.set(null);
        drawCardHandlerP.set(null);
        claimRouteHandlerP.set(null);
    }

    private String createMessage(String text) {
        DateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        return String.format("(%s) %s > %s\n", format.format(date), name, text);
    }

}
