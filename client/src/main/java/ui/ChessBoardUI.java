package ui;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessBoard;

public class ChessBoardUI {
    public static void displayBoard(ChessPiece[][] board, boolean isWhite){
        System.out.print(EscapeSequences.ERASE_SCREEN);
        char[] col = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        int startRow = isWhite ? 0 : 7;
        int endRow = isWhite ? 8 : -1;
        int stepRow = isWhite ? 1 : -1;
        int startCol = isWhite ? 0 : 7;
        int endCol = isWhite ? 8 : -1;
        int stepCol = isWhite ? 1 : -1;

        System.out.print("   ");
        for(int i = startCol; i != endCol; i += stepCol){
            System.out.print(" " + col[i] + "  ");
        }
        System.out.println();
        for(int j = startRow; j != endRow; j += stepRow){
            System.out.print((8-j) + " ");
            for(int i = startCol; i != endCol; i += stepCol){
                boolean isWhiteSquare = (i + j) % 2 == 0;
                String bgColor = isWhiteSquare ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
                ChessPiece piece = board[j][i];
                String pieceSymbol = piece != null ? getPieceSymbol(piece) : EscapeSequences.EMPTY;
                System.out.print(bgColor + pieceSymbol + EscapeSequences.RESET_BG_COLOR);
            }
            System.out.println(" " + (8 - j));
        }
        System.out.print("   ");
        for(int i = startCol; i != endCol; i += stepCol){
            System.out.print(" " + col[i] + "  ");
        }
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
