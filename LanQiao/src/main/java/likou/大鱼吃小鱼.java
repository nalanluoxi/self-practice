package likou;

import java.io.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：大鱼吃小鱼
 * @Date：2025/2/25 18:19
 * @Filename：大鱼吃小鱼
 */
public class 大鱼吃小鱼 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken()!=StreamTokenizer.TT_EOF){
            int n = (int) in.nval;
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            out.println(turns());
        }
    }

    static int[] arr;
    public static int turns(){

        return 0;
    }
}
