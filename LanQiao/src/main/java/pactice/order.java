package pactice;

import java.util.ArrayList;

public class order {
    public static void main(String[] args) {
        int[] num={1,7,4,9,2,5};
        wiggleMaxLength(num);
        wiggleMaxLength2(num);
    }

    public static int wiggleMaxLength2(int[] nums){
        if (nums.length<=1){
            return nums.length;
        }

        int curDiff=0;
        int perDiff=0;
        int count=1;

        for (int i = 1; i < nums.length; i++) {
            curDiff = nums[i] - nums[i - 1];
            if ( (curDiff > 0 && perDiff <= 0) || (curDiff < 0 && perDiff >= 0)) {
                perDiff = curDiff;
                count++;
            }
        }
        System.out.println("2:"+count);
        return count;
    }


     public static int wiggleMaxLength(int[] nums) {
        if (nums.length <= 1) {
            return nums.length;
        }
        //当前差值
        int curDiff = 0;
        //上一个差值
        int preDiff = 0;
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            //得到当前差值
            curDiff = nums[i] - nums[i - 1];
            //如果当前差值和上一个差值为一正一负
            //等于0的情况表示初始时的preDiff
            if ((curDiff > 0 && preDiff <= 0) || (curDiff < 0 && preDiff >= 0)) {
                count++;
                preDiff = curDiff;
            }
        }
         System.out.println(count);
        return count;
    }

   /* public static int wiggleMaxLength(int[] nums) {
       int len=nums.length-1;
       int [] xnext=new int[len];

       if (nums.length==1){
           return 1;
       }

        /*xnext  相邻数据差的集合*/
       /* for (int i = 0; i < nums.length; i++) {
            if ((i+1)<=len){
                xnext[i]=nums[i+1]-nums[i];
            }
        }*/

       /* Integer count=2;
       for (int i = 0; i < xnext.length; i++) {
            if ((i+1)<xnext.length&&nums[i+1]!=0&&xnext[i+1]!=0){
                if (xnext[i]/xnext[i+1]<=0){
                    count++;
                }
            }
            if (nums[i+1]==0){
                count=1;
            }
        }


        System.out.println("count:"+count);

        return count;
    }*/





}
