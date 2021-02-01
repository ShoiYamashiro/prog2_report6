package jp.ac.uryukyu.ie.e205752;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * オセロの処理をするクラス 
 * オセロ盤の一辺(8, 10, 12, 14, 16)
 * 各マスの状態(B:黒, W:白, N:石なし)
 * プレイヤーの石の色
 * 相手の石の色
 * ターンを数える
 */
class OthelloBoard {
    private int size;
    private char[][] squares;
    private char playerColor;
    private char otherColor;
    private int turnCounter;
    private final String alphabets = "abcdefghijklmnop";

    // コンストラクタ
    public OthelloBoard() {
        this.size = 8;
        // this.size = askBoardSize();
        this.squares = new char[this.size][this.size];
    }

    // オセロを開始する
    public void start() {
        // プレイヤーの石を決める
        this.askPlayerColor();
        // オセロ盤を開始直後の状態にする
        this.initializeBoard();
        this.printBoard();
        this.turnCounter = 1;
        int turnCounterMax = this.size*this.size - 4;
        int skipCounter = 0;
        // 先手がどちらかを決める
        boolean isPlayerTurn = true;
        if (this.playerColor == 'W') {
            isPlayerTurn = false;
        }

        // 各ターンの処理
        System.out.println("オセロを始めます。");
        int playerDiscNum;
        int otherDiscNum;
        while (this.turnCounter <= turnCounterMax) {
            // 現時点での石の数を表示する
            playerDiscNum = this.countDisc(this.playerColor);
            otherDiscNum = this.countDisc(this.otherColor);
            System.out.print("あなた = " + playerDiscNum + "  ");
            System.out.println("相手 = " + otherDiscNum);
            if (isPlayerTurn) {
                // プレイヤーのターン
                // プレイヤーが石をおけるかどうか判定する
                if (! this.checkSquaresForNewDisc(this.playerColor, this.otherColor)) {
                    // プレイヤーのターンはスキップされる
                    System.out.println("あなたのターンはスキップされました。");
                   if (skipCounter == 1) {
                        // すでに相手のターンもスキップされていた場合、ゲーム終了
                        break;
                    }
                    isPlayerTurn = !isPlayerTurn;
                    skipCounter ++;
                    continue;
                }
                System.out.println("Turn " + turnCounter + ":あなたのターンです。");
                skipCounter = 0;
                this.askNewCoordinates(this.playerColor, this.otherColor);
            } else {
                // 相手のターン
                // 相手が石をおけるかどうか判定する
                if (! this.checkSquaresForNewDisc(this.otherColor, this.playerColor)) {
                    // プレイヤーのターンはスキップされる
                    System.out.println("相手のターンはスキップされました。");
                    if (skipCounter == 1) {
                        // すでにプレイヤーのターンもスキップされていた場合、ゲーム終了
                        break;
                    }
                    isPlayerTurn = !isPlayerTurn;
                    skipCounter ++;
                    continue;
                }
                // 相手のターン
                System.out.println("Turn " + turnCounter + ":相手のターンです。");
                skipCounter = 0;
                this.askNewCoordinates(this.otherColor, this.playerColor);
            }
            this.printBoard();
            // 次ターンに向けての処理
            this.turnCounter ++;
            isPlayerTurn = !isPlayerTurn;
        }

        // 勝敗の判定
        playerDiscNum = this.countDisc(this.playerColor);
        otherDiscNum = this.countDisc(this.otherColor);
        System.out.print("あなた = " + playerDiscNum + "  ");
        System.out.println("相手 = " + otherDiscNum);
        if (playerDiscNum > otherDiscNum) {
            System.out.println("あなたの勝ちです。");
        } else if (playerDiscNum == otherDiscNum) {
            System.out.println("引き分けです。");
        } else {
            System.out.println("あなたの負けです。");
        }
    }

    // オセロ盤をコンソール上に表示する
    private void printBoard() {
        this.printBoardAlphabetLine(); // アルファベット行
        this.printBoardOtherLine("┏", "┳", "┓"); // 上端
        for (int y = 0; y < this.size - 1; y++) {
            this.printBoardDiscLine(y); // 石を表示する行
            this.printBoardOtherLine("┣", "╋", "┫"); // 行間の枠
        }
        this.printBoardDiscLine(this.size - 1); // 石を表示する行
        this.printBoardOtherLine("┗", "┻", "┛"); // 下端
    }

    // オセロ盤の列を示すアルファベットを表示する
    private void printBoardAlphabetLine() {
        String buf = "  ";
        for (int x = 0; x < this.size; x++) {
            buf += "   " + this.alphabets.charAt(x);
        }
        System.out.println(buf);
    }

    // オセロ盤の石がある行を1行分表示する
    private void printBoardDiscLine(int y) {
        String buf = String.format("%2d┃", y + 1);
        for (int x = 0; x < this.size; x++) {
            if (this.squares[y][x] == 'B') {
                buf += "●┃";
            } else if (this.squares[y][x] == 'W') {
                buf += "○┃";
            } else {
                buf += "　┃";
            }
        }
        System.out.println(buf);
    }

    // オセロ盤の枠を表す罫線を1行分表示する
private void printBoardOtherLine(String left, String middle, String right) {
    String buf = "  " + left;
    for (int x = 0; x < this.size - 1; x ++) {
        buf += "━" + middle;
    }
    System.out.println(buf + "━" + right);
}
}
