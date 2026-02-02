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
    ChessPiece[][] gameBoard = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        gameBoard[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Removes a chess piece to the chessboard
     *
     * @param position where to remove the piece from
     */
    public void removePiece(ChessPosition position){
        gameBoard[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return gameBoard[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        gameBoard = new ChessPiece[8][8];
        for(int i = 0; i < 8; i += 7){
            ChessGame.TeamColor teamColor;
            if(i == 0){
                teamColor = ChessGame.TeamColor.WHITE;
            }
            else{
                teamColor = ChessGame.TeamColor.BLACK;
            }
            gameBoard[i][0] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
            gameBoard[i][7] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
            gameBoard[i][1] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            gameBoard[i][6] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            gameBoard[i][2] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            gameBoard[i][5] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            gameBoard[i][3] = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
            gameBoard[i][4] = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        }
        for(int i = 1; i < 8; i += 5){
            ChessGame.TeamColor teamColor;
            if(i == 1){
                teamColor = ChessGame.TeamColor.WHITE;
            }
            else{
                teamColor = ChessGame.TeamColor.BLACK;
            }
            for(int j = 0; j < 8; j++){
                gameBoard[i][j] = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(gameBoard, that.gameBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(gameBoard);
    }
}
