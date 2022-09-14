package battleship;

import java.util.Arrays;

public class Field {
    private char[][] battleField = new char[10][10];

    public void createBattleField() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                battleField[i][j] = CellTypes.FOG_OF_WAR.getSymbol();
            }
        }
    }

    public void setBattleField(char[][] battleField) {
        this.battleField = battleField;
    }

    public char[][] getBattleField() {
        return battleField;
    }

    public void printPlayerField() {
        char currentWordInRow = 'A';
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                if (i == 0) {
                    if (j == 0) System.out.print("  ");
                    else System.out.print(j + " ");
                } else if (j == 0) {
                    System.out.print(currentWordInRow + " ");
                    currentWordInRow++;
                } else {
                    System.out.print(battleField[i - 1][j - 1] + " ");
                }
            }
            if (i == 5) {
                System.out.print("<--- Your field");
            }
            System.out.println();
        }
    }

    public void printEnemyField() {
        char currentWordInRow = 'A';
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                if (i == 0) {
                    if (j == 0) System.out.print("  ");
                    else System.out.print(j + " ");
                } else if (j == 0) {
                    System.out.print(currentWordInRow + " ");
                    currentWordInRow++;
                } else {
                    if (battleField[i - 1][j - 1] == CellTypes.DAMAGED.getSymbol() || battleField[i - 1][j - 1] == CellTypes.MISSED.getSymbol()) System.out.print(battleField[i - 1][j - 1] + " ");
                    else {
                        System.out.print(CellTypes.FOG_OF_WAR.getSymbol() + " ");
                    }
                }
            }
            if (i == 5) {
                System.out.print("<--- Enemy field");
            }
            System.out.println();
        }
    }

    public char[][] copyOfBattleField() {
        return Arrays.stream(battleField).map(char[]::clone).toArray(char[][]::new);
    }



}
