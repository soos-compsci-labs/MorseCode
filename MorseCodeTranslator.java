import java.util.HashMap;

public class MorseCodeTranslator {
    private static HashMap<String, String> encode = new HashMap<>();
    private static HashMap<String, String> decode = new HashMap<>();

    public MorseCodeTranslator(String[] english, String[] code) {
        // add the english and morse code to the hashmaps
        encode = arraysToMap(english, code);
        decode = arraysToMap(code, english);
    }

    public String encode(String line) {
        /*
        Using a string builder accompanied by a ternary operator
        to distinguish if the character is a space or not, and encoding
        each character with it's corresponding morse code, and if it doesn't exist, it's ignored.
         */
        StringBuilder sb = new StringBuilder();
        String[] chars = line.split("");
        for (String c : chars) sb.append((c.equals(" ")) ? "/ " : (encode.containsKey(c.toUpperCase())) ? encode.get(c.toUpperCase()) + " " : c);
        return sb.toString().toString();
    }

    public String decode(String line) {
        /*
        Using a string builder accompanied by a ternary operator
        to distinguish if the character is a slash or not, and decoding
        each character with its corresponding english character
         */
        StringBuilder sb = new StringBuilder();
        String[] chars = line.split(" ");
        for (String c : chars) sb.append((c.equals("/")) ? " " : decode.get(c));
        return sb.toString();
    }

    // helper method to convert two arrays to a hashmap
    public static HashMap<String, String> arraysToMap(String[] arrayOne, String[] arrayTwo) {
        HashMap<String, String> map = new HashMap<>();
        // Setting the first array as the key and the second array as the value of the map
        for (int i = 0; i < arrayOne.length; i++) map.put(arrayOne[i], arrayTwo[i]);
        return map;
    }
}
