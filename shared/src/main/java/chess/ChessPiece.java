package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        List<ChessMove> legalMoves = new java.util.ArrayList<>(List.of());
        if (piece.getPieceType() == PieceType.BISHOP || piece.getPieceType() == PieceType.QUEEN) {
            for(int i = 1; i < 8; i++){
                if(row + i > 8 || col + i > 8){
                    break;
                }
                if(board.getPiece(new ChessPosition(row + i, col + i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col + i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row + i, col + i)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col + i)));
                        break;
                    }
                }
            }
            for(int i = 1; i < 8; i++){
                if(row - i <= 0 || col + i > 8){
                    break;
                }
                if(board.getPiece(new ChessPosition(row - i, col + i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col + i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row - i, col + i)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col + i)));
                        break;
                    }
                }
            }
            for(int i = 1; i < 8; i++){
                if(row + i > 8 || col - i <= 0){
                    break;
                }
                if(board.getPiece(new ChessPosition(row + i, col - i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col - i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row + i, col - i)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col - i)));
                        break;
                    }
                }
            }
            for(int i = 1; i < 8; i++){
                if(row - i <= 0 || col - i <= 0){
                    break;
                }
                if(board.getPiece(new ChessPosition(row - i, col - i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col - i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row + i, col + i)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col - i)));
                        break;
                    }
                }
            }
        }
        if (piece.getPieceType() == PieceType.KNIGHT){
            if(row + 2 <= 8){
                if(col + 1 <= 8){
                    if(board.getPiece(new ChessPosition(row + 2, col + 1)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col + 1)));
                    }
                    else if(board.getPiece(new ChessPosition(row + 2, col + 1)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col + 1)));
                    }
                }
                if(col - 1 > 0){
                    if(board.getPiece(new ChessPosition(row + 2, col - 1)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col - 1)));
                    }
                    else if(board.getPiece(new ChessPosition(row + 2, col - 1)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col - 1)));
                    }
                }
            }
            if(row - 2 > 0){
                if(col + 1 <= 8){
                    if(board.getPiece(new ChessPosition(row - 2, col + 1)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col + 1)));
                    }
                    else if(board.getPiece(new ChessPosition(row - 2, col + 1)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col + 1)));
                    }
                }
                if(col - 1 > 0){
                    if(board.getPiece(new ChessPosition(row - 2, col - 1)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col - 1)));
                    }
                    else if(board.getPiece(new ChessPosition(row - 2, col - 1)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col - 1)));
                    }
                }
            }
            if(col + 2 <= 8){
                if(row + 1 <= 8){
                    if(board.getPiece(new ChessPosition(row + 1, col + 2)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 2)));
                    }
                    else if(board.getPiece(new ChessPosition(row + 1, col + 2)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 2)));
                    }
                }
                if(row - 1 > 0){
                    if(board.getPiece(new ChessPosition(row - 1, col + 2)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 2)));
                    }
                    else if(board.getPiece(new ChessPosition(row - 1, col + 2)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 2)));
                    }
                }
            }
            if(col - 2 > 0){
                if(row + 1 <= 8){
                    if(board.getPiece(new ChessPosition(row + 1, col - 2)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 2)));
                    }
                    else if(board.getPiece(new ChessPosition(row + 1, col - 2)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 2)));
                    }
                }
                if(row - 1 > 0){
                    if(board.getPiece(new ChessPosition(row - 1, col - 2)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 2)));
                    }
                    else if(board.getPiece(new ChessPosition(row - 1, col - 2)).pieceColor != piece.pieceColor){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 2)));
                    }
                }
            }
        }
        if (piece.getPieceType() == PieceType.ROOK || piece.getPieceType() == PieceType.QUEEN){

        }
        if (piece.getPieceType() == PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (row != 7) {
                    if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col)));
                        if (row == 2) {
                            if (board.getPiece(new ChessPosition(row + 2, col)) == null) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col)));
                            }
                        }
                    }
                    if (col != 1) {
                        if (board.getPiece(new ChessPosition(row + 1, col - 1)) != null) {
                            if (board.getPiece(new ChessPosition(row + 1, col - 1)).pieceColor != piece.pieceColor) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1)));
                            }
                        }
                    }
                    if (col != 8) {
                        if (board.getPiece(new ChessPosition(row + 1, col + 1)) != null) {
                            if (board.getPiece(new ChessPosition(row + 1, col + 1)).pieceColor != piece.pieceColor) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1)));
                            }
                        }
                    }
                } else {
                    if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                        for (PieceType PT : PieceType.values()) {
                            if (PT == PieceType.KING || PT == PieceType.PAWN) {
                                continue;
                            }
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PT));
                        }
                    }
                    if (col != 1) {
                        if (board.getPiece(new ChessPosition(row + 1, col - 1)) != null) {
                            if (board.getPiece(new ChessPosition(row + 1, col - 1)).pieceColor != piece.pieceColor) {
                                for (PieceType PT : PieceType.values()) {
                                    if (PT == PieceType.KING || PT == PieceType.PAWN) {
                                        continue;
                                    }
                                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PT));
                                }
                            }
                        }
                    }
                    if (col != 8) {
                        if (board.getPiece(new ChessPosition(row + 1, col + 1)) != null) {
                            if (board.getPiece(new ChessPosition(row + 1, col + 1)).pieceColor != piece.pieceColor) {
                                for (PieceType PT : PieceType.values()) {
                                    if (PT == PieceType.KING || PT == PieceType.PAWN) {
                                        continue;
                                    }
                                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PT));
                                }
                            }
                        }
                    }
                }
            } else {
                if (row != 2) {
                    if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col)));
                        if (row == 7) {
                            if (board.getPiece(new ChessPosition(row - 2, col)) == null) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col)));
                            }
                        }
                    }
                    if (col != 1) {
                        if (board.getPiece(new ChessPosition(row - 1, col - 1)) != null) {
                            if (board.getPiece(new ChessPosition(row - 1, col - 1)).pieceColor != piece.pieceColor) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1)));
                            }
                        }
                    }
                    if (col != 8) {
                        if (board.getPiece(new ChessPosition(row - 1, col + 1)) != null) {
                            if (board.getPiece(new ChessPosition(row - 1, col + 1)).pieceColor != piece.pieceColor) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1)));
                            }
                        }
                    }
                } else {
                    if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                        for (PieceType PT : PieceType.values()) {
                            if (PT == PieceType.KING || PT == PieceType.PAWN) {
                                continue;
                            }
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PT));
                        }
                    }
                    if (col != 1) {
                        if (board.getPiece(new ChessPosition(row - 1, col - 1)) != null) {
                            if (board.getPiece(new ChessPosition(row - 1, col - 1)).pieceColor != piece.pieceColor) {
                                for (PieceType PT : PieceType.values()) {
                                    if (PT == PieceType.KING || PT == PieceType.PAWN) {
                                        continue;
                                    }
                                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PT));
                                }
                            }
                        }
                    }
                    if (col != 8) {
                        if (board.getPiece(new ChessPosition(row - 1, col + 1)) != null) {
                            if (board.getPiece(new ChessPosition(row - 1, col + 1)).pieceColor != piece.pieceColor) {
                                for (PieceType PT : PieceType.values()) {
                                    if (PT == PieceType.KING || PT == PieceType.PAWN) {
                                        continue;
                                    }
                                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PT));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (piece.getPieceType() == PieceType.KING){

        }
        return legalMoves;
    }
}
