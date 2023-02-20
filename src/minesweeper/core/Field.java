package minesweeper.core;

import java.util.Random;

/**
 * Field represents playing field and game logic.
 */
public class Field {
    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        //generate the field content
        generate();
    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Clue && (((Clue)tile).getValue() == 0)) {
                openAdjacentTiles(row, column);

            }

            if (tile instanceof Mine) {
                state = GameState.FAILED;
                return;
            }

            if (isSolved()) {
                state = GameState.SOLVED;
                return;
            }
        }
    }

    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.MARKED) {
            tile.setState(Tile.State.CLOSED);
        }
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.MARKED);
        }

    }


    /**
     * Generates playing field.
     */
    private void generate() {
        Random random = new Random();

        int i = 0, r, c;
        /*while (i < mineCount) {
            if (tiles[getRowCount()][getColumnCount()] == null) {
                tiles[random.nextInt(getRowCount())][random.nextInt(getColumnCount())] = new Mine();
                i++;
            }*/
        while (i < mineCount) {
            r = random.nextInt(rowCount);
            c = random.nextInt(columnCount);
            if (tiles[r][c] == null) {
                tiles[r][c] = new Mine();
                i++;
            }
        }

        for (int j = 0; j < getRowCount(); j++) {
            for (int p = 0; p < getColumnCount(); p++) {
                if (tiles[j][p] == null) {
                    tiles[j][p] = new Clue(countAdjacentMines(j,p));
                }
            }
        }

    }


    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    public int getNumberOf(Tile.State state) {
        int count = 0;
        for (int r = 0; r < rowCount; r++) {
            for (Tile t : tiles[r]) {
                if (t.getState() == state)
                    count++;
            }
        }
        return count;
    }

    private boolean isSolved() {
        return (rowCount * columnCount) - mineCount == getNumberOf(Tile.State.OPEN);
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    void openAdjacentTiles(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        openTile(actRow, actColumn);
                    }
                }
            }
        }
    }

    public void openAdjacentTiles1(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(tile instanceof Clue ? Tile.State.OPEN : Tile.State.CLOSED);
            if (isSolved()) {
                state = GameState.SOLVED;
                return;
            }
            if (tile instanceof Clue && (((Clue) tile).getValue() == 0)) {
                for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                    int actRow = row + rowOffset;
                    if (actRow >= 0 && actRow < rowCount) {
                        for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                            int actColumn = column + columnOffset;
                            if (actColumn >= 0 && actColumn < columnCount) {
                                if(!(rowOffset == 0 && columnOffset == 0)) {
                                    Tile nextTile = tiles[actRow][actColumn];
                                    if (nextTile instanceof Clue && Tile.State.CLOSED.equals(nextTile.getState())) {
                                        if(((Clue) nextTile).getValue() > 0){
                                            nextTile.setState(Tile.State.OPEN);
                                        } else {
                                            openAdjacentTiles1(actRow, actColumn);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void failGame(){
        state = GameState.FAILED;
    }

    public Tile getTiles(int row, int column) {
        return tiles[row][column];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getState() {
        return state;
    }
}
