package jp.ac.uryukyu.ie.e205752;

/**
 * オセロの処理をするクラス 
 * オセロ盤の一辺(8, 10, 12, 14, 16)
 * 各マスの状態(B:黒, W:白, N:石なし)
 * プレイヤーの石の色
 * 相手の石の色
 * ターンを数える
 */
public class OthelloBoard {
    private int size;
    private char[][] squares;
    private char playerColor;
    private char otherColor;
    private int turnCounter;
    private final String alphabets = "abcdefghijklmnop";
}