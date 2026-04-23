package 蓝桥杯真题.省12A组;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省12A组
 * @Project：LanQiaoBei
 * @name：直线
 * @Date：2025/4/10 21:03
 * @Filename：直线
 */
public class 直线 {
    /*  public static void main(String[] args) {

          //long n = 420;

          int x=2;
          int y=3;
          long n=x+y+2;
          int nowx=1;
          int nowy=1;
          Long ans=0l;
          for (int i = 0; i < x; i++) {
              for (int j = 0; j < y; j++) {
                  ans+=getOne(x,y,i,j);
              }
          }
          System.out.println(ans);
          System.out.println(ans*2+n);
          System.out.println("45641");
          //System.out.println("23320");
      }

      public static long getOne(int x,int y,int nowx,int nowy){
          return (y-nowy) + ((y-nowy) / 2 +(y-nowy)%2) * (x-nowx - 1);
      }

      public static int getAdd(int n) {
          return n * (n + 1) / 2;
      }*/
  /*  public static void help(){
        HashSet<Map<Double,Double>> lines = new HashSet<>();
        //List<Map<Integer,Integer>> list = new ArrayList<>();

        *//*for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 21; j++) {
                Map<Integer,Integer> map = new HashMap<>();
                map.put(i,j);
                list.add(map);
            }
        }*//*

        //得到两个直线点
        double x1=0,x2=0,y1=0,y2=0;
        int x=20;
        int y=21;
        for ( x1 = 0; x1 < x; x1++) {
            for ( y1 = 0; y1 < y; y1++) {
                for ( x2 = 0; x2 < x; x2++) {
                    for ( y2 = 0; y2 < y; y2++) {
                        if (x1 == x2 || y1 == y2) {
                            continue;
                        }

                        double k = (y2 - y1) / (x2 - x1);
                        double b = (x2 * y1 - x1 * y2) / (x2 - x1);
                        if (b==-0.0){
                            b=0.0;
                        }
                        Map<Double, Double> temp = new HashMap<>();
                        temp.put(k, b);
                        if (lines.add(temp)) {
                            System.out.println("("+x1+", "+"y1"+") : ("+x2+", "+y2+")  "  +"k:" + k + "  b:" + b);
                        }
                    }
                }
            }
        }
*/
  /*      for (int i = 0; i < list.size(); i++) {
            for (int j = i+1; j < list.size(); j++) {
                for (Map.Entry<Integer, Integer> entry : list.get(i).entrySet()) {
                    x1 = entry.getKey();
                    y1 = entry.getValue();
                }
                for (Map.Entry<Integer, Integer> entry : list.get(j).entrySet()) {
                    x2 = entry.getKey();
                    y2 = entry.getValue();
                }

                if (x1 == x2 || y1 == y2) {
                    continue;
                }

                double k = (y2 - y1) / (x2 - x1);
                double b = (x2 * y1 - x1 * y2) / (x2 - x1);
                Map<Double, Double> temp = new HashMap<>();
                temp.put(k, b);
                if (lines.add(temp)) {
                    System.out.println("("+x1+", "+"y1"+") : ("+x2+", "+y2+")  "  +"k:" + k + "  b:" + b);
                }
            }
        }*/
     /*   System.out.println(lines.size());
        System.out.println(lines.size()+20+21);*/

    public static void help2(){
        Set<Map<Double, Double>> set = new HashSet<>();
        int x = 20;
        int y = 21;
        for (double x1 = 0; x1 < x; x1++) {
            for (double y1 = 0; y1 < y; y1++) {
                for (double x2 = 0; x2 < x; x2++) {
                    for (double y2 = 0; y2 < y; y2++) {
                        if (x1 == x2 || y1 == y2) {
                            continue;
                        }
                        double k = (y2 - y1) / (x2 - x1);
                        double b = (x2 * y1 - x1 * y2) / (x2 - x1);
                        if (b==-0.0){
                            b=0.0;
                        }
                        Map<Double, Double> map = new HashMap<>();
                        map.put(k, b);
                        if (set.add(map)) {
                            //System.out.println("("+x1+" , "+"y1"+") : ("+x2+" , "+y2+")  "  +"k:" + k + "  b:" + b);
                        }
                    }
                }
            }
        }
        System.out.println(set.size());
        System.out.println(set.size() + 20 + 21);
    }

    public static void main(String[] args) {
        help2();
        /*
*/
    }

    /*static Map<Integer, List<Integer>> map = new HashMap<>();*/
   /* static Set<Function> set = new HashSet<>();

    public static boolean addFunction(Function f) {
        for (Function function : set) {
            if (function.equals(f)) {
                return false;
            }
        }
        set.add(f);
        return true;
    }

    public static void main(String[] args) {
        int x = 19;
        int y = 20;
        int ans = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < x; k++) {
                    for (int l = 0; l < y; l++) {
                        if (i == k || j == l) {
                            continue;
                        }
                        Function newfunction = new Function(i, j, k, l);
                        if (addFunction(newfunction)) {
                            ans++;
                            System.out.println(newfunction.toString());
                        }
                    }
                }
            }
        }
        System.out.println(ans);
        System.out.println(32843);
        System.out.println(40257-ans);

    }

    public static class Function {
        int a;
        int b;
        int c;

        public Function(int x1, int y1, int x2, int y2) {
            this.a = y1 - y2;
            this.b = x2 - x1;
            this.c = x1 * y2 - x2 * y1;
        }

        public boolean equals(Function f) {
            boolean b1 = Math.abs(this.a * f.b - this.b * f.a) == 0;
            boolean b2 = Math.abs(this.a * f.c - this.c * f.a) == 0;
            boolean b3 = Math.abs(this.b * f.c - this.c * f.b) == 0;
            return b1 && b2 && b3;
        }

        @Override
        public String toString() {
            return "Function{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    '}';
        }
    }
*/
/*    public static boolean addSet(int k, int b) {
        if (map.containsKey(k)) {
            List<Integer> list = map.get(k);
            if (list.contains(b)) {
                return false;
            }else {
                list.add(b);
                return true;
            }
        }else {
            List<Integer> list = new ArrayList<>();
            list.add(b);
            map.put(k,list);
            return true;
        }
    }*/

/*    public static int[] getfun(int x1, int y1, int x2, int y2) {
        int k = Integer.MAX_VALUE;
        if (x2 - x1 != 0) {
            k = (y2 - y1) / (x2 - x1);
        }
        int b = y1 - k * x1;
        return new int[]{k, b};
    }*/



 /*   public static void main(String[] args) {
        HashSet<Map<Double,Double>> lines = new HashSet<>();
        List<Map<Integer,Integer>> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 21; j++) {
                Map<Integer,Integer> map = new HashMap<>();
                map.put(i,j);
                list.add(map);
            }
        }

        //得到两个直线点
        double x1=0,x2=0,y1=0,y2=0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i+1; j < list.size(); j++) {
                for (Map.Entry<Integer, Integer> entry : list.get(i).entrySet()) {
                    x1 = entry.getKey();
                    y1 = entry.getValue();
                }
                for (Map.Entry<Integer, Integer> entry : list.get(j).entrySet()) {
                    x2 = entry.getKey();
                    y2 = entry.getValue();
                }

                if (x1 == x2 || y1 == y2) {
                    continue;
                }

                double k = (y2 - y1) / (x2 - x1);
                double b = (x2 * y1 - x1 * y2) / (x2 - x1);
                Map<Double, Double> temp = new HashMap<>();
                temp.put(k, b);
                lines.add(temp);
            }
        }
        System.out.println(lines.size()+20+21);
    }*/
}
