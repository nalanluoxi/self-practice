package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：库存管理3
 * @Date：2025/7/13 11:55
 * @Filename：库存管理3
 */
public class 库存管理3 {
    public static int[] inventoryManagement(int[] stock, int cnt) {
        Arrays.sort(stock);
        int[]ans=new int[cnt];
        for (int i = 0; i < ans.length; i++) {
            ans[i]=stock[i];
        }
        return ans;
    }
}
