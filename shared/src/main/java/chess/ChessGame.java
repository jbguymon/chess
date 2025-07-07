package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private static TeamColor teamTurnColor;
    private final ChessBoard board;
    public ChessGame() {
        teamTurnColor = TeamColor.WHITE;
        board = new ChessBoard();
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("It is not this piece's turn");
        }
        else{
            Collection<ChessMove> valMoves = validMoves(move.getStartPosition());
            if(valMoves.contains(move)){
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
                throw new InvalidMoveException("You did not make a valid move");
            }
        }
        if(board.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }
        else{
            setTeamTurn(TeamColor.BLACK);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
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
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
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
        board.resetBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
