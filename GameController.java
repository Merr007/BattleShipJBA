package battleship;

import java.util.Scanner;

public class GameController {

    public static void runGame(){
        Game game = Game.getInstance();
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        game.arrangeShips(player1);
        game.arrangeShips(player2);
        while(!game.isFinished()) {
            game.shoot(player1, player2);
            game.shoot(player2, player1);
        }
        game.closeInputStream();
    }
}
