import minesweeper.core.*;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class FieldTest {
    private Random randomGenerator = new Random();
    private Field field;
    private int rowCount;
    private int columnCount;
    private int minesCount;

    public FieldTest() {
        rowCount = randomGenerator.nextInt(10) + 5;
        columnCount = rowCount;
        minesCount = Math.max(1, randomGenerator.nextInt(rowCount * columnCount));
        field = new Field(rowCount, columnCount, minesCount);
    }

    @Test
    public void checkFieldStructure(){
        int rowCounter = field.getTiles().length;
        int columnCounter = field.getTiles()[0].length;
        assertEquals(rowCount, rowCounter);
        assertEquals(columnCount, columnCounter);

        int minesCounter = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (field.getTiles(row, column) instanceof Mine) {
                    minesCounter++;
                }
            }
        }
        assertEquals(minesCount, minesCounter);
    }

    @Test
    public void checkInitialTilesStates(){
        int closedTilesCounter = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (Tile.State.CLOSED.equals(field.getTiles(row, column).getState())){
                    closedTilesCounter++;
                }
            }
        }
        assertEquals(rowCount*columnCount,closedTilesCounter);
    }
    @Test
    public void testOpenZeroTile(){
        int actRow = 0;
        int actColumn = 0;
        Tile tile = field.getTiles(actRow,actColumn);
        field.openTile(actRow,actColumn);
        assertEquals(GameState.PLAYING,field.getState());
        assertEquals(field.getState(), GameState.PLAYING);
        assertFalse(tile instanceof Mine);

        assertEquals(0, (((Clue)field.getTiles(actRow,actColumn)).getValue()));


    }
    @Test
    public void testOpenClueTile(){
        int actRow = 0;
        int actColumn = 0;
        Tile tile = field.getTiles(actRow,actColumn);
        assertEquals(GameState.PLAYING,field.getState());
        assertEquals(field.getState(), GameState.PLAYING);
        assertFalse(tile instanceof Mine);

        field.openTile(actRow,actColumn);
        int minesCounter = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (field.getTiles(actRow, actColumn) instanceof Mine) {
                    minesCounter++;
                }
            }
        }
        assertEquals(minesCounter, (((Clue)field.getTiles(actRow,actColumn)).getValue()));


    }

    @Test
    public void openMineTile(){
        int actRow = 0;
        int actColumn = 0;
        assertEquals(field.getTiles(actRow,actColumn),field.getTiles(actRow,actColumn) instanceof Mine);
    }



}
