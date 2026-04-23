package likou.回溯;

/**
 * @Author 纳兰洛熙
 * @Package：likou.回溯
 * @Project：LanQiaoBei
 * @name：单词搜索
 * @Date：2025/6/10 10:30
 * @Filename：单词搜索
 */
public class 单词搜索 {

    public static void main(String[] args) {
        char[][]board=new char[][]{{'A','B','C','E'},
                                    {'S','F','C','S'},
                                    {'A','D','E','E'}};
        String word="SEE";
        System.out.println(exist(board,word));
        System.out.println(exist2(board,word));
    }

    public static boolean exist2(char[][] board, String word) {
        int h = board.length, w = board[0].length;
        boolean[][] visited = new boolean[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                boolean flag = check(board, visited, i, j, word, 0);
                if (flag) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean check(char[][] board, boolean[][] visited, int i, int j, String s, int k) {
        if (board[i][j] != s.charAt(k)) {
            return false;
        } else if (k == s.length() - 1) {
            return true;
        }
        visited[i][j] = true;
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        boolean result = false;
        for (int[] dir : directions) {
            int newi = i + dir[0], newj = j + dir[1];
            if (newi >= 0 && newi < board.length && newj >= 0 && newj < board[0].length) {
                if (!visited[newi][newj]) {
                    boolean flag = check(board, visited, newi, newj, s, k + 1);
                    if (flag) {
                        result = true;
                        break;
                    }
                }
            }
        }
        visited[i][j] = false;
        return result;
    }



    public static boolean exist(char[][] board, String word) {
        int n=board.length;
        int m=board[0].length;
        boolean[][]visited=new boolean [n][m];
        int len=word.length();
        for(int i=0;i<len;i++){
            for(int j=0;j<len;j++){
                boolean p=help(board,i,j,visited,word,0);
                if(p){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean help(char[][]board,int i,int j,boolean [][] visited,String word,int index){
        if(index==word.length()){
            return true;
        }
        if(board[i][j]!=word.charAt(index)){
            return false;
        }
        visited[i][j]=true;
        int[][]dirs={{0,1},{0,-1},{1,0},{-1,0}};
        boolean res=false;
        for(int[] dir:dirs){
            int ni=i+dir[0];
            int nj=j+dir[1];
            if(ni>=0&&ni<board.length&&nj>=0&&nj<board[0].length){
                if(!visited[ni][nj]){
                    boolean p=help(board,ni,nj,visited,word,index+1);
                    if(p){
                        res=true;
                        break;
                    }
                }
            }
        }
        visited[i][j]=false;
        return res;

    }

}
