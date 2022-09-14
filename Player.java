package battleship;

public class Player {
    private Field battleField;
    private String name;

    public Player(String name) {
        this.name = name;
        battleField = new Field();
        battleField.createBattleField();
    }

    public Field getField() {
        return battleField;
    }

    public String getName() {
        return name;
    }
}
