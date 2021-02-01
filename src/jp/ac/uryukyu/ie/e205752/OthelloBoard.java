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

    // 石をひっくり返す
    public void turnOverDiscs(ArrayList<Coordinates> discs) {
        for (int i = 0; i < discs.size(); i++) {
            int x = discs.get(i).x;
            int y = discs.get(i).y;
            if (this.squares[y][x] == 'B') {
                this.squares[y][x] = 'W';
            } else if (this.squares[y][x] == 'W') {
                this.squares[y][x] = 'B';
            }
        }
    }

    // 石を置ける場所(他の石をひっくり返せる場所)があるかどうか判定する
    private boolean checkSquaresForNewDisc(char myColor, char enemyColor) {
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                if (this.squares[y][x] != 'N') {
                    continue;
                }
                ArrayList<Coordinates> discs = this.checkDiscsTurnedOverAllLine(myColor, enemyColor,
                        new Coordinates(x, y), 1);
                if (discs.size() >= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // 石を置く場所が決まるまで入力を受け付ける
    private void askNewCoordinates(char myColor, char enemyColor) {
        while (true) {
            // 入力
            System.out.println("\n石を置く場所を決めてください。");
            System.out.println("[x座標 y座標](例 a 1)：");
            Scanner sc = new Scanner(System.in);
            // オセロ盤の範囲内かどうか判定する
            Coordinates newDisc = this.checkCoordinatesRange(sc.nextLine());
            if (newDisc.equals(-1, -1)) {
                // 座標が正しくない場合、再度入力させる
                System.out.println("入力が間違っています。");
                continue;
            }
            if (this.squares[newDisc.y][newDisc.x] != 'N') {
                // すでに石が置かれている場合、再度入力させる
                System.out.println("すでに石があります。");
                continue;
            }
            // 相手の石をひっくり返せるかどうか判定する
            ArrayList<Coordinates> discs = this.checkDiscsTurnedOverAllLine(
                myColor, enemyColor, newDisc, this.size*this.size);
            if (! discs.isEmpty()) {
                // ひっくり返せる石がある場合、実際に石をひっくり返す
                this.putDisc(myColor, newDisc);
                this.turnOverDiscs(discs);
                this.printDiscsTurnedOver(discs);
                return;
            }
            System.out.println("相手の石をひっくり返せません。");
        }
    }