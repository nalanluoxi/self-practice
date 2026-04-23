package example.java2;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：test
 * @Date：2025/7/2 16:53
 * @Filename：test
 */
public class test {


    public static void main(String[] args) {
        /*int [] nums={1,1,1,2,2,3};
        int[] help = help(nums, 2);
        for (int i = 0; i < help.length; i++) {
            System.out.print(help[i]+" ");
        }*/
   //     System.out.println(help(3,4));

        Vector<Integer> list=new Vector<>();


    }

   /* public static int help(int n ,int k){
        int []dp=new int[n+1];
        dp[0]=1;
        for (int i = 1; i <= n; i++) {
            for (int j = i-1; j >=i-k; j--) {
                if (j<0){
                    break;
                }
                dp[i]+=dp[j];
            }
        }
        return dp[n];
    }*/
    /*public static int[] help(int[] nums,int k){

        Map<Integer ,Integer> map=new HashMap<>();
        for(int i:nums){
            if(!map.containsKey(i)){
                map.put(i, 1);
            }else{
                map.put(i, map.get(i)+1);
            }
        }

        List<int[]>list=new ArrayList<>();
        for (Integer i : map.keySet()) {
            Integer i1 = map.get(i);
            list.add(new int[]{i1,i});
        }
        Collections.sort(list,new Comparator<>(){
            @Override
            public int compare(int[] o1, int[] o2) {
                return o2[0]-o1[0];
            }
        });
        int[] ans=new int[k];
        for (int i = 0; i < k; i++) {
            ans[i]=list.get(i)[1];
        }
        return ans;
    }*/
}
