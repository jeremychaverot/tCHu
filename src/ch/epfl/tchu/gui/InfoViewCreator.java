package ch.epfl.tchu.gui;

import javafx.beans.binding.Bindings;
import javafx.collections.*;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.tchu.game.*;

/**
 * Package-private class that contains a method used to create the information view.
 * @author Cristian Safta (324694)
 */
final public class InfoViewCreator {

    /**
     * Static method that created the information view.
     * @param playerId (PlayerId) : the id of the player.
     * @param playerNames (Map<PlayerId,String>) : the names of the players.
     * @param gameState (ObservableGameState) : the state of the game.
     * @param gameInfo (ObservableList<Text>) : the info about the current game events.
     * @return pane (Pane) : the info view desired.
     */
    public static Pane createInfoView(PlayerId playerId, Map<PlayerId,String> playerNames, ObservableGameState gameState,
                                      ObservableList<Text> gameInfo) {
        List<PlayerId> allPlayers = PlayerId.ALL.subList(0, playerNames.size());
        VBox infoView = new VBox();
        infoView.getStylesheets().addAll("info.css", "colors.css");
        PlayerId identity = playerId;
        if (allPlayers.size() == 2) {
            VBox playerStats = new VBox();
            playerStats.setId("player-stats");

            for (int i = 0; i < allPlayers.size(); i++) {
                playerStats.getChildren().add(createPlayerInfoText(gameState, identity, playerNames.get(identity)));
                identity = identity.next(allPlayers);
            }
            infoView.getChildren().add(playerStats);
        } else {
            VBox playerStats1 = new VBox();
            playerStats1.setId("player-stats");
            VBox playerStats2 = new VBox();
            playerStats2.setId("player-stats");

            int half = (int) Math.ceil(((double) allPlayers.size()) / 2);
            for (int i = 0 ; i < half ; i++) {
                playerStats1.getChildren().add(createPlayerInfoText(gameState, identity, playerNames.get(identity)));
                identity = identity.next(allPlayers);
            }

            Separator sepVert = new Separator();
            sepVert.setOrientation(Orientation.VERTICAL);

            for (int i = half ; i < allPlayers.size() ; i++) {
                playerStats2.getChildren().add(createPlayerInfoText(gameState, identity, playerNames.get(identity)));
                identity = identity.next(allPlayers);
            }

            BorderPane borderPane = new BorderPane(sepVert, null, playerStats2, null, playerStats1);
            infoView.getChildren().add(borderPane);
        }

        Separator sep = new Separator();
        sep.setOrientation(Orientation.HORIZONTAL);

        TextFlow gameInformation = new TextFlow();
        gameInformation.setId("game-info");
        Bindings.bindContent(gameInformation.getChildren(), gameInfo);

        infoView.getChildren().addAll(sep, gameInformation);

        return infoView; 
    }

    /**
     * Helper method that creates the info text for one player.
     * @param gameState (ObservableGameState) : the state of the game.
     * @param playerId (PlayerId) : the player we want to create the info text of.
     * @param name (String) : name of the player.
     * @return text (TextFlow) : the info text of the player, to be put on the GUI.
     */
    private static TextFlow createPlayerInfoText(ObservableGameState gameState, PlayerId playerId, String name) {
        TextFlow playerTxt = new TextFlow();
        playerTxt.getStyleClass().add(playerId.toString());
        Circle circle = new Circle(5);
        circle.getStyleClass().add("filled");
        Text text = new Text();
        text.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS, name,
                gameState.ticketCount(playerId), gameState.cardCount(playerId),
                gameState.carCount(playerId), gameState.points(playerId)));
        playerTxt.getChildren().addAll(circle, text);

        return playerTxt;
    }

    /**
     * Empty private constructor because irrelevant.
     */
    private InfoViewCreator() {}

}
