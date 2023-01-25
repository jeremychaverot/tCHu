package ch.epfl.tchu.gui;

/**
 * @author Cristian Safta (324694)
 */
public abstract class Sounds {
    
    static Sound drawCardSound = new Sound("resources/drawCard.wav");
    static Sound claimRouteSound = new Sound("resources/trainPassing.wav");
    static Sound trivialSound = new Sound("resources/trivial.wav");
    static Sound backgroundMusic = new Sound("resources/backgroundMusic.wav");
    static Sound tickSound = new Sound("resources/tick.wav");
    static Sound sendMessageSound = new Sound("resources/sendMessage.wav");

    public static void playDrawCardSound() {
        drawCardSound.stop();
        drawCardSound.play();
    }
    
    public static void playClaimRouteSound() { claimRouteSound.play(); }
    
    public static void playTrivialSound() {
        trivialSound.play();
    }

    public static void playTickSound() {
        tickSound.play();
    }

    public static void playSendMessageSound() {
        sendMessageSound.stop();
        sendMessageSound.play();
    }

    public static void playBackgroundMusic() {
        backgroundMusic.playOnLoop();
    }

}
