import java.util.*;
import java.io.*;

public class FlashCards {

    private static Random rng = new Random();
    private static Scanner scnr = new Scanner(System.in);
    private static int score = 0;

    public static void main(String args[]) throws IOException {
        String path;
        String choice;
        int game = 1;
        boolean wantGame = false;
        path = args[0];
        HashMap<String, String> cards = getCards(path);
        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> defs = new ArrayList(cards.values());
        List<String> choices;
        words.addAll(cards.keySet());

        do {
            try {
                System.out.print("\nDo you wish to start a game? (y/n)");
                choice = scnr.next().toLowerCase();
                switch(choice) {
                    case "y":
                        wantGame = true;
                        break;
                    case "n":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option.");
                        break;
                }
            } catch(Exception e) {
                scnr.nextLine();
            }
        } while (!wantGame);



        while(game < 6) {
            String word;
            if (words.size() > 1) {
                int i = rng.nextInt(words.size() - 1);
                word = words.remove(i);
                choices = getChoices(cards.get(word), defs);
                userChoice(choices, cards.get(word), word);
                game++;
            }
            else {
                word = words.remove(0);
                System.out.println("The definition of " + word + ":");
                game++;
            }
        }

        System.out.println("Your score was: " + score + "/5. Keep practicing!");

    }


    /**
     * method handles user input for questions
     */
    private static void userChoice(List<String> defs, String answer, String word) {
        boolean exception;
        boolean invalid = false;
        do {
            try {
                System.out.println("The definition of " + word + ":");
                printChoices(defs);
                System.out.print("Answer: ");
                String choice = scnr.next().toLowerCase();
                int i = 0;
                switch (choice) {
                    case "a":
                        i = 0;
                        break;
                    case "b":
                        i = 1;
                        break;
                    case "c":
                        i = 2;
                        break;
                    case "d":
                        i = 3;
                        break;
                    default:
                        System.out.println("\nInvalid input.");
                        invalid = true;
                        break;
                }

                if (invalid) {
                    System.out.println("I assume you just don't know... The correct answer is: \n---> " + answer + "\n");
                }
                else if (defs.get(i).equals(answer)) {
                    System.out.println("Correct!\n");
                    score++;
                }
                else {
                    System.out.println("\nWRONG! The correct answer is: \n---> " + answer + "\n");
                }
                exception = false;
            } catch(Exception e) {
                scnr.nextLine();
                exception = true;
            }
        } while(exception);


    }

    /**
     * Separates words from definitions, adds them to hashmap
     */
    private static HashMap<String, String> getCards(String path) throws IOException{
        File file = new File(path);
        HashMap<String, String> cards = new HashMap<>();
        Scanner reader = new Scanner(file);
        String word = ""; //variable for current word
        String definition = ""; //variable for current word's definition
        char current;
        boolean isDefinition = false;

        while (reader.hasNext()) { //will read each individual char
            reader.useDelimiter("");
            current = reader.next().charAt(0);

            if (current == ':') { //key char for separating words and definitions
                isDefinition= true;
            }
            else if (current == '\n') { //newlines for new word and definition pairs, will reset variables
                if (definition.length() > 0) {
                    isDefinition = false;
                    definition = fixString(definition); //method call
                    cards.put(word, definition);
                    word = "";
                    definition = "";
                }
            }
            else if (!reader.hasNext()) {
                definition += current;
                definition = fixString(definition); //method call
                cards.put(word, definition);
            }
            else if (isDefinition) {
                definition += current;
            }
            else if (current != '\r'){
                word += current;
            }
        }
        reader.close();

        return cards;
    }

    /**
     * gets rid of spaces in beginnings of definitions and \r at ends
     */
    private static String fixString(String input) {
        String temp = "";
        if (input.charAt(0) == ' ') {
            if (input.charAt(input.length() - 1) == '\r') {
                temp = input.substring(1, (input.length() - 1));
            }
            else {
                temp = input.substring(1);
            }
        }
        return temp;
    }

    private static List<String> getChoices(String answer, List<String> defs) {
        List<String> choices = new ArrayList<>();
        int i = rng.nextInt(3);

        for (int j = 0; j < 4; j++) {
            int k = rng.nextInt(defs.size() - 1);
            if (j == i) {
                choices.add(answer);
            }
            else if (!defs.get(k).equals(answer)) {
                choices.add(defs.get(k));
            }
        }

        return choices;
    }

    /**
    * prints the choices a-d
    */
    private static void printChoices(List<String> choices) {
        int letter = 97;

        for (String choice : choices) {
            System.out.println(Character.toString((char) letter) + "). " + choice);
            letter++;
        }
        System.out.println();
    }


}
