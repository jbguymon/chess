package ui;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessBoard;
import chess.ChessPosition;

public class ChessBoardUI {
    public static void displayBoard(ChessBoard board, boolean isWhite){
        System.out.print(EscapeSequences.ERASE_SCREEN);
        char[] col = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        int startRow = isWhite ? 8 : 1;
        int endRow = isWhite ? 0 : 9;
        int stepRow = isWhite ? -1 : 1;
        int startCol = isWhite ? 1 : 8;
        int endCol = isWhite ? 9 : 0;
        int stepCol = isWhite ? 1 : -1;

        System.out.print("  ");
        for(int i = startCol; i != endCol; i += stepCol){
            System.out.print(" " + col[i - 1] + "\u2003");
        }
        System.out.println();
        for(int j = startRow; j != endRow; j += stepRow){
            System.out.print(j + " ");
            for(int i = startCol; i != endCol; i += stepCol){
                boolean isWhiteSquare = (i + j) % 2 == 1;
                String bgColor = isWhiteSquare ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
                ChessPiece piece = board.getPiece(new ChessPosition(j, i));
                String pieceSymbol = piece != null ? getPieceSymbol(piece) : EscapeSequences.EMPTY;
                System.out.print(bgColor + pieceSymbol + EscapeSequences.RESET_BG_COLOR);
            }
            System.out.println(" " + j);
        }
        System.out.print("  ");
        for(int i = startCol; i != endCol; i += stepCol){
            System.out.print(" " + col[i - 1] + "\u2003");
        }
        System.out.println();
    }

    private static String getPieceSymbol(ChessPiece piece){
        switch(piece.getPieceType()) {
            case KING:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case ROOK:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case BISHOP:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case PAWN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
            default:
                return EscapeSequences.EMPTY;
        }
    }

}
