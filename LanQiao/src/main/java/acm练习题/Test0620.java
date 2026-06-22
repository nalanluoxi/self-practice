package acm练习题;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package acm练习题
 * @date 2026-06-20 17:39
 */
public class Test0620 {

    public static void main(String[] args) {
       //test01();
       //  test02();
        //test03();
       // Nstring("NNTN");
        Nstring("BBBNNNBBNNNCCNNNNDD");
        //Nstring("IKYAIUIQLHHGHJPSEGRSBQAGMDPCBFPWCRDHEBSTLJWH");
        //test04();
    }



    public static void test06(){

    }






    //0...1
    //.#.#.
    //..*..
    //.#.#.
    //2...3
    public static void baoxiang(char[][]list){

    }

    public static void test04(){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        for (int i = 0; i < n; i++) {
            String string = in.nextLine();
            Nstring(string);
        }
    }

    public static void Nstring(String str){
        int[] arr=new int[str.length()];

        LinkedList <Integer> deque=new LinkedList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i)=='N'){
                arr[i]=1;
            }else {
                deque.add(i);
            }
        }
        if (deque.size()<=2){
            System.out.println(str.length());
            return;
        }
        if (deque.size()==str.length()){
            System.out.println(2);
            return;
        }
        int[]dp=new int[str.length()];
        int t=0;
        int index=0;


        for (int i = 0; i < arr.length; i++) {
            if (arr[i]==1){
                t++;
            }else {
                for (int j = 0; j < t; j++) {
                    dp[index++]=t;
                }
                index=i+1;
                t=0;
            }
        }
        if (t!=0){
            for (int j = 0; j < t; j++) {
                dp[index++]=t;
            }

            t=0;
        }
        int max=0;
        for (int i = 0; i < deque.size()-1; i++) {
            Integer t1 = deque.get(i);
            Integer t2 = deque.get(i + 1);
            int temp =  dp[t1 + 1]  + 2;
            if (t1>0){
                temp+=dp[t1-1];
            }
            if (t2+1<dp.length){
                temp+=dp[t2+1];
            }
            max=Math.max(max,temp);
        }
        System.out.println(max);

    }

    public static void test03(){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        for (int i = 0; i < n; i++) {
            String[] split = in.nextLine().split(" ");
            int m=Integer.valueOf(split[0]);
            int t=Integer.valueOf(split[1]);
            int m1=Integer.valueOf(split[2]);
            int t1=Integer.valueOf(split[3]);
            int m2=Integer.valueOf(split[4]);
            int t2=Integer.valueOf(split[5]);
            Paishui(m,t,m1,t1,m2,t2);
        }
    }


    public static void Paishui(int m, int t, int m1, int t1, int m2, int t2) {
       int water=0;
        for (int cur = 0; cur < t; cur++) {
            boolean start1 = (cur / t1) % 2 == 0;
            boolean start2 = (cur / t2) % 2 == 0;
            int temp=(start1?m1:0)-(start2?m2:0);
            water=Math.min(m,Math.max(0,water+temp));
        }
        System.out.println(water);
    }


    public static void test02(){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        String[] dian = in.nextLine().split(" ");
        int[]arr=new int[n];
        for (int i = 0; i < n; i++) {
            arr[i]=Integer.valueOf(dian[i]);
        }
        int nshui = in.nextInt();
        in.nextLine();
        String[] shui = in.nextLine().split(" ");
        int[]brr=new int[nshui];
        for (int i = 0; i < nshui; i++) {
            brr[i]=Integer.valueOf(shui[i]);
        }
        hongshui(arr,brr);

    }

    public static void hongshui(int[] arr,int [] brr){
        for (int i = 0; i < brr.length; i++) {
            int h = brr[i];
            //Deque<Integer> deque=new LinkedList<>();
            int last=-1;
            int num=0;
            for (int j = 0; j < arr.length; j++) {
                if (arr[j]<=h){
                    continue;
                }else {
                    if (last==-1){
                        last=j;
                        num=1;
                    }else if (last==j-1){
                        last=j;
                    }else {
                        last=j;
                        num++;
                    }
                }
            }
            System.out.println(num);
        }
    }

    public static void test022(){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        String[] dian = in.nextLine().split(" ");
        int[]arr=new int[n];
        for (int i = 0; i < n; i++) {
            arr[i]=Integer.valueOf(dian[i]);
        }
        int nshui = in.nextInt();
        in.nextLine();
        String[] shui = in.nextLine().split(" ");
        int[]brr=new int[nshui];
        for (int i = 0; i < nshui; i++) {
            brr[i]=Integer.valueOf(shui[i]);
        }
        hongshui2(arr,brr);

    }

    // 并查集
    static int[] parent;
    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    static void union(int a, int b) {
        parent[find(a)] = find(b);
    }

    public static void hongshui2(int[] arr, int[] brr) {
        int n = arr.length, m = brr.length;

        // 建筑按高度降序排列，保留原始下标
        Integer[] buildIdx = new Integer[n];
        for (int i = 0; i < n; i++) buildIdx[i] = i;
        Arrays.sort(buildIdx, (a, b) -> arr[b] - arr[a]);

        // 查询按洪水高度降序排列，保留原始下标
        Integer[] queryIdx = new Integer[m];
        for (int i = 0; i < m; i++) queryIdx[i] = i;
        Arrays.sort(queryIdx, (a, b) -> brr[b] - brr[a]);

        parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] active = new boolean[n];
        int[] ans = new int[m];
        int components = 0, bi = 0;

        for (int qi = 0; qi < m; qi++) {
            int h = brr[queryIdx[qi]];
            // 把所有高度 > h 的建筑加入，检查左右是否可合并
            while (bi < n && arr[buildIdx[bi]] > h) {
                int j = buildIdx[bi++];
                active[j] = true;
                parent[j] = j;
                components++;
                if (j > 0 && active[j - 1] && find(j) != find(j - 1)) {
                    union(j, j - 1);
                    components--;
                }
                if (j < n - 1 && active[j + 1] && find(j) != find(j + 1)) {
                    union(j, j + 1);
                    components--;
                }
            }
            ans[queryIdx[qi]] = components;
        }
        for (int a : ans) System.out.println(a);
    }

    public static void test01(){
        //System.out.println(getTwo(2));
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        while (n-- >0) { // 注意 while 处理多个 case
            int m = in.nextInt();
            in.nextLine(); // 消耗 nextInt() 留下的换行符
            String[] split = in.nextLine().split(" ");
            int[]nums=new int[m];
            for (int i = 0; i < m; i++) {
                nums[i]=Integer.parseInt(split[i]);
            }
            test(nums);
        }
    }

    public static void test(int[]nums){
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            String two = getTwo(num);
            int twonums = getNum(two);
            Integer orDefault = map.getOrDefault(twonums, 0);
            map.put(twonums, orDefault + 1);
        }
        System.out.println(map.size());
    }

    public static int getNum(String t){
        int ans=0;
        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i)=='1'){
                ans++;
            }
        }
        return ans;
    }


    public static String getTwo(int n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            sb.append(n % 2);
            n = n / 2;
        }
        return sb.reverse().toString();
    }

}
