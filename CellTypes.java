package battleship;

public enum CellTypes {
    FOG_OF_WAR('~'),
    SHIP('O'),
    MISSED('M'),
    DAMAGED('X');

    private char symbol;

    CellTypes(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
