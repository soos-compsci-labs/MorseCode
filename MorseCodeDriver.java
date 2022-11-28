import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.*;


public class MorseCodeDriver {
    static boolean sound = false;
    private static final int DOT = 100, DASH = DOT * 4, FREQ = 800;

    // Method to read a file and return an arraylist of the lines
    static ArrayList<String> readFile(String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        Scanner file = new Scanner(new File(fileName));
        // adding the lines to the arraylist
        while (file.hasNextLine()) lines.add(file.nextLine());
        file.close();
        return lines;
    }


    // Method to just display the menu
    static void showMenu() {
        System.out.println("\n1. Encode a file");
        System.out.println("2. Encode raw input");
        System.out.println("3. Decode a file");
        System.out.println("4. Decode raw input");
        System.out.println("5. Toggle sound (currently " + (sound ? "on" : "off") + ")");
        System.out.println("6. Quit");
        System.out.print("\n>>> ");
    }

    public static void main(String[] arg) throws IOException, InterruptedException, LineUnavailableException {
        System.out.println("-- --- .-. ... . / -.-. --- -.. . / - .-. .- -. ... .-.. .- - --- .-."); // Title of application in morse code
        MorseCodeTranslator translator = new MorseCodeTranslator(MorseCode.english, MorseCode.code);
        while (true) {
            showMenu();
            Scanner input = new Scanner(System.in); // Scanner for user input and file input
            String userInput = input.nextLine();
            // check if it's a valid int
            if (!userInput.matches("\\d")) { // if it's not a number
                System.out.println("Invalid input");
                continue;
            }

            int choice = Integer.parseInt(userInput); // convert to int
            ArrayList<String> lines = new ArrayList<>();
            switch (choice) { // switch statement for the menu, acts like many if statements
                // cases correspond to the menu options
                case 1 -> {
                    lines = readFile(getFileName(input, "Enter the name of the file to encode: ")); // set lines to the arraylist of the file
                    System.out.println();
                }
                case 2 -> {
                    System.out.print("Enter the text to encode: ");
                    lines.add(getInput(input)); // set lines to the input, which is just one line
                }
                case 3 -> {
                    lines = readFile(getFileName(input, "Enter the name of the file to decode: ")); // set lines to the arraylist of the file
                    System.out.println();
                }
                case 4 -> {
                    System.out.print("Enter the text to decode: ");
                    lines.add(getInput(input)); // set lines to the input, which is just one line
                }
                case 5 -> {
                    // toggle sound
                    sound = !sound;
                    System.out.println("Sound is now " + (sound ? "on" : "off")); // toggle sound, ternary operator
                    continue; // continue to the next iteration of the loop
                }
                case 6 -> {
                    System.out.println("Goodbye!");
                    System.exit(0); // exit the program
                }
                default -> {
                    // if the user's input is not a valid option
                    System.out.println("Invalid choice");
                    continue; // continue to the next iteration of the loop
                }
            }
            if(sound && (choice == 1 || choice == 2)) { // if sound is on and the choice is to encode
                for(String line : lines) { // for each line in the arraylist
                    line = translator.encode(line); // encode the line
                    for(char c : line.toCharArray()) { // for each character in the line
                        // if c is a space, sleep for DOT / 5
                        if(c == ' ' || c == '/') Thread.sleep(500); // if it's a space or a slash, sleep for 500ms
                        System.out.print(c);
                        if(c == ' ' || c == '/') {
                            Thread.sleep(500); // if it's a space or a slash, sleep for 500ms again ( and continue out of the for loop as they don't have sound )
                            continue;
                        }
                        // playing the sound
                        try (SourceDataLine sdl = AudioSystem.getSourceDataLine(new AudioFormat(8000F, 8, 1, true, false))) { // creating the sound
                            sdl.open(sdl.getFormat()); // open the line
                            sdl.start(); // start the sound
                            for (int i = 0; i < (c == '.' ? DOT : DASH) * 8; i++) {
                                sdl.write(new byte[]{(byte) ((1/Math.sin(i / (8000F / FREQ) * 2.0 * Math.PI)) * 127.0)}, 0, 1);
                                // create a cosecant wave sound with the frequency of FREQ, and the length of DOT or DASH, depending on the character
                            }
                            sdl.drain(); // wait for the sound to finish
                        }
                        Thread.sleep(DOT / 5); // sleep for DOT / 5
                    }
                    System.out.println();
                }
            } else { // if sound is off or the choice is to decode
                for (String line : lines) {
                    if (choice == 1 || choice == 2) System.out.println(translator.encode(line)); // if the choice is to encode, encode the line
                    else System.out.println(translator.decode(line)); // if the choice is to decode, decode the line
                }
            }

        }
    }

    // Method to get the file name from the user, and print the prompt
    public static String getFileName(Scanner input, String message) {
        System.out.print(message);
        String fileName = input.next();
        // check if the file is valid
        while (!new File(fileName).exists()) {
            System.out.print("File not found. " + message);
            fileName = input.next();
        }
        return fileName;
    }

    // Method to get the input from the user
    public static String getInput(Scanner input) {
        String text = input.nextLine();
        System.out.println();
        return text;
    }
}