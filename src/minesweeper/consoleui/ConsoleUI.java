package minesweeper.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import minesweeper.core.Field;
import minesweeper.core.GameState;
import minesweeper.core.Mine;
import minesweeper.core.Tile;

/**
 * Console user interface.
 */
public class ConsoleUI implements UserInterface {
    /**
     * Playing field.
     */
    private Field field;

    /**
     * Input reader.
     */
    Pattern OPEN_MARK_PATTERN = Pattern.compile("([OomM]{1})([A-Za-z]{1})([0-9]{1,2})");

    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Reads line of text from the reader.
     *
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Starts the game.
     *
     * @param field field of mines and clues
     */
    @Override
    public void Play(Field field) {
        this.field = field;
        do {
            update();
            processInput();
            if (field.getState() == GameState.SOLVED) {
                System.out.print("Vyhral si omg! Nice!");

            } else if (field.getState() == GameState.FAILED) {
                System.out.print("=========BOOOOOOOM==========\n");
                System.out.print("==========You died============");
                System.exit(0);

            }
        } while (field.getState() == GameState.PLAYING);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {
        Formatter f = new Formatter();
        f.format("Počet označených políčok ako mína je %d\n", field.getNumberOf(Tile.State.MARKED))
                .format("Počet mín je %d\n", field.getMineCount());
        f.format("   ");
        for (int i = 0; i < field.getColumnCount(); i++) {
            f.format("%3s", i);

        }
        f.format("\n");

        for (int c = 0; c < field.getRowCount(); c++) {
            f.format("%3s", Character.toString(c + 65));

            for (int j = 0; j < field.getColumnCount(); j++) {
                f.format("%3s", field.getTiles(c, j));

            }

            f.format("%n");
        }
        System.out.println(f);
    }


    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
    private void processInput() {
        System.out.println("Zadaj vstup " + System.getProperty("user.name"));
        System.out.println("Nápoveda: X - ukončiť hru, M - mark, O - open. Napr.: OA1 - otvorenie dlazdice v riadku A a stlpci 1");
        String playerInput = readLine();
        if (playerInput.toUpperCase().equals("X")) {
            System.out.println("Goodbye.");
            System.exit(0);
        }

//        try {
//            handleInput(playerInput);
//        } catch (WrongFormatExceptions e) {
//            System.out.println(e.getMessage());
//            System.out.println("Skús to znova");
//        }
        try{
            handleInput(playerInput);
        }catch (IndexOutOfBoundsException | WrongFormatExceptions e){
            System.out.println(e.getMessage());
            System.out.println("skús to znova");

        }

    }


    private void doStuff(char character, char rowChar, int column) {
        int rowInt = rowChar - 65;
        //ked je Mark
        if (character == 'M') {
            field.markTile(rowInt, column);

        }
        //ked je open
        if (character == 'O') {
            if (field.getTiles(rowInt, column).getState() == Tile.State.MARKED) {
                System.out.println("Nemôžeš otvoriť tile, ktorý je v stave Marked");
            } else if (field.getTiles(rowInt, column).getState() == Tile.State.OPEN) {
                System.out.println("Nemôžeš otvoriť tile, ktorý je v stave Open");
            } else {
                if (field.getTiles(rowInt, column) instanceof Mine) {
                    field.failGame();
                } else {
                    field.openAdjacentTiles1(rowInt, column);
                }
            }

        }


    }

    void handleInput(String input) throws WrongFormatExceptions {
        Matcher matcher = OPEN_MARK_PATTERN.matcher(input.toUpperCase());

        if (!OPEN_MARK_PATTERN.matcher(input).matches()) {
            throw new WrongFormatExceptions("nespravný vstup");
        }
        if (matcher.matches()) {
            doStuff(matcher.group(1).charAt(0), matcher.group(2).charAt(0), Integer.parseInt(matcher.group(3)));
        }

    }
}
