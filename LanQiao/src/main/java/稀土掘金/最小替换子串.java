package 稀土掘金;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：最小替换子串
 * @Date：2025/1/18 10:56
 * @Filename：最小替换子串
 */
public class 最小替换子串 {


    public static void main(String[] args) {
        //  You can add more test cases here
        //System.out.println(solution("ADDF") == 1);
        //System.out.println(solution("ASAFASAFADDD") == 3);
        System.out.println(solution("AAAASSSSDDDDFFFF") == 0);
    }


    //a5 d3 f2 s2
    //A S A F A S A F A D D D
    public static int solution(String input) {
        // Please write your code here
        int[] nums = getnums(input);
        int target = input.length() / 4;
        int[] targetNums = new int[4];
        boolean flag = true;
        for (int i = 0; i < nums.length; i++) {
            targetNums[i] = Math.max(0,nums[i] - target);
            if (nums[i]!=target){
                flag=false;
            }
        }
        if (flag){
            return 0;
        }
        int minCount = input.length();
        for (int i = 0; i < input.length(); i++) {
            for (int j = i; j < input.length(); j++) {
                int [] newnums=getnums(input.substring(i,j+1));
                if (isOk(newnums,targetNums)){
                    minCount=Math.min(minCount,j-i+1);
                }
            }
        }
        return minCount;
    }

    public static boolean isOk(int[] nums, int []target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]<target[i]){
                return false;
            }
        }
        return true;
    }

    public static int[] getnums(String str) {
        int[] nums = new int[4];
        for (int i = 0; i < str.length(); i++) {
            nums[getIndex(str.charAt(i))]++;
        }
        return nums;
    }

    public static int getIndex(char c) {
        if (c == 'A') {
            return 0;
        } else if (c == 'D') {
            return 1;
        } else if (c == 'F') {
            return 2;
        } else if (c == 'S') {
            return 3;
        }
        return -1;
    }
}
