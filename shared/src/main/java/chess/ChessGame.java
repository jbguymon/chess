package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import static chess.ChessPiece.PieceType.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurnColor;
    ChessBoard board = new ChessBoard();

    public ChessGame() {
        teamTurnColor = TeamColor.WHITE;
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        List<ChessMove> legalMoves = new ArrayList<>(List.of());
        List<ChessMove> loopMoves;
        if(board.getPiece(startPosition) == null){
            return legalMoves;
        }
        else{
            ChessPiece myPiece = board.getPiece(startPosition);
            loopMoves = (List<ChessMove>) myPiece.pieceMoves(board, startPosition);

            for (ChessMove move : loopMoves){
                ChessPiece takenPiece = board.getPiece(move.getEndPosition());
                if(move.getPromotionPiece() == null){
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                    board.removePiece(move.getStartPosition());
                }
                else{
                    ChessPiece proPiece = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
                    board.addPiece(move.getEndPosition(), proPiece);
                    board.removePiece(move.getStartPosition());
                }
                if(!isInCheck(myPiece.getTeamColor())){
                    legalMoves.add(move);
                }
                board.addPiece(startPosition, myPiece);
                board.removePiece(move.getEndPosition());
                if(takenPiece != null){
                    board.addPiece(move.getEndPosition(), takenPiece);
                }
            }
        }
        return legalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException("There is no piece at this position.");
        }
        if(board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("It is not this piece's turn.");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if(validMoves.contains(move)){
            if(move.getPromotionPiece() == null){
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.removePiece(move.getStartPosition());
            }
            else{
                ChessPiece promPiece = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), promPiece);
                board.removePiece(move.getStartPosition());
            }
        }
        else{
            throw new InvalidMoveException("You made an illegal or invalid move.");
        }
        if(getTeamTurn() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                if(board.getPiece(new ChessPosition(i,j)) == null){
                    continue;
                }
                if(board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor){
                    return isSpaceAttacked(new ChessPosition(i, j), teamColor);
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                if(board.getPiece(new ChessPosition(i, j)) == null){
                    continue;
                }
                if(board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor){
                    continue;
                }
                Collection<ChessMove> validMovesCol = validMoves(new ChessPosition(i, j));
                if(!validMovesCol.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                if(board.getPiece(new ChessPosition(i, j)) == null){
                    continue;
                }
                if(board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor){
                    continue;
                }
                Collection<ChessMove> myCol = validMoves(new ChessPosition(i, j));
                if(!myCol.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * Checks if a square can be attacked by the opposite color
     *
     * @param pos   square to check if being attacked
     * @param color defending color
     * @return whether the space is attacked or not
     */
    public boolean isSpaceAttacked(ChessPosition pos, TeamColor color){
        int row = pos.getRow();
        int col = pos.getColumn();
        if(checkDirection(row, col, 1, 1, color, BISHOP, QUEEN)){
            return true;
        }
        if (checkDirection(row, col, 1, -1, color, BISHOP, QUEEN)) {
            return true;
        }
        if (checkDirection(row, col, -1, 1, color, BISHOP, QUEEN)) {
            return true;
        }
        if (checkDirection(row, col, -1, -1, color, BISHOP, QUEEN)) {
            return true;
        }

        // straight (rook/queen)
        if (checkDirection(row, col, 1, 0, color, ROOK, QUEEN)) {
            return true;
        }
        if (checkDirection(row, col, -1, 0, color, ROOK, QUEEN)) {
            return true;
        }
        if (checkDirection(row, col, 0, 1, color, ROOK, QUEEN)) {
            return true;
        }
        if (checkDirection(row, col, 0, -1, color, ROOK, QUEEN)) {
            return true;
        }
        if (checkKnightAttack(row, col, color)) {
            return true;
        }
        if (checkKingAttack(row, col, color)) {
            return true;
        }
        if (checkPawnAttack(row, col, color)) {
            return true;
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int rowDirection, int colDirection, TeamColor color, ChessPiece.PieceType... validTypes){
        for(int i = 1; i < 8; i++){
            int newRow = row + rowDirection * i;
            int newCol = col + colDirection * i;
            if(newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8){
                break;
            }
            ChessPiece piece = board.getPiece(new ChessPosition(newRow, newCol));
            if(piece == null) continue;
            if(piece.getTeamColor() == color) break;
            for(ChessPiece.PieceType type : validTypes) {
                if(piece.getPieceType() == type){
                    return true;
                }
            }
            break;
        }
        return false;
    }

    private boolean checkKnightAttack(int row, int col, TeamColor color){
        int[][] moves = {
                {2,1},{2,-1},{-2,1},{-2,-1},
                {1,2},{1,-2},{-1,2},{-1,-2}
        };
        for (int[] m : moves) {
            int r = row + m[0];
            int c = col + m[1];
            if (r < 1 || r > 8 || c < 1 || c > 8) {
                continue;
            }
            ChessPiece p = board.getPiece(new ChessPosition(r, c));
            if (p != null && p.getTeamColor() != color && p.getPieceType() == KNIGHT) {
                return true;
            }
        }
        return false;
    }

    private boolean checkKingAttack(int row, int col, TeamColor color){
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int r = row + dr;
                int c = col + dc;
                if (r < 1 || r > 8 || c < 1 || c > 8) {
                    continue;
                }
                ChessPiece p = board.getPiece(new ChessPosition(r, c));
                if (p != null && p.getTeamColor() != color && p.getPieceType() == KING) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkPawnAttack(int row, int col, TeamColor color) {
        int dir = (color == TeamColor.WHITE) ? 1 : -1;
        int[][] attacks = {{dir,1},{dir,-1}};
        for (int[] a : attacks) {
            int r = row + a[0];
            int c = col + a[1];
            if (r < 1 || r > 8 || c < 1 || c > 8) {
                continue;
            }
            ChessPiece p = board.getPiece(new ChessPosition(r, c));
            if (p != null && p.getTeamColor() != color && p.getPieceType() == PAWN) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurnColor == chessGame.teamTurnColor && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurnColor, board);
    }
}
