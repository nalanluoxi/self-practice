package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：有效数独
 * @Date：2025/1/25 9:23
 * @Filename：有效数独
 */
public class 有效数独 {
    public static void main(String[] args) {
        char[][] board = {
                {'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','8','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}
        };
        boolean validSudoku = isValidSudoku(board);
        System.out.println(validSudoku);
    }

    static  int [][] rows;
    static  int [][] cols;
    static  int [][][] subBoxes;
    public static boolean isValidSudoku(char[][] board) {
        rows=new int[9][10];
        cols=new int[9][10];
        subBoxes=new int[3][3][10];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (!add(c,i,j)){
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean add(char c,int x,int y){
        if (c=='.'){
            return true;
        }
        int index=c-'0';
        if (rows[x][index]==1){
            return false;
        }else {
            rows[x][index]=1;
        }
        if (cols[y][index]==1){
            return false;
        }   else {
            cols[y][index]=1;
        }
        if (subBoxes[x/3][y/3][index]==1){
            return false;
        }   else {
            subBoxes[x/3][y/3][index]=1;
        }
        return true;
    }
}
