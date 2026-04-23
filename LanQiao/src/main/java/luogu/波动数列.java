package luogu;

import java.util.Scanner;

public class 波动数列 {


        static int n;
        static int s;
        static int a;
        static int b;
        static int count;

        public static void main(String[] args){
            // 输入数据
            Scanner s1 = new Scanner(System.in);
            Scanner s2 = new Scanner(s1.nextLine());
            int arr[] = new int[4];
            int len = 0;
            while(s2.hasNext())
            {
                arr[len] = s2.nextInt();
                len++;
            }

            n = arr[0];
            s = arr[1];
            a = arr[2];
            b = arr[3];

            //首位从-9开始判断
            for(int i=-9; i<=9; i++)
            {
                fun(i,0,i);
            }

            System.out.println(count%100000007);
        }

        static int sum = 0;
        // i表示所处理的第一个元素标记，j表示数列所进行长度，
        public static void fun(int i,int j, int k)
        {
            if(i>9)
            {
                return;
            }
            //若到达末尾，判断和是否为s，方案加1，sum清0
            if(j==n)
            {
                if(sum == s)
                {
                    count++;
                }
                sum = 0;
                return ;
            }
            //k为每位的值
            sum = sum + k;//计算数组的和
            fun(i, j+1, k+a);
            fun(i, j+1, k-b);

        }
    }




