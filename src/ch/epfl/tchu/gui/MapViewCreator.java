package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers.ChooseCardsHandler;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * MapViewCreator is a package-private class that creates the view of the map.
 * @author Jérémy Chaverot (315858)
 */
final class MapViewCreator {

    @FunctionalInterface
    interface CardChooser {

        /**
         * Called when a player needs to choose the cards he wants to use to claim a route.
         * @param options (List<SortedBag<Card>>) : list of the possible set of cards he can use.
         * @param handler (ChooseCardsHandler) : intended to being used when the player makes his choice.
         */
        void chooseCards(List<SortedBag<Card>> options, ChooseCardsHandler handler);
    }

    /**
     * Allow to create a view of the map.
     * @param gameState (ObservableGameState) : observable state of the game.
     * @param claimRouteHP (ObjectProperty<ClaimRouteHandler>) : property containing the handler to use when a player
     *                 wants to claim of a route.
     * @param cardChooser (CardChooser) : card selector.
     */
    public static Pane createMapView(ObservableGameState gameState, ObjectProperty<ClaimRouteHandler> claimRouteHP,
                                     CardChooser cardChooser) {

        Pane mapView = new Pane();
        mapView.getStylesheets().addAll("usamap.css", "colors.css");
        ImageView map = new ImageView();
        mapView.getChildren().add(map);

        List<Group> allRouteGroup = new ArrayList<>();

        for (int i = 0 ; i < UsaMap.routes().size() ; i++) {
            allRouteGroup.add(new Group());
            Group routeGroup = allRouteGroup.get(i);
            Route route = UsaMap.routes().get(i);

            routeGroup.setId(route.id());
            routeGroup.getStyleClass().addAll("route", route.level().name());
            routeGroup.getStyleClass().add(route.color() == null ? "NEUTRAL" : route.color().toString());

            List<Group> allBoxGroup = new ArrayList<>();

            for (int j = 0 ; j < route.length() ; j++) {
                allBoxGroup.add(new Group());
                Group boxGroup = allBoxGroup.get(j);
                boxGroup.setId(route.id() + "_" + (j+1));

                Rectangle rectangle1 = new Rectangle(24, 8);
                Group way = new Group(rectangle1);
                rectangle1.getStyleClass().addAll("track", "filled");

                Rectangle rectangle2 = new Rectangle(24, 8);
                Circle circle1 = new Circle((rectangle2.getWidth() / 2) - (rectangle2.getWidth() / 4), rectangle2.getHeight() / 2, 2);
                Circle circle2 = new Circle((rectangle2.getWidth() / 2) + (rectangle2.getWidth() / 4), rectangle2.getHeight() / 2, 2);
                Group car = new Group(rectangle2, circle1, circle2);
                car.getStyleClass().add("car");
                rectangle2.getStyleClass().add("filled");
                way.getChildren().add(car);
                boxGroup.getChildren().add(way);
            }

            for (Group boxGroup : allBoxGroup) {
                routeGroup.getChildren().add(boxGroup);
            }

            gameState.routes(route).addListener((o, oV, nV) -> {
                routeGroup.getStyleClass().add(nV.toString());
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), routeGroup);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setCycleCount(4);
                fadeTransition.setAutoReverse(true);
                fadeTransition.play();
            });

            routeGroup.setOnMouseEntered(e -> {
                if (!routeGroup.isDisable()) Sounds.playTickSound();
            });
            routeGroup.disableProperty().bind(claimRouteHP.isNull().or(gameState.claimable(route).not()));
            routeGroup.setOnMouseClicked(e -> {
                List<SortedBag<Card>> possibleClaimCards = gameState.possibleClaimCards(route);
                ClaimRouteHandler claimRouteH = claimRouteHP.get();
                if (possibleClaimCards.size() == 1)
                    claimRouteH.onClaimRoute(route, possibleClaimCards.get(0));
                else {
                    ChooseCardsHandler chooseCardsH = chosenCards -> claimRouteH.onClaimRoute(route, chosenCards);
                    cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
                }
            });

            mapView.getChildren().add(routeGroup);
        }

        for (PlayerId p : PlayerId.ALL) {
            gameState.longestTrail(p).addListener((o, oV, nV) -> {
                List<Route> routes = nV.routes();
                List<String> routesId = new ArrayList<>();
                routes.forEach(r -> routesId.add(r.id()));
                for (Group g : allRouteGroup) {
                    var list = g.getStyleClass();
                    if (list.size() == 3) {
                        list.set(2, "WHITE"); // When no one possesses the route
                    } else if (routesId.contains(g.getId())) {
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), g);
                        fadeTransition.setFromValue(1.0);
                        fadeTransition.setToValue(0.0);
                        fadeTransition.setCycleCount(Animation.INDEFINITE);
                        fadeTransition.setAutoReverse(true);
                        fadeTransition.play();
                    }
                }
            });
        }

        return mapView;
    }

    /**
     * Empty private constructor because irrelevant.
     */
    private MapViewCreator() {}

}
