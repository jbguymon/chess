package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        List<ChessMove> legalMoves = new java.util.ArrayList<>(List.of());
        ChessPiece piece = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(piece.type == PieceType.KING){
            if(row + 1 <= 8){
                if(board.getPiece(new ChessPosition(row + 1, col)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row + 1, col)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col)));
                    }
                }
                if(col + 1 <= 8){
                    if (board.getPiece(new ChessPosition(row + 1, col + 1)) == null){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1)));
                    }
                    else{
                        if(board.getPiece(new ChessPosition(row + 1, col + 1)).getTeamColor() != piece.getTeamColor()) {
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1)));
                        }
                    }
                }
                if(col - 1 > 0){
                    if (board.getPiece(new ChessPosition(row + 1, col - 1)) == null){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1)));
                    }
                    else{
                        if(board.getPiece(new ChessPosition(row + 1, col - 1)).getTeamColor() != piece.getTeamColor()) {
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1)));
                        }
                    }
                }
            }
            if(row - 1 > 0){
                if(board.getPiece(new ChessPosition(row - 1, col)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col)));
                }
                if(col + 1 <= 8){
                    if (board.getPiece(new ChessPosition(row - 1, col + 1)) == null){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1)));
                    }
                    else{
                        if(board.getPiece(new ChessPosition(row - 1, col + 1)).getTeamColor() != piece.getTeamColor()) {
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1)));
                        }
                    }
                }
                if(col - 1 > 0){
                    if (board.getPiece(new ChessPosition(row - 1, col - 1)) == null){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1)));
                    }
                    else{
                        if(board.getPiece(new ChessPosition(row - 1, col - 1)).getTeamColor() != piece.getTeamColor()) {
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1)));
                        }
                    }
                }
            }
            if(col + 1 <= 8){
                if(board.getPiece(new ChessPosition(row, col + 1)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row, col + 1)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row, col + 1)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row, col + 1)));
                    }
                }
            }
            if(col - 1 > 0){
                if(board.getPiece(new ChessPosition(row, col - 1)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row, col - 1)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row, col - 1)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row, col - 1)));
                    }
                }
            }
        }
        if(piece.type == PieceType.KNIGHT){
            if(row + 1 <= 8 && col + 2 <= 8) {
                if (board.getPiece(new ChessPosition(row + 1, col + 2)) == null) {
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 2)));
                }
                else {
                    if(board.getPiece(new ChessPosition(row + 1, col + 2)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 2)));
                    }
                }
            }
            if(row - 1 > 0 && col + 2 <= 8) {
                if (board.getPiece(new ChessPosition(row - 1, col + 2)) == null) {
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 2)));
                }
                else {
                    if(board.getPiece(new ChessPosition(row - 1, col + 2)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 2)));
                    }
                }
            }
            if(row + 1 <= 8 && col - 2 > 0) {
                if (board.getPiece(new ChessPosition(row + 1, col - 2)) == null) {
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 2)));
                }
                else {
                    if(board.getPiece(new ChessPosition(row + 1, col - 2)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 2)));
                    }
                }
            }
            if(row - 1 > 0 && col - 2 > 0) {
                if (board.getPiece(new ChessPosition(row - 1, col - 2)) == null) {
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 2)));
                }
                else {
                    if(board.getPiece(new ChessPosition(row - 1, col - 2)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 2)));
                    }
                }
            }
            if(row + 2 <= 8 && col + 1 <= 8) {
                if (board.getPiece(new ChessPosition(row + 2, col + 1)) == null) {
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col + 1)));
                }
                else {
                    if(board.getPiece(new ChessPosition(row + 2, col + 1)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col + 1)));
                    }
                }
            }
            if(row - 2 > 0 && col + 1 <= 8) {
                if (board.getPiece(new ChessPosition(row - 2, col + 1)) == null) {
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col + 1)));
                }
                else {
                    if(board.getPiece(new ChessPosition(row - 2, col + 1)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col + 1)));
                    }
                }
            }
            if(row + 2 <= 8 && col - 1 > 0) {
                if (board.getPiece(new ChessPosition(row + 2, col - 1)) == null) {
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col - 1)));
                }
                else {
                    if(board.getPiece(new ChessPosition(row + 2, col - 1)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col - 1)));
                    }
                }
            }
            if(row - 2 > 0 && col - 1 > 0) {
                if (board.getPiece(new ChessPosition(row - 2, col - 1)) == null) {
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col - 1)));
                }
                else {
                    if(board.getPiece(new ChessPosition(row - 2, col - 1)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col - 1)));
                    }
                }
            }
        }
        if(piece.type == PieceType.ROOK || piece.type == PieceType.QUEEN){
            for(int i = 1; i < 8; i++){
                if(row + i > 8){
                    break;
                }
                if(board.getPiece(new ChessPosition(row + i, col)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row + i, col)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col)));
                        break;
                    }
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                if(row - i < 1){
                    break;
                }
                if(board.getPiece(new ChessPosition(row - i, col)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row - i, col)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col)));
                        break;
                    }
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                if(col + i > 8){
                    break;
                }
                if(board.getPiece(new ChessPosition(row, col + i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row, col + i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row, col + i)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row, col + i)));
                        break;
                    }
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                if(col - i < 1){
                    break;
                }
                if(board.getPiece(new ChessPosition(row, col - i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row, col - i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row, col - i)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row, col - i)));
                        break;
                    }
                    break;
                }
            }
        }
        if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN){
            for(int i = 1; i < 8; i++){
                if(row + i > 8 || col + i > 8){
                    break;
                }
                if(board.getPiece(new ChessPosition(row + i, col + i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col + i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row + i, col + i)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col + i)));
                        break;
                    }
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                if(row - i < 1 || col + i > 8){
                    break;
                }
                if(board.getPiece(new ChessPosition(row - i, col + i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col + i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row - i, col + i)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col + i)));
                        break;
                    }
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                if(row + i > 8 || col - i < 1){
                    break;
                }
                if(board.getPiece(new ChessPosition(row + i, col - i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col - i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row + i, col - i)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col - i)));
                        break;
                    }
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                if(row - i < 1 || col - i < 1){
                    break;
                }
                if(board.getPiece(new ChessPosition(row - i, col - i)) == null){
                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col - i)));
                }
                else{
                    if(board.getPiece(new ChessPosition(row - i, col - i)).getTeamColor() != piece.getTeamColor()){
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - i, col - i)));
                        break;
                    }
                    break;
                }
            }
        }
        if(piece.type == PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (row != 7) {
                    if (row == 2) {
                        if (board.getPiece(new ChessPosition(row + 1, col)) == null && board.getPiece(new ChessPosition(row + 2, col)) == null) {
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col)));
                        }
                    }
                    if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col)));
                    }
                    if (col + 1 <= 8) {
                        if (board.getPiece(new ChessPosition(row + 1, col + 1)) != null) {
                            if (board.getPiece(new ChessPosition(row + 1, col + 1)).getTeamColor() != piece.getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1)));
                            }
                        }
                    }
                    if (col - 1 > 0) {
                        if (board.getPiece(new ChessPosition(row + 1, col - 1)) != null) {
                            if (board.getPiece(new ChessPosition(row + 1, col - 1)).getTeamColor() != piece.getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1)));
                            }
                        }
                    }
                } else {
                    if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.QUEEN));
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.ROOK));
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.BISHOP));
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.KNIGHT));
                    }
                    if (col + 1 <= 8) {
                        if (board.getPiece(new ChessPosition(row + 1, col + 1)) != null) {
                            if (board.getPiece(new ChessPosition(row + 1, col + 1)).getTeamColor() != piece.getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.QUEEN));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.ROOK));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.BISHOP));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.KNIGHT));
                            }
                        }
                    }
                    if (col - 1 > 0) {
                        if (board.getPiece(new ChessPosition(row + 1, col - 1)) != null) {
                            if (board.getPiece(new ChessPosition(row + 1, col - 1)).getTeamColor() != piece.getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.QUEEN));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.ROOK));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.BISHOP));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.KNIGHT));
                            }
                        }
                    }
                }
            } else {
                if (row != 2) {
                    if (row == 7) {
                        if (board.getPiece(new ChessPosition(row - 1, col)) == null && board.getPiece(new ChessPosition(row - 2, col)) == null) {
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col)));
                        }
                    }
                    if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col)));
                    }
                    if (col + 1 <= 8) {
                        if (board.getPiece(new ChessPosition(row - 1, col + 1)) != null) {
                            if (board.getPiece(new ChessPosition(row - 1, col + 1)).getTeamColor() != piece.getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1)));
                            }
                        }
                    }
                    if (col - 1 > 0) {
                        if (board.getPiece(new ChessPosition(row - 1, col - 1)) != null) {
                            if (board.getPiece(new ChessPosition(row - 1, col - 1)).getTeamColor() != piece.getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1)));
                            }
                        }
                    }
                }
                else{
                    if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.QUEEN));
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.ROOK));
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.BISHOP));
                        legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.KNIGHT));
                    }
                    if (col + 1 <= 8) {
                        if (board.getPiece(new ChessPosition(row - 1, col + 1)) != null) {
                            if (board.getPiece(new ChessPosition(row - 1, col + 1)).getTeamColor() != piece.getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.QUEEN));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.ROOK));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.BISHOP));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.KNIGHT));
                            }
                        }
                    }
                    if (col - 1 > 0) {
                        if (board.getPiece(new ChessPosition(row - 1, col - 1)) != null) {
                            if (board.getPiece(new ChessPosition(row - 1, col - 1)).getTeamColor() != piece.getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.QUEEN));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.ROOK));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.BISHOP));
                                legalMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.KNIGHT));
                            }
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
