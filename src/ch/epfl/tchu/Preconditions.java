package ch.epfl.tchu;

/**
 * @author Jérémy Chaverot (315858)
 */
public final class Preconditions {

    /**
     * Check if the condition is satisfied as expected.
     * @param shouldBeTrue (boolean) : the condition verified.
     * @throws IllegalArgumentException if the condition is not satisfied, i.e. if false.
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue)
            throw new IllegalArgumentException();
    }

    /**
     * Empty private constructor because irrelevant.
     */
    private Preconditions() {}

}
