package likou.贪心;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：岛屿的最大面积
 * @Date：2025/5/12 9:15
 * @Filename：岛屿的最大面积
 */
public class 岛屿的最大面积 {
    public static void main(String[] args) {
        int[][] nums = {
                {0,0,1,0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,1,1,0,1,0,0,0,0,0,0,0,0},
                {0,1,0,0,1,1,0,0,1,0,1,0,0},
                {0,1,0,0,1,1,0,0,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,0,0,0,0,0,0,1,1,0,0,0,0}
        };
        System.out.println(maxAreaOfIsland(nums));
    }

    static int[][]visited;
    static int ans;
    public static int maxAreaOfIsland(int[][] grid) {
        ans=0;
        visited=new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
               if (grid[i][j]==1 && visited[i][j]==0){
                   ans=Math.max(ans,dfs(grid,i,j));
               }
            }
        }
        return ans;
    }

    public static int dfs(int[][] grid,int x,int y){
        int all=1;
        visited[x][y]=1;
        if (x+1< grid.length && grid[x+1][y]==1 && visited[x+1][y]==0){
            all+=dfs(grid,x+1,y);
        }
        if (x-1>=0 && grid[x-1][y]==1 && visited[x-1][y]==0){
            all+=dfs(grid,x-1,y);
        }
        if (y+1< grid[0].length && grid[x][y+1]==1 && visited[x][y+1]==0){
            all+=dfs(grid,x,y+1);
        }
        if (y-1>=0 && grid[x][y-1]==1 && visited[x][y-1]==0){
            all+=dfs(grid,x,y-1);
        }
        return all;
    }
}
