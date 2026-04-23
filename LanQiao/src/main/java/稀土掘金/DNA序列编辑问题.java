package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：DNA序列编辑问题
 * @Date：2024/12/29 21:55
 * @Filename：DNA序列编辑问题
 */
public class DNA序列编辑问题 {


    public static void main(String[] args) {
        //  You can add more test cases here
       // System.out.println(solution("AGCTTAGC", "AGCTAGCT") == 2);
        System.out.println(solution("AGCCGAGC", "GCTAGCT") == 4);
    }


    public static int solution(String dna1, String dna2) {
        // Please write your code here
        int indextar = 0;
        int indexnow = 0;
        int count = 0;
        char tar;
        char now;

        while (indextar < dna2.length()) {
            tar = dna2.charAt(indextar);
            while (indexnow < dna1.length()) {
                now = dna1.charAt(indexnow);
                if (tar == now) {
                    indextar++;
                    indexnow++;
                    break;
                } else {
                    indexnow++;
                    count++;
                }
            }
            if (indexnow == dna1.length() && indextar < dna2.length()) {
                count += dna2.length() - indextar;
                break;
            }
        }

        return count;
    }
}
