package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：和为K的子数组
 * @Date：2025/5/12 15:27
 * @Filename：和为K的子数组
 */
public class 和为K的子数组 {
    public static void main(String[] args) {
       //int[] nums={1,1,1};
        //int[] nums={1,2,1,2,1};
     //   int[] nums={1};
/*        int k=3;
        System.out.println(subarraySum(nums,k));*/
        int[] nums={1,-1,0};
        int k=0;
        System.out.println(subarraySum(nums,k));
    }

    static int ans;
    static Map<Integer,Integer> map;
    public static int subarraySum(int[] nums, int k) {
        ans=0;
        map=new HashMap<>();
        map.put(0,1);
       int pre=0;
        for (int i = 0; i < nums.length; i++) {
            pre+=nums[i];
            if (map.containsKey(pre-k)){
                ans+=map.get(pre-k);
            }
            map.put(pre,map.getOrDefault(pre,0)+1);
        }
        return ans;
    }

  /*  static Deque<Integer> deque;
    static int ans;
    public static int subarraySum(int[] nums, int k) {
        ans=0;
        deque=new LinkedList<>();
        int temk=k;
        int i=0;
        while (i<nums.length){
            if (deque.isEmpty()){
                if (nums[i]>k){
                    i++;
                    continue;
                }
                deque.addFirst(nums[i]);
                temk-=nums[i];
                i++;
            }
            else if (!deque.isEmpty()&&nums[i]<=temk){
                deque.addFirst(nums[i]);
                temk-=nums[i];
                i++;
            } else if (nums[i]>temk){
                while (!deque.isEmpty()&&nums[i]>temk){
                    temk+=deque.pollLast();
                }
            }
            if (temk==0){
                ans++;
            }
        }
        return ans;
    }
*/
    /*static List<List<Integer>> set;
    static List<Integer> list;
    public static int subarraySum(int[] nums, int k) {
        set=new ArrayList<>();
        list=new ArrayList<>();
        Arrays.sort(nums);
        dfs(nums,k,nums.length-1);
        return set.size();
    }
    public static void dfs(int[]num ,int k,int index){
        if (k==0){
            set.add(new ArrayList<>(list));
            return;
        }
        for (int i = index; i >=0; i--) {
            if (num[i]>k){
                continue;
            }else {
                list.add(num[i]);
                dfs(num,k-num[i],i-1);
            }
        }
    }*/
}
