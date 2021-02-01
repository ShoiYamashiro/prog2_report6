package jp.ac.uryukyu.ie.e205752;

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
