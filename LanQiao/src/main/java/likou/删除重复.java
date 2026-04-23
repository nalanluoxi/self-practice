package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：删除重复
 * @Date：2025/1/22 9:32
 * @Filename：删除重复
 */
public class 删除重复 {
    public static void main(String[] args) {
        int[] num = {0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 4, 4, 4, 4};
        int i = removeDuplicates(num);
        System.out.println(i);
        for (int i1 = 0; i1 < num.length; i1++) {
            System.out.print(num[i1] + " , ");
        }

    }

    public static int removeDuplicates(int[] nums) {
        if (nums.length <=2) {
            return nums.length;
        }
        int k=2;
        for (int i = 2; i < nums.length; i++) {
            if (nums[i]!=nums[k-2]){
                nums[k]=nums[i];
                k++;
            }
        }

        return  k;
    }

}
