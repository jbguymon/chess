package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
                ChessPiece proPiece = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), proPiece);
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Checks if a square can be attacked by the opposite color
     *
     * @param pos   square to check if being attacked
     * @param color defending color
     * @return whether the space is attacked or not
     */
    public boolean isSpaceAttacked(ChessPosition pos, TeamColor color){
        throw new RuntimeException("Not implemented");
    }
}
