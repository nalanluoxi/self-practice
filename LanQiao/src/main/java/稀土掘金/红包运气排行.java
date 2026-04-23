package 稀土掘金;


import java.util.*;


/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：红包运气排行
 * @Date：2025/1/23 16:44
 * @Filename：红包运气排行
 */
public class 红包运气排行 {


    public static void main(String[] args) {
        List<String> solution = solution(12,
                Arrays.asList("aa", "aaaaaaa", "aaaa", "aaaa", "aaaa", "aaaaaaaaaa", "aaaaaaaaa", "aaaa", "aaaaaaaaaa", "aaaaaaaaa", "aaaaa", "aaaa")
                , Arrays.asList(17, 14, 11, 2, 8, 16, 14, 17, 10, 6, 5, 12));
        solution.forEach(y -> System.out.print(y + " "));
        //System.out.println(solution(4, Arrays.asList("a", "b", "c", "d"), Arrays.asList(1, 2, 2, 1)).equals(Arrays.asList("b", "c", "a", "d")));
        //System.out.println(solution(3, Arrays.asList("x", "y", "z"), Arrays.asList(100, 200, 200)).equals(Arrays.asList("y", "z", "x")));
        //System.out.println(solution(5, Arrays.asList("m", "n", "o", "p", "q"), Arrays.asList(50, 50, 30, 30, 20)).equals(Arrays.asList("m", "n", "o", "p", "q")));
    }


    public static List<String> solution(int n, List<String> s, List<Integer> x) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        List<String> res = new ArrayList<>();
        Map<String, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < s.size(); i++) {
            String index = s.get(i);
            if (map.containsKey(index)) {
                Integer old = map.get(index);
                map.remove(index);
                map.put(index, Math.max(x.get(i), old));
            } else {
                map.put(index, x.get(i));
            }
        }
        List<Integer> num = new ArrayList<>();
        map.forEach((k, v) -> {
           // System.out.println(k + " : " + v + " ,");
            num.add(v);
        });
        Collections.sort(num);
        Collections.reverse(num);
     //   System.out.println("==========================");
        for (Integer i : num) {
         //   System.out.println("i: " + i);
            for (String string : map.keySet()) {
                Integer tem = map.get(string);
            //    System.out.println(string + " : " + tem);
             //   System.out.println(i==tem);
                if (tem.equals(i)) {
                    res.add(string);
                    map.remove(string);
            //        System.out.println("找到");
                    break;
                }
            }
        }
        res.forEach(y -> System.out.print(y + " "));
        return res;
    }

}
