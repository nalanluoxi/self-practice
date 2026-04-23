package luogu;

import java.util.Arrays;
import java.util.Scanner;

public class Pinshizi2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Integer n = Integer.parseInt(scanner.nextLine());

        int[][] f = new int[n][3];
        for (Integer i = 0; i < n; i++) {
            f[i][0] = scanner.nextInt();
            f[i][1] = scanner.nextInt();
            f[i][2] = scanner.nextInt();
        }

        Arrays.sort(f, (o1, o2) -> o1[0] == o2[0] ? o1[1] - o2[1] : o1[0] - o2[0]);
        long count = 0, mod = (long) 1e9 + 7;
        int []f0=new int[n];
        int []f1=new int[n];
        int []f2=new int[n];
        int i=0,j=0,k=0;
        for (Integer m = 0; m < n; m++) {
            if (f[m][2]==0){
                f0[i++]=f[m][0];
            } else if (f[m][2]==1) {
                f1[j++]=f[m][0];
            }else if (f[m][2]==2) {
                f2[k++]=f[m][0];
            }
        }


        for (Integer index = 0; index < n; index++) {
            int col = f[index][2];
            int nowl = f[index][0];
            int nowr = f[index][1];
            if (col==0){
                count+=add(f1,nowl);
                count+=add(f2,nowl);
            }else if (col==1){
                count+=add(f0,nowl);
                count+=add(f2,nowl);
            }else if (col==2){
                count+=add(f0,nowl);
                count+=add(f1,nowl);
            }
        }
        count/=2;
        count%=mod;
        System.out.println(count);
    }

    public static int add(int[] f,int nowl){
        int count=0;
        int right=0;
        for (int i = f.length-1; i>=0; i++) {
            if (f[i]==0){
                right=i;
            }
            if (f[i]<nowl){
                count=right-i+1;
                return count;
            }
        }
        return count;
    }
    public static long help(int col, int[][] f, int nowl, int nowr) {
        long count = 0;
        for (int i = 0; i < f.length; i++) {
            if (f[i][2] != col) {
                if (f[i][0] > nowl && f[i][1] < nowr) {
                    count++;
                } else if (f[i][0] < nowl && f[i][1] > nowr) {
                    count++;
                }

            }
        }
        return count;
    }

    public static class 波动数列 {
        public static void main(String[] args) {


        }

        public static int n;
        public static int s;
        public static int a;
        public static int b;
        //public static int n;
        public static int[] num;
        public static int count;
        public static void bodong(){
            Scanner scanner=new Scanner(System.in);
            n=scanner.nextInt();
            s=scanner.nextInt();
            a=scanner.nextInt();
            b=scanner.nextInt();
            num[0]=1;
            num=initialize(1,num.length-1,num);

            int sum=getsum();
            count=0;
            if (sum==s){
                count++;
            }
            for (int i = 1; i < n; i++) {
                help(i);
                if (getsum()==s){
                    count++;
                }
            }



        }

        public static void help(int index){
            num[index]=num[index-1]-b;
            num=initialize(index+1,num.length-1,num);
        }

        public static int[] initialize(int startindex,int endindex,int[] nums){
            for (int i = startindex; i < endindex; i++) {
                nums[i]=nums[i-1]+a;
            }
            return nums;
        }
        public static int getsum(){
            int []n=num;
            int all=0;
            for (int i = 0; i < n.length; i++) {
                all+=n[i];
            }
            return all;
        }




    }
}
