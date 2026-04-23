package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：岛屿数量
 * @Date：2025/3/24 11:17
 * @Filename：岛屿数量
 */
public class 岛屿数量 {
    public static void main(String[] args) {

    }

    public static int numIslands(char[][] grid) {
        int ans=0;
        if (grid.length==0 || grid[0].length==0){
            return 0;
        }
        int xlen = grid.length;
        int ylen = grid[0].length;
        for (int x = 0; x < xlen; x++) {
            for (int y = 0; y < ylen; y++) {
                if (grid[x][y]=='1'){
                    ans++;
                    help(grid,x,y);
                }
            }
        }
        return ans;
    }

    public static void help(char[][] nums,int x,int y){
        int xlen = nums.length;
        int ylen = nums[0].length;
        if (x<0 || x>=xlen || y<0 || y>=ylen || nums[x][y]=='0'){
            return;
        }
        nums[x][y]='0';
        help(nums,x-1,y);
        help(nums,x+1,y);
        help(nums,x,y-1);
        help(nums,x,y+1);
    }
}
