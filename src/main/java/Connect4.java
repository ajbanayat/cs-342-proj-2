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
    int rows;
    int cols;

    public static void main(String[] args) {
        launch(args);
    }

    public void changePlayer() {
        if (player == 1) {
            player = 2;
        } else {
            player = 1;
        }
        listView.getItems().add("Player " + player + " turn");
    }

    public void newGame() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                buttons[r][c].setText("");
                buttons[r][c].setDisable(false);
                buttons[r][c].setStyle("-fx-text-fill: white");
                buttons[r][c].setPlayer(0);
            }
        }
        player = 1;
        listView.getItems().add("New game started");
        listView.getItems().add("Player 1 turn");
    }

    public boolean hasValue(GameButton button) {
        return button.getText() == "X" || button.getText().equals("O");
    }

    private int checkLeft(int row, int col) {
        if (col < 0) {
            return 0;
        } else if (buttons[row][col].getPlayer() == player) {
            return 1 + checkLeft(row, col - 1);
        } else {
            return 0;
        }
    }

    private int checkRight(int row, int col) {
        if (col >= cols) {
            return 0;
        } else if (buttons[row][col].getPlayer() == player) {
            return 1 + checkRight(row, col + 1);
        } else {
            return 0;
        }
    }

    public boolean hasEqualRow(int row, int col) {
        return 1 + checkLeft(row, col - 1) + checkRight(row, col + 1) >= 4;
    }

    private int checkUp(int row, int col) {
        if (row < 0) {
            return 0;
        } else if (buttons[row][col].getPlayer() == player) {
            return 1 + checkUp(row - 1, col);
        } else {
            return 0;
        }
    }

    private int checkDown(int row, int col) {
        if (row >= rows) {
            return 0;
        } else if (buttons[row][col].getPlayer() == player) {
            return 1 + checkDown(row + 1, col);
        } else {
            return 0;
        }
    }

    public boolean hasEqualColumn(int row, int col) {
        return 1 + checkUp(row - 1, col) + checkDown(row + 1, col) >= 4;
    }

    public int WonDiagUR(int row, int col) {
        if ((row < 0 || row > rows) || (col < 0 || col > cols)) return 0;
        if ((row-1 >= 0 && col+1 < cols) && (buttons[row][col].getPlayer() == buttons[row-1][col+1].getPlayer())) {
            return 1 + WonDiagUR(row-1, col+1);
        }
        return 0;
    }

    public int WonDiagDL(int row, int col) {
        if ((row < 0 || row > rows) || (col < 0 || col > cols)) return 0;
        if ((row+1 < rows && col-1 >= 0) && (buttons[row][col].getPlayer() == buttons[row+1][col-1].getPlayer())) {
            return 1 + WonDiagDL(row+1, col-1);
        }
        return 0;
    }

    public int WonDiagUL(int row, int col) {
        if ((row < 0 || row > rows) || (col < 0 || col > cols)) return 0;
        if ((row-1 >= 0 && col-1 >= 0) && (buttons[row][col].getPlayer() == buttons[row-1][col-1].getPlayer())) {
            return 1 + WonDiagUR(row-1, col-1);
        }
        return 0;
    }

    public int WonDiagDR(int row, int col) {
        if ((row < 0 || row > rows) || (col < 0 || col > cols)) return 0;
        if ((row+1 < rows && col+1 < cols) && (buttons[row][col].getPlayer() == buttons[row+1][col+1].getPlayer())) {
            return 1 + WonDiagDR(row+1, col+1);
        }
        return 0;
    }

//    public int WonDiagDR(int row, int col) {
//        if (row < 0 || row > rows || col < 0 || col > cols) return 0;
//        if (row + 1 < rows && col + 1 < cols && buttons[row][col].getPlayer() == buttons[row+1][col+1].getPlayer()) {
//            return 1 + Won
//        }
//    }

    public boolean WonDiag(int row, int col) {
        int x = 1 + WonDiagUR(row, col) + WonDiagDL(row, col);
        int y = 1 + WonDiagUL(row, col) + WonDiagDR(row, col);
        return  (x >= 4 || y >= 4);
    }

    public boolean winner(int row, int col) {
        return hasEqualRow(row, col) || hasEqualColumn(row, col) || WonDiag(row, col);
    }

    public void endGame() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                buttons[r][c].setDisable(true);
            }
        }
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
        return (row == 5 || (row < 5 && buttons[row+1][col].isDisable() == true));
//        return (row < 6 && (buttons[row+1][col].isDisabled() == false));
    }

    private int dropToValidPosition(int row, int col) {
        int curRow = row;
        while (!isValidPostion(curRow++, col));
        buttons[curRow-1][col].setPlayer(player);
        return curRow-1;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tic-Tac-Toe");

        rows = 6;
        cols = 7;

        listView = new ListView<>();
        buttons = new GameButton[rows][cols];

        GridPane gridPane = new GridPane();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                buttons[r][c] = new GameButton();
                buttons[r][c].setMaxSize(100, 100);
                buttons[r][c].setMinSize(100, 100);
                gridPane.add(buttons[r][c], c + 1, r + 1);
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
                int row = r;
                int col = c;
                buttons[r][c].setOnAction(actionEvent -> {
                    int newRow = dropToValidPosition(row, col);
                    GameButton temp = buttons[newRow][col];
                    if (player == 1) {
                        temp.setStyle("-fx-text-fill: yellow");
                        temp.setStyle("-fx-background-color: yellow");
                        temp.setDisable(true);
                    } else {
                        temp.setStyle("-fx-text-fill: red");
                        temp.setStyle("-fx-background-color: red");
                        temp.setDisable(true);
                    }
//                    System.out.println("clicked row " + newRow + " col " + col);
//                    System.out.println("for player " + player + ": row -> " + hasEqualRow(newRow, col));
//                    System.out.println("for player " + player + ": col -> " + hasEqualColumn(newRow, col));
                    System.out.println("WonDiagUR: " + WonDiagUR(newRow, col));
                    System.out.println("WonDiagUL: " + WonDiagUL(newRow, col));
                    System.out.println("WonDiagDL: " + WonDiagDL(newRow, col));
                    System.out.println("WonDiagDR: " + WonDiagDR(newRow, col));
                    System.out.println("Total in DL/UR diagonal: " + (1 + WonDiagUR(newRow, col) + WonDiagDL(newRow, col)));
                    System.out.println("Total in UL/DR diagonal: " + (1 + WonDiagDR(newRow, col) + WonDiagUL(newRow, col)));
                    System.out.println("winner: " + winner(newRow, col));
                    temp.setDisable(true);
                    if (winner(newRow, col)) {
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
            scene = new Scene(vbox, 900,650);
            scene.getRoot().setStyle("-fx-font-family: 'arial'");
            primaryStage.setScene(scene);
        });

        scene = new Scene(startButton, 500, 500);
//        scene.getStylesheets().add("stylesheet.css");
        scene.getRoot().setStyle("-fx-font-family: 'arial'");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
