package luogu;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：段式回文
 * @Date：2025/3/18 22:31
 * @Filename：段式回文
 */
public class 段式回文 {

    public static void main(String[] args) {
        //int i = longestDecomposition("ghiabcdefhelloadamhelloabcdefghi");
        int i = longestDecomposition("elvtoelvto");
        System.out.println(i);
    }

    public static int longestDecomposition(String text) {
        int ans=0;
        int l=0;
        int r=text.length()-1;
        int i=1;
        while (l<=r){
            String left = text.substring(l, l + i);
            String right = text.substring(r - i + 1, r + 1);
            if (l!=(r-i+1)&&left.equals(right)){
                System.out.println("left:"+l+" : "+left+" right:"+r+" : "+right);
                ans+=2;
                l+=i;
                r-=i;
                i=1;
            } else if (l==(r-i+1)&&left.equals(right)){
                ans+=1;
                break;
            }else {
                i++;
            }
        }

        return ans;
    }
}
