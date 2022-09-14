package battleship;

import java.util.*;

public class Game {
    private static Game game;
    private static boolean finished = false;
    private static final Scanner scanner = new Scanner(System.in);
    private static final String ENTER_STRING = "Press Enter and pass the move to another player";
    private static final String SHIPS_TEXT = "Enter the coordinates of the %s (%d cells):%n";
    private static final Map<Character, Integer> MAP_OF_ROWS = new HashMap<>();

    static {
        MAP_OF_ROWS.put('A', 0);
        MAP_OF_ROWS.put('B', 1);
        MAP_OF_ROWS.put('C', 2);
        MAP_OF_ROWS.put('D', 3);
        MAP_OF_ROWS.put('E', 4);
        MAP_OF_ROWS.put('F', 5);
        MAP_OF_ROWS.put('G', 6);
        MAP_OF_ROWS.put('H', 7);
        MAP_OF_ROWS.put('I', 8);
        MAP_OF_ROWS.put('J', 9);
    }

    private Game() {
    }

    public static Game getInstance() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }


    public void printField(Player player) {
        player.getField().printPlayerField();
    }

    public void printEnemyField(Player player) {
        player.getField().printEnemyField();
    }

    public void arrangeShips(Player player) {
        String firstCoordinate;
        String secondCoordinate;

        System.out.printf("%s, place your ships on the game field%n", player.getName());
        printField(player);
        for (ShipTypes ship : ShipTypes.values()) {
            System.out.printf(SHIPS_TEXT, ship.getName(), ship.getCells());
            while (true) {
                firstCoordinate = scanner.next();
                secondCoordinate = scanner.next();
                scanner.nextLine();
                if (setShip(firstCoordinate, secondCoordinate, ship.getCells(), ship.getName(), player)) {
                    break;
                }
            }
            printField(player);
        }
        System.out.println(ENTER_STRING);
        scanner.nextLine();


    }

    public void shoot(Player player, Player enemy) {
        int x, y;
        String currentCoordinate;
        boolean isShoot = false;

        printEnemyField(enemy);
        System.out.println("---------------------");
        printField(player);
        System.out.printf("%s, it's your turn:%n", player.getName());
        while(!isShoot) {
            currentCoordinate = scanner.next();
            scanner.nextLine();
            if(!currentCoordinate.matches("([A-Ja-j]10)|([A-Ja-j][1-9]{1})")) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }
            x = Integer.parseInt(currentCoordinate.substring(1)) - 1;
            y = MAP_OF_ROWS.get(currentCoordinate.toUpperCase().charAt(0));
            switch (enemy.getField().getBattleField()[y][x]) {
                case 'O':
                    enemy.getField().getBattleField()[y][x] = CellTypes.DAMAGED.getSymbol();
                    if(hasWon(enemy)) return;
                    if(isSank(y, x, enemy)) {
                        System.out.println("You sank a ship!");
                    } else {
                        System.out.println("You hit a ship!");
                    }
                    isShoot = true;
                    break;
                case '~':
                    enemy.getField().getBattleField()[y][x] = CellTypes.MISSED.getSymbol();
                    System.out.println("You missed!");
                    isShoot = true;
                    break;
                case 'X':
                    System.out.println("You hit a ship!");
                    isShoot = true;
                    break;
                case 'M':
                    System.out.println("You missed!");
                    isShoot = true;
            }
        }
        System.out.println(ENTER_STRING);
        scanner.nextLine();
    }

    private boolean isSank(int i, int j, Player enemy) {
        for(int k = -1; k <= 1; k++) {
            for(int p = -1; p <= 1; p++) {
                if (i + k < 0 || j + p < 0 || i + k >= enemy.getField().getBattleField().length
                        || j + p >= enemy.getField().getBattleField()[i].length) {
                    continue;
                }
                if (enemy.getField().getBattleField()[i + k][j + p] == CellTypes.SHIP.getSymbol()) return false;
            }
        }

        return true;
    }

    private boolean hasWon(Player enemy) {
        for (int i = 0; i < enemy.getField().getBattleField().length; i++) {
            for (int j = 0; j < enemy.getField().getBattleField()[i].length; j++) {
                if(enemy.getField().getBattleField()[i][j] == CellTypes.SHIP.getSymbol()) return false;
            }
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
        finished = true;
        return true;
    }

    public boolean isFinished() {
        return finished;
    }

    private boolean setShip(String first, String second, int cells, String ship, Player player) {
        int firstY = MAP_OF_ROWS.get(first.toUpperCase().charAt(0));
        int firstX = Integer.parseInt(first.substring(1)) - 1;
        int secondY = MAP_OF_ROWS.get(second.toUpperCase().charAt(0));
        int secondX = Integer.parseInt(second.substring(1)) - 1;

        if ((firstY == secondY && Math.abs(secondX - firstX) + 1 != cells) || (secondX == firstX && Math.abs(secondY - firstY) + 1 != cells)) {
            System.out.printf("Error! Wrong length of the %s! Try again:%n", ship);
            return false;
        }
        if (firstX != secondX && firstY != secondY) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }
        char[][] newBattleField = player.getField().copyOfBattleField();
        if(firstX > secondX) {
            int temp;
            temp = firstX;
            firstX = secondX;
            secondX = temp;
        }

        if (firstY > secondY) {
            int temp;
            temp = firstY;
            firstY = secondY;
            secondY = temp;
        }

        for (int i = firstY; i <= secondY; i++) {
            for (int j = firstX; j <= secondX; j++) {
                if (newBattleField[i][j] == CellTypes.SHIP.getSymbol() || isProximate(i, j, player.getField().getBattleField())) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
                newBattleField[i][j] = CellTypes.SHIP.getSymbol();
            }
        }
        player.getField().setBattleField(newBattleField);
        return true;
    }

    private boolean isProximate(int i, int j, char[][] battleField) {
        for(int k = -1; k <= 1; k++) {
            for(int p = -1; p <= 1; p++) {
                if(i + k < 0 || j + p < 0 || i + k >= battleField.length || j + p >= battleField[i].length) {
                    continue;
                }
                if (battleField[i + k][j + p] == CellTypes.SHIP.getSymbol()) return true;
            }
        }

        return false;
    }

    public void closeInputStream() {
        scanner.close();
    }
}
