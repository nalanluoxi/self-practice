package likou;



public class 盛水最多 {
    public static void main(String[] args) {
        int [] num={1,8,6,2,5,4,8,3,7};
        System.out.println(maxArea(num));


    }

    public  static int maxArea(int[] height) {
        int left=0;
        int right= height.length-1;
        int max=0;

        while (left<right){
            int temmax=(right-left)*Math.min(height[left],height[right]);
            max=Math.max(max,temmax);
            if (height[left]<=height[right]){
                left++;
            }else {
                right--;
            }
        }
        return max;
    }



}
