package jp.ac.uryukyu.ie.e205752;

import java.util.ArrayList;
import java.util.Scanner;

class OthelloBoardTest {
    public static void main(String args[]) {
        OthelloBoard ob = new OthelloBoard();
        ob.start();
    }
}

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

    // プレイヤーの入力した座標がオセロ盤の範囲内かどうか判定する
    // 判定に成功すればその座標を、失敗すれば(-1, -1)を返す
    private Coordinates checkCoordinatesRange(String line) {
        String[] tokens = line.split(" ");
        // 1文字目のアルファベットから横の座標を読み取る
        int x = this.alphabets.indexOf(tokens[0]);
        if (tokens[0].length() != 1 || x < 0 || this.size <= x) {
            return new Coordinates(-1, -1);
        }
        // 残りの文字から縦の座標を読み取る
        int y;
        try {
            y = Integer.parseInt(tokens[1]);
            if (y <= 0 || this.size < y) {
                return new Coordinates(-1, -1);
            }
        } catch (NumberFormatException e) {
            return new Coordinates(-1, -1);
        }
        return new Coordinates(x, y - 1);
    }

    // 入力された座標の石が相手の石をひっくり返せるかどうか判定する
    // ひっくり返せる石の座標をArraylistにして返す
    // 引数countMaxでひっくり返せる個数の最大値を決められるので、
    // その座標に石を置けるかどうかだけの判定なら1で良い
    // ひっくり返せる石の座標をすべて返すときはsize*sizeにする
    private ArrayList<Coordinates> checkDiscsTurnedOverAllLine(char myColor, char enemyColor, Coordinates myCoordinates,
            int countMax) {
        ArrayList<Coordinates> discs = new ArrayList<Coordinates>();
        // 各方向をスキャンする
        for (int d = 0; d < 8; d++) {
            discs.addAll(this.checkDiscsTurnedOverOneLine(myColor, enemyColor, myCoordinates, d));
            // ひっくり返せる石の最大値を超えた場合は、処理を中止する
            if (discs.size() > countMax) {
                break;
            }
        }
        return discs;
    }

    // 入力された座標の石が相手の石をひっくり返せるかどうか判定する
    // 引数directionによりスキャンする向きが変わる
    // 0:0度, 1:45度, 2:90度, 3:135度, 4:180度, 5:225度, 6:270度, 7:315度
    private ArrayList<Coordinates> checkDiscsTurnedOverOneLine(
        char myColor, char enemyColor, Coordinates myCoordinates, int direction)
    {
        // ひっくり返せる石をスキャンする
        Coordinates currentCoordinates = new Coordinates(myCoordinates);
        ArrayList<Coordinates> discs = new ArrayList<Coordinates>();
        // 相手の石が続く間、隣をスキャンし続ける
        while (true) {
            // 隣の石の座標を求める
            Coordinates nextDisc = this.getNextDiscCoordinates(currentCoordinates, direction);
            if (nextDisc.equals(-1, -1)) {
                // ひっくり返せる石がない場合、空のリストを返す
                discs.clear();
                break;
            }
            if (this.squares[nextDisc.y][nextDisc.x] == enemyColor) {
                // 隣に相手の石があれば、ひっくり返すリストに仮登録する
                discs.add(nextDisc);
            } else if (this.squares[nextDisc.y][nextDisc.x] == myColor) {
                // 隣に自分の石があれば、リストを返す
                break;
            } else {
                // 隣に石がなければ、空のリストを返す
                discs.clear();
                break;
            }
            // 隣の石に進む
            currentCoordinates.copy(nextDisc);
        }
        return discs;
    }

    // 隣(方向により異なる)にある石の座標を返す
    // 座標が範囲外であれば(-1, -1)を返す
    private Coordinates getNextDiscCoordinates(Coordinates myDisc, int direction) {
        // x座標
        int x = myDisc.x;
        if (direction == 0 || direction == 1 || direction == 7) {
            x++; // 0度, 45度, 315度
        } else if (direction == 3 || direction == 4 || direction == 5) {
            x--; // 135度, 180度, 225度
        }
        // y座標
        int y = myDisc.y;
        if (direction == 1 || direction == 2 || direction == 3) {
            y--; // 45度, 90度, 135度
        } else if (direction == 5 || direction == 6 || direction == 7) {
            y++; // 225度, 270度, 315度
        }
        if (x < 0 || this.size <= x || y < 0 || this.size <= y) {
            // 座標が範囲外の場合
            return new Coordinates(-1, -1);
        }
        return new Coordinates(x, y);
    }

    // オセロ盤のサイズが決まるまで入力を受け付ける
    // このメソッドをコンストラクタのthis.sizeの右辺に貼り付けると、
    // オセロ盤のサイズを入力する処理を追加できる
    private int askBoardSize() {
        while (true) {
            System.out.println("");
            System.out.println("オセロ盤の一辺の長さを決めてください。");
            System.out.print("[8, 10, 12, 14, 16 のいずれか]：");
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            if ("8".equals(line) || "10".equals(line) || "12".equals(line) || "14".equals(line) || "16".equals(line)) {
                System.out.println("オセロ盤の一辺の長さは" + line + "です。");
                return Integer.parseInt(line);
            }
            System.out.println("入力が間違っています。");
        }
    }

    // プレイヤーの石の色が決まるまで入力を受け付ける
    private void askPlayerColor() {
        while (true) {
            System.out.println("\nあなたの石を決めてください。");
            System.out.println("[b (黒), w (白) のいずれか]：");
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            if ("b".equals(line)) {
                System.out.println("あなたの石は黒です。");
                this.playerColor = 'B';
                this.otherColor = 'W';
                return;
            } else if ("w".equals(line)) {
                System.out.println("あなたの石は白です。");
                this.playerColor = 'W';
                this.otherColor = 'B';
                return;
            }
            System.out.println("入力が間違っています。");
        }
    }

    // 指定された色の石を数える
    private int countDisc(char myColor) {
        int count = 0;
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                if (this.squares[y][x] == myColor) {
                    count++;
                }
            }
        }
        return count;
    }

    // オセロ盤を開始直後の状態にする
    private void initializeBoard() {
        for (int y = 0; y < this.size; y ++) {
            for (int x = 0; x < this.size; x ++) {
                squares[y][x] = 'N';
            }
        }
        // 中央4マスだけに石を置く
        this.putDisc('B', this.size/2 - 1, this.size/2 - 1);
        this.putDisc('B', this.size/2, this.size/2);
        this.putDisc('W', this.size/2, this.size/2 - 1);
        this.putDisc('W', this.size/2 - 1, this.size/2);
    }

    // オセロ盤の指定された座標に石を置く
    private void putDisc(char discColor, int x, int y) {
        this.squares[y][x] = discColor;
    }

    private void putDisc(char discColor, Coordinates c) {
        this.putDisc(discColor, c.x, c.y);
    }

    // ひっくり返した石の座標をすべて表示する
    private void printDiscsTurnedOver(ArrayList<Coordinates> discs) {
        System.out.println("次の石をひっくり返しました。");
        int count = 0;
        for (int i = 0; i < discs.size(); i++) {
            System.out.print(this.alphabets.substring(discs.get(i).x, discs.get(i).x + 1) + (discs.get(i).y + 1) + " ");
            count++;
            if (count == 8) {
                System.out.println("");
                count = 0;
            }
        }
        System.out.println("");
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
        for (int x = 0; x < this.size - 1; x++) {
            buf += "━" + middle;
        }
        System.out.println(buf + "━" + right);
    }
}

class Coordinates {
    public int x;
    public int y;

    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinates(Coordinates c) {
        this.x = c.x;
        this.y = c.y;
    }

    public void copy(Coordinates c) {
        this.x = c.x;
        this.y = c.y;
    }

    public boolean equals(int x, int y) {
        if (this.x == x && this.y == y) {
            return true;
        } else {
            return false;
        }
    }
}