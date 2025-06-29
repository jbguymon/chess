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
        List<ChessMove> legalMoves =  Arrays.asList();
        if (piece.getPieceType() == PieceType.BISHOP) {

        }
        else if (piece.getPieceType() == PieceType.KNIGHT){

        }
        else if (piece.getPieceType() == PieceType.ROOK){

        }
        else if (piece.getPieceType() == PieceType.PAWN){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (row != 7) {
                    if(board.getPiece(new ChessPosition(row + 1, col)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col)));
                        if (row == 2) {
                            if (board.getPiece(new ChessPosition(row + 2, col)) == null) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col)));
                            }
                        }
                    }
                    if(col != 1){
                        if(board.getPiece(new ChessPosition(row + 1, col - 1)) != null){
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1)));
                        }
                    }
                    if(col != 8){
                        if(board.getPiece(new ChessPosition(row + 1, col + 1)) != null){
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1)));
                        }
                    }
                }
                else{
                    if(board.getPiece(new ChessPosition(row + 1, col)) == null) {
                        for(PieceType PT : PieceType.values()){
                            if (PT == PieceType.KING || PT == PieceType.PAWN){
                                continue;
                            }
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PT));
                        }
                    }
                    if(col != 1){
                        if(board.getPiece(new ChessPosition(row + 1, col - 1)) != null){
                            for(PieceType PT : PieceType.values()){
                                if (PT == PieceType.KING || PT == PieceType.PAWN){
                                    continue;
                                }
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PT));
                            }
                        }
                    }
                    if(col != 8){
                        if(board.getPiece(new ChessPosition(row + 1, col + 1)) != null){
                            for(PieceType PT : PieceType.values()){
                                if (PT == PieceType.KING || PT == PieceType.PAWN){
                                    continue;
                                }
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PT));
                            }
                        }
                    }
                }
            }
            else{
                if (row != 2) {
                    if(board.getPiece(new ChessPosition(row - 1, col)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col)));
                        if (row == 7) {
                            if (board.getPiece(new ChessPosition(row - 2, col)) == null) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col)));
                            }
                        }
                    }
                    if(col != 1){
                        if(board.getPiece(new ChessPosition(row - 1, col - 1)) != null){
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1)));
                        }
                    }
                    if(col != 8){
                        if(board.getPiece(new ChessPosition(row - 1, col + 1)) != null){
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1)));
                        }
                    }
                }
                else{
                    if(board.getPiece(new ChessPosition(row - 1, col)) == null) {
                        for(PieceType PT : PieceType.values()){
                            if (PT == PieceType.KING || PT == PieceType.PAWN){
                                continue;
                            }
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PT));
                        }
                    }
                    if(col != 1){
                        if(board.getPiece(new ChessPosition(row - 1, col - 1)) != null){
                            for(PieceType PT : PieceType.values()){
                                if (PT == PieceType.KING || PT == PieceType.PAWN){
                                    continue;
                                }
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PT));
                            }
                        }
                    }
                    if(col != 8){
                        if(board.getPiece(new ChessPosition(row - 1, col + 1)) != null){
                            for(PieceType PT : PieceType.values()){
                                if (PT == PieceType.KING || PT == PieceType.PAWN){
                                    continue;
                                }
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PT));
                            }
                        }
                    }
                }
            }
        }
        else if (piece.getPieceType() == PieceType.QUEEN){

        }
        else if (piece.getPieceType() == PieceType.KING){

        }
        return legalMoves;
    }
}
