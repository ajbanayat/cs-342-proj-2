import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Connect4 extends Application {
    int player;
    ListView<String> listView;
    GameButton[][] buttons;
    Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    public void changePlayer () {
        if (player == 1) {
            player = 2;
        } else {
            player = 1;
        }
        listView.getItems().add("Player " + player + " turn");
    }

    public void newGame() {
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                buttons[c][r].setText("");
                buttons[c][r].setDisable(false);
                buttons[c][r].setStyle("-fx-text-fill: white");
            }
        }
        player = 1;
        listView.getItems().add("New game started");
        listView.getItems().add("Player 1 turn");
    }

    public boolean hasValue(GameButton button) {
        return button.getText() == "X" || button.getText().equals("O");
    }

    private int hasEqualRowHelper1(int row, int col, int player) {

    }

    private int hasEqualRowHelper2(int row, int col, int player) {

    }

    public boolean hasEqualRow() {
        for (GameButton[] row : buttons) {
            if (hasValue(row[0]) && row[0].getText().equals(row[1].getText()) && row[0].getText().equals(row[2].getText())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEqualColumn() {
        for (int col = 0; col < 3; col++) {
            if (hasValue(buttons[0][col]) && buttons[0][col].getText().equals(buttons[1][col].getText()) && buttons[0][col].getText().equals(buttons[2][col].getText())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEqualDiagonal() {
        if ((hasValue(buttons[0][0]) && buttons[0][0].getText().equals(buttons[1][1].getText()) && buttons[0][0].getText().equals(buttons[2][2].getText())) ||
                (hasValue(buttons[0][2]) && buttons[0][2].getText().equals(buttons[1][1].getText()) && buttons[0][2].getText().equals(buttons[2][0].getText()))) {
            return true;
        }
        return false;
    }

    // can use recursion to check the row
    public boolean winner() {
//        return hasEqualRow() || hasEqualColumn() || hasEqualDiagonal();
        return false;
    }

    public void endGame() {
//        for (int r = 0; r < 3; r++) {
//            for (int c = 0; c < 3; c++) {
//                buttons[c][r].setDisable(true);
//            }
//        }
    }

    public boolean isBoardFull() {
        for (GameButton[] row: buttons) {
            for (GameButton cell : row) {
                if (!hasValue(cell)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidPostion(int row, int col) {
        return (row == 5 || (row < 5 && buttons[col][row+1].isDisable() == true));
//        return (row < 6 && (buttons[row+1][col].isDisabled() == false));
    }

    private int dropToValidPosition(int row, int col) {
        int curRow = row;
        while (!isValidPostion(curRow++, col));
        return curRow-1;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tic-Tac-Toe");

        int rows = 6;
        int cols = 7;

        listView = new ListView<>();
        buttons = new GameButton[7][6];

        GridPane gridPane = new GridPane();
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                buttons[c][r] = new GameButton();
                buttons[c][r].setMaxSize(100, 100);
                buttons[c][r].setMinSize(100, 100);
                gridPane.add(buttons[c][r], c + 1, r + 1);
            }
        }

        MenuItem startOver = new MenuItem("Start Over");
        startOver.setOnAction(actionEvent -> newGame());
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(actionEvent -> Platform.exit());

        Menu menu = new Menu("Menu");
        menu.getItems().add(startOver);
        menu.getItems().add(exit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameButton curButton = buttons[c][r];
                int row = r;
                int col = c;
                buttons[c][r].setOnAction(actionEvent -> {
                    int newRow = dropToValidPosition(row, col);
                    System.out.println("newRow: " + newRow);
                    GameButton temp = buttons[col][newRow];
                    if (player == 1) {
                        temp.setText("X");
                        temp.setStyle("-fx-text-fill: yellow");
                        temp.setStyle("-fx-background-color: yellow");
                        temp.setDisable(true);
                    } else {
                        temp.setText("O");
                        temp.setStyle("-fx-text-fill: red");
                        temp.setStyle("-fx-background-color: red");
                        temp.setDisable(true);
                    }
                    temp.setDisable(true);
                    if (winner()) {
                        endGame();
                        listView.getItems().add("Player " + player + " wins");
                    } else if (isBoardFull()) {
                        endGame();
                        listView.getItems().add("Tie");
                    } else {
                        changePlayer();
                    }
                });
            }
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(gridPane);
        borderPane.setRight(listView);
        VBox vbox = new VBox(menuBar, borderPane);

        newGame();

        buttons[0][0].setText("A");
        buttons[0][0].setStyle("-fx-text-fill: yellow");


        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            scene = new Scene(vbox, 1000,1000);
            primaryStage.setScene(scene);
        });

        scene = new Scene(startButton, 1000, 1000);
//        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}