package chess;

import java.util.ArrayList;
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

    /** The various different chess piece options */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }

    /** @return Which team this chess piece belongs to */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /** @return which type of chess piece this piece is */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to.
     * Does not take into account moves that are illegal due to leaving the king in danger.
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> legalMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        switch (piece.getPieceType()) {
            case KING   -> addKingMoves(board, myPosition, legalMoves);
            case KNIGHT -> addKnightMoves(board, myPosition, legalMoves);
            case ROOK   -> addRookMoves(board, myPosition, legalMoves);
            case BISHOP -> addBishopMoves(board, myPosition, legalMoves);
            case QUEEN  -> {
                addRookMoves(board, myPosition, legalMoves);
                addBishopMoves(board, myPosition, legalMoves);
            }
            case PAWN   -> addPawnMoves(board, myPosition, legalMoves);
        }

        return legalMoves;
    }


    private void addIfValid(ChessBoard board, ChessPosition from, ChessPosition to, List<ChessMove> moves) {
        if (to.getRow() < 1 || to.getRow() > 8 || to.getColumn() < 1 || to.getColumn() > 8) {
            return;
        }
        ChessPiece target = board.getPiece(to);
        if (target == null || target.getTeamColor() != pieceColor) {
            moves.add(new ChessMove(from, to));
        }
    }

    private void addIfValidWithPromotion(ChessBoard board, ChessPosition from, ChessPosition to, List<ChessMove> moves) {
        if (to.getRow() < 1 || to.getRow() > 8 || to.getColumn() < 1 || to.getColumn() > 8) {
            return;
        }
        ChessPiece target = board.getPiece(to);
        if (target == null || target.getTeamColor() != pieceColor) {
            moves.add(new ChessMove(from, to, PieceType.QUEEN));
            moves.add(new ChessMove(from, to, PieceType.ROOK));
            moves.add(new ChessMove(from, to, PieceType.BISHOP));
            moves.add(new ChessMove(from, to, PieceType.KNIGHT));
        }
    }

    private void addKingMoves(ChessBoard board, ChessPosition pos, List<ChessMove> moves) {
        int r = pos.getRow();
        int c = pos.getColumn();

        // all 8 directions, max 1 step
        addIfValid(board, pos, new ChessPosition(r + 1, c), moves);
        addIfValid(board, pos, new ChessPosition(r - 1, c), moves);
        addIfValid(board, pos, new ChessPosition(r, c + 1), moves);
        addIfValid(board, pos, new ChessPosition(r, c - 1), moves);
        addIfValid(board, pos, new ChessPosition(r + 1, c + 1), moves);
        addIfValid(board, pos, new ChessPosition(r + 1, c - 1), moves);
        addIfValid(board, pos, new ChessPosition(r - 1, c + 1), moves);
        addIfValid(board, pos, new ChessPosition(r - 1, c - 1), moves);
    }

    private void addKnightMoves(ChessBoard board, ChessPosition pos, List<ChessMove> moves) {
        int r = pos.getRow();
        int c = pos.getColumn();

        addIfValid(board, pos, new ChessPosition(r + 2, c + 1), moves);
        addIfValid(board, pos, new ChessPosition(r + 2, c - 1), moves);
        addIfValid(board, pos, new ChessPosition(r - 2, c + 1), moves);
        addIfValid(board, pos, new ChessPosition(r - 2, c - 1), moves);
        addIfValid(board, pos, new ChessPosition(r + 1, c + 2), moves);
        addIfValid(board, pos, new ChessPosition(r + 1, c - 2), moves);
        addIfValid(board, pos, new ChessPosition(r - 1, c + 2), moves);
        addIfValid(board, pos, new ChessPosition(r - 1, c - 2), moves);
    }

    private void addRookMoves(ChessBoard board, ChessPosition pos, List<ChessMove> moves) {
        addSlideMoves(board, pos, 1, 0, moves);
        addSlideMoves(board, pos, -1, 0, moves);
        addSlideMoves(board, pos, 0, 1, moves);
        addSlideMoves(board, pos, 0, -1, moves);
    }

    private void addBishopMoves(ChessBoard board, ChessPosition pos, List<ChessMove> moves) {
        addSlideMoves(board, pos, 1, 1, moves);
        addSlideMoves(board, pos, 1, -1, moves);
        addSlideMoves(board, pos, -1, 1, moves);
        addSlideMoves(board, pos, -1, -1, moves);
    }

    private void addSlideMoves(ChessBoard board, ChessPosition start, int row, int col, List<ChessMove> moves) {
        int r = start.getRow() + row;
        int c = start.getColumn() + col;

        while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
            ChessPosition end = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(end);

            if (target == null) {
                moves.add(new ChessMove(start, end));
            } else {
                if (target.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(start, end));
                }
                break;
            }
            r += row;
            c += col;
        }
    }

    private void addPawnMoves(ChessBoard board, ChessPosition pos, List<ChessMove> moves) {
        int r = pos.getRow();
        int c = pos.getColumn();
        boolean isWhite = pieceColor == ChessGame.TeamColor.WHITE;
        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 2 : 7;
        int promotionRow = isWhite ? 7 : 2;

        ChessPosition oneForward = new ChessPosition(r + direction, c);
        if (isValidPosition(oneForward) && board.getPiece(oneForward) == null) {
            if (r == promotionRow) {
                addPromotionMoves(pos, oneForward, moves);
            } else {
                moves.add(new ChessMove(pos, oneForward));

                // Double step from starting position
                if (r == startRow) {
                    ChessPosition twoForward = new ChessPosition(r + 2 * direction, c);
                    if (isValidPosition(twoForward) && board.getPiece(twoForward) == null) {
                        moves.add(new ChessMove(pos, twoForward));
                    }
                }
            }
        }

        int[] captureCols = {c - 1, c + 1};
        for (int capCol : captureCols) {
            if (capCol < 1 || capCol > 8) continue;

            ChessPosition capturePos = new ChessPosition(r + direction, capCol);
            if (!isValidPosition(capturePos)) continue;

            ChessPiece target = board.getPiece(capturePos);
            if (target != null && target.getTeamColor() != pieceColor) {
                if (r == promotionRow) {
                    addPromotionMoves(pos, capturePos, moves);
                } else {
                    moves.add(new ChessMove(pos, capturePos));
                }
            }
        }
    }

    private void addPromotionMoves(ChessPosition from, ChessPosition to, List<ChessMove> moves) {
        moves.add(new ChessMove(from, to, PieceType.QUEEN));
        moves.add(new ChessMove(from, to, PieceType.ROOK));
        moves.add(new ChessMove(from, to, PieceType.BISHOP));
        moves.add(new ChessMove(from, to, PieceType.KNIGHT));
    }

    private boolean isValidPosition(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
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