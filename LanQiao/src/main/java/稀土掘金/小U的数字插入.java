package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：小U的数字插入
 * @Date：2024/12/29 22:28
 * @Filename：小U的数字插入
 */
public class 小U的数字插入 {


    public static int solution(int a, int b) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        String numa = String.valueOf(a);
        String numb = String.valueOf(b);
        Integer max=0;
        for (int i = 0; i <= numa.length(); i++) {
            String tempnum = addIndex(numa, numb, i);
            max=Math.max(max,Integer.valueOf(tempnum));
        }
        return max;
    }

    public static String addIndex(String str1,String str2,int index){
        if (index==0){
            return str2+str1;
        }
        return str1.substring(0,index)+str2+str1.substring(index);
    }

    public static void main(String[] args) {
         System.out.println(solution(76543, 4) == 765443);
        System.out.println(solution(1, 0) == 10);
        System.out.println(solution(44, 5) == 544);
          System.out.println(solution(666, 6) == 6666);
        System.out.println(solution(9, 15));
    }
}
