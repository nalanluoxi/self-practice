package likou;

import java.util.Stack;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：每日温度
 * @Date：2025/2/24 19:42
 * @Filename：每日温度
 */
public class 每日温度 {
    public static void main(String[] args) {
        int[] ints = dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    static int[] ans;
    static int[]stack;
    static int r;
    //static Stack<Integer> stack;
    public static int[] dailyTemperatures(int[] temp) {
        ans=new int[temp.length];
        stack=new int[temp.length];
        r=0;
        //stack=new Stack<>();
        for (int i = 0; i < temp.length; i++) {

            while (r>0&&temp[i]>temp[stack[r-1]]){
                Integer lastindex = stack[--r];
                ans[lastindex]=i-lastindex;
            }
            stack[r++]=i;
        }
/*        if (stack!=null){
            Integer lastindex = stack.pop();
            ans[lastindex]=0;
        }*/
        return ans;
    }
}
