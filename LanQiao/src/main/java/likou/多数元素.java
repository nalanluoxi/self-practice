package likou;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：多数元素
 * @Date：2025/1/22 10:19
 * @Filename：多数元素
 */
public class 多数元素 {
    public static void main(String[] args) {

    }
    public static int majorityElement(int[] nums) {
        Map<Integer,Integer> map=new HashMap<>();
        for (int num : nums) {
            if (map.containsKey(num)){
                map.put(num,map.get(num)+1);
            }else {
                map.put(num,1);
            }
        }
        int res=0;
        int max=0;
        for (Integer i : map.keySet()) {
            if (map.get(i)>max) {
                max = map.get(i);
                res = i;
            }
        }
        return res;
    }

}
