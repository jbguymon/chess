package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        for(int i = 0; i < 8; i += 7) {
            ChessGame.TeamColor color;
            if(i == 0){
                color = ChessGame.TeamColor.WHITE;
            }
            else{
                color = ChessGame.TeamColor.BLACK;
            }
            squares[i][0] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
            squares[i][1] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
            squares[i][2] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
            squares[i][3] = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
            squares[i][4] = new ChessPiece(color, ChessPiece.PieceType.KING);
            squares[i][5] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
            squares[i][6] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
            squares[i][7] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
        }
        for(int i = 1; i < 8; i += 5){
            ChessGame.TeamColor color;
            if(i == 1){
                color = ChessGame.TeamColor.WHITE;
            }
            else{
                color = ChessGame.TeamColor.BLACK;
            }
            for(int j = 0; j < 8; j++){
                squares[i][j] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
