package likou;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：库存管理
 * @Date：2025/7/8 15:19
 * @Filename：库存管理
 */
public class 库存管理 {
    public static void main(String[] args) {

    }

    public int[] inventoryManagement(int[] stock, int cnt) {
        Arrays.sort(stock);
        int[]ans=new int[cnt];
        for (int i = 0; i < ans.length; i++) {
            ans[i]=stock[i];
        }
        return ans;
    }
}
