package likou.力扣test2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：数组中重复的数据
 * @Date：2025/7/8 15:49
 * @Filename：数组中重复的数据
 */
public class 数组中重复的数据 {

    public static void main(String[] args) {
        int[]nums={4,3,2,7,8,2,3,1};
        List<Integer> l = findDuplicates(nums);
        for (Integer i : l) {
            System.out.println(i);
        }
    }
    static List<Integer> ans;
    public static List<Integer> findDuplicates(int[] nums) {
        ans=new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)){
                ans.add(num);
            }else {
                set.add(num);
            }
        }
        return ans;
    }
}
