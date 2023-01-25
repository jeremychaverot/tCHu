package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * This interface represents a serde, which is an object capable of (de)serializing values of a given type.
 * @param <E> : type of the elements the serde is able to (de)serialize.
 * @author Jérémy Chaverot (315858)
 */
public interface Serde<E> {

    /**
     * To serialize an object and obtain the corresponding string.
     * @param object <E> : object which is to be serialized.
     * @return string (String) : the string of the serialized object.
     */
    public abstract String serialize(E object);

    /**
     * To deserialize a string and obtain the corresponding object.
     * @param string (String : string which is to be deserialized.
     * @return object <E> : the object of the deserialized string.
     */
    public abstract E deserialize(String string);

    /**
     * To get the corresponding serde from given serializing and deserializing functions.
     * @param serializer (Function<T, String>) : the serializing function.
     * @param deserializer (Function<T, String>) : the deserializing function.
     * @param <T> : type of the elements that are serialized.
     * @return serde (Serde<T>) : the serde resulting from the two functions.
     */
    public static <T> Serde<T> of(Function<T, String> serializer, Function<String, T> deserializer) {
        return new Serde<T>() {

            /**
             * To serialize an object and obtain the corresponding string.
             * @param object <T> : object which is to be serialized.
             * @return string (String) : the string of the serialized object.
             */
            @Override
            public String serialize(T object) { return serializer.apply(object); }

            /**
             * To deserialize a string and obtain the corresponding object.
             * @param string (String) : string which is to be deserialized.
             * @return object <T> : the object of the deserialized string.
             */
            @Override
            public T deserialize(String string) { return deserializer.apply(string); }

        };
    }

    /**
     * To get corresponding serde of a list of all values of a set of enumerate values.
     * @param list (List<T>) : list of all values of a set of enumerate values.
     * @param <T> : type of the enumerate values.
     * @return serde (Serde<T>) : the corresponding serde of the list.
     * @throws IllegalArgumentException if the list is null.
     */
    public static <T> Serde<T> oneOf(List<T> list) {
        List<T> copyList = new ArrayList<>(list);
        Preconditions.checkArgument(copyList != null);
        Preconditions.checkArgument(!copyList.isEmpty());

        Function<T, String> serializer = t -> Integer.toString(copyList.indexOf(t));
        Function<String, T> deserializer = s -> copyList.get(Objects.checkIndex(Integer.parseInt(s), copyList.size()));

        return of(serializer, deserializer);
    }

    /**
     * To get a serde capable of (de)serializing lists of values (de)serialized by the given serde.
     * @param serde (Serde<T>) : serde capable of (de)serializing the elements of type <T>.
     * @param character (String) : separation character.
     * @param <T> : type of the elements in the list.
     * @return serde (Serde<List<T>>) : serde capable of (de)serializing list of values of type <T>.
     */
    public static <T> Serde<List<T>> listOf(Serde<T> serde, String character) {
        return new Serde<List<T>>() {

            /**
             * To serialize a list and obtain the corresponding string.
             * @param list (List<T>) : list of elements of type T which is to be serialized.
             * @return string (String) : the string of the serialized list.
             * @throws NullPointerException if the argument is null (should never happen).
             */
            @Override
            public String serialize(List<T> list) {
                String serialized = "";
                if (!list.isEmpty()) {
                    List<String> listOfSerializedElements = new ArrayList<>();
                    list.forEach(t -> listOfSerializedElements.add(serde.serialize(t)));
                    serialized = String.join(character, listOfSerializedElements);
                }

                return serialized;
            }

            /**
             * To deserialize a string and obtain the corresponding list.
             * @param string (String) : string which is to be deserialized.
             * @return list (List<T>) : the list of elements of type T of the deserialized string.
             * @throws NullPointerException if the argument is null (should never happen).
             */
            @Override
            public List<T> deserialize(String string) {
                String[] splitString = string.split(Pattern.quote(character), -1);
                List<T> list = new ArrayList<>();
                if (!string.isEmpty())
                    for (String s : splitString) list.add(serde.deserialize(s));

                return list;
            }

        };
    }

    /**
     * To get a serde capable of (de)serializing sorted bag of values (de)serialized by the given serde.
     * @param serde (Serde<T>) : serde capable of (de)serializing the elements of type <T>.
     * @param character (String) : separation character.
     * @param <T> : type of the elements in the sorted bag.
     * @return serde (Serde<SortedBag<T>>) : serde capable of (de)serializing sorted bag of values of type <T>.
     */
    public static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, String character) {
        return new Serde<SortedBag<T>>() {

            /**
             * To serialize a sorted bag and obtain the corresponding string.
             * @param sortedBag (SortedBag<T>) : sorted bag of elements of type T which is to be serialized.
             * @return string (String) : the string of the serialized sorted bag.
             * @throws NullPointerException if the argument is null (should never happen).
             */
            @Override
            public String serialize(SortedBag<T> sortedBag) {
                return listOf(serde, character).serialize(sortedBag.toList());
            }

            /**
             * To deserialize a string and obtain the corresponding sorted bag.
             * @param string (String) : string which is to be deserialized.
             * @return sortedBag (SortedBag<T>) : the sorted bag of elements of type T of the deserialized string.
             * @throws NullPointerException if the argument is null (should never happen).
             */
            @Override
            public SortedBag<T> deserialize(String string) {
                return SortedBag.of(listOf(serde, character).deserialize(string));
            }

        };
    }

}
