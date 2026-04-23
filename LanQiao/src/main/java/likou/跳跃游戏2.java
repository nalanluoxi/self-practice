package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：跳跃游戏2
 * @Date：2025/2/2 16:57
 * @Filename：跳跃游戏2
 */
public class 跳跃游戏2 {
    public static void main(String[] args) {

        int jump = jump(new int[]{2,3,1,1,4});
        System.out.println(jump);
    }

   /* public static int jump(int[] nums) {
        int index=0;
        int count=0;
        while (index!=nums.length-1){
            if (index+nums[index]>=nums.length-1){
                return count+1;
            }
            int next = getMax(nums, index);
            index=next;
            count++;
        }
        return count;
    }

    public static int getMax(int[] nums,int index){
        int max=nums[index+1];
        int maxindex=index+1;
        for (int i = index+1; i <nums.length&&i<=nums[index]+index ; i++) {
            if (nums[i]>=max){
                max=nums[i];
                maxindex=i;
            }
        }
        return maxindex;
    }*/
   public static int jump(int[] nums) {
       int length = nums.length;
       int end = 0;
       int maxPosition = 0;
       int steps = 0;
       for (int i = 0; i < length - 1; i++) {
           maxPosition = Math.max(maxPosition, i + nums[i]);
           if (i == end) {
               end = maxPosition;
               steps++;
           }
       }
       return steps;
   }
}
