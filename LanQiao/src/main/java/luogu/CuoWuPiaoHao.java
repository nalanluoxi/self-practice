package luogu;

import java.util.*;

public class CuoWuPiaoHao {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int ns = Integer.parseInt(scanner.nextLine());
        ArrayList<Integer> list=new ArrayList<>();
        for (int i = 0; i < ns; i++) {
            String str = scanner.nextLine();
            for (String s : str.split(" ")) {
                list.add(Integer.parseInt(s));
            }
        }
        Integer[] nums=new Integer[list.size()];
        nums=list.toArray(nums);
        Arrays.sort(nums);
        int m=0,n=0;

        for (int i = 0; i < nums.length-1; i++) {
            if (nums[i]==nums[i+1]){
                n=nums[i];
            }
            if (nums[i]<nums[i+1]-1){
                m=nums[i]+1;
            }
            if (m*n>0){
                System.out.println(m+" "+n);
                break;
            }
        }

    }
}
