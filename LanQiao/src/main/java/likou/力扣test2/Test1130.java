package likou.力扣test2;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：Test1130
 * @Date：2025/11/30 15:57
 * @Filename：Test1130
 */
public class Test1130 {
    public static void main(String[] args) {
        // System.out.println(lengthOfLastWord(  "   fly me   to   the moon  "  ));
        // System.out.println(lengthOfLastWord(  "luffy is still joyboy" ));

        // System.out.println(convert("PAYPALISHIRING", 4));
        //System.out.println(convert("PAYPALISHIRING", 4).equals("PINALSIGYAHRPI"));
        // System.out.println(convert2("PAYPALISHIRING", 4).equals("PINALSIGYAHRPI"));
        /*int[]nums={2,7,11,15};

        int[] ints = twoSum(nums, 9);
        System.out.println(ints[0]+" "+ints[1]);*/


        //    int[] nums={1,8,6,2,5,4,8,3,7};
        /*int[] nums={1,2,1};
        System.out.println(maxArea(nums));*/

        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> lists = threeSum(nums);
        for (List<Integer> list : lists) {
            System.out.println(list);
        }
    }


    public static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> ans = new ArrayList<>();
        if (s == null || s.length() == 0 || words == null || words.length == 0) {
            return ans;
        }
        int wordNum = words.length;
        int wordLen = words[0].length();
        int len = s.length();
        Map<String ,Integer> map = new HashMap<>();
        for (String word : words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        for (int i = 0; i < wordLen; i++) {
            Map<String ,Integer> temp=new HashMap<>();

            int left=i,right=i,num=0;
            while (right+wordLen<=len){
                String tword = s.substring(right, right + wordLen);
                right+=wordLen;
                if (map.containsKey(tword)){
                    num++;
                    int value = temp.getOrDefault(tword, 0) + 1;
                    temp.put(tword, value);
                    while (map.get(tword)<temp.get(tword)){
                        String removeWord = s.substring(left, left + wordLen);
                        temp.put(removeWord, temp.get(removeWord) - 1);
                        left+=wordLen;
                        num--;
                    }
                }else {
                    temp.clear();
                    num=0;
                    left=right;
                }
                if (num==wordNum){
                    ans.add(left);
                    String removeWord = s.substring(left, left + wordLen);
                    temp.put(removeWord, temp.get(removeWord) - 1);
                    num--;
                    left+=wordLen;
                }
            }
        }

        return ans;
    }
    public static List<Integer> findSubstring2(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.length() == 0 || words == null || words.length == 0) {
            return res;
        }
        int wordNum = words.length;
        int wordLen = words[0].length();
        // 将单词数组构建成哈希表
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        // 这里只需遍历0~wordLen即可，因为滑动窗口都是按照wordLen的倍数进行滑动的
        for (int i = 0; i < wordLen; i++) {
            Map<String, Integer> tmp = new HashMap<>();
            // 滑动窗口
            int left = i, right = i, hit = 0;
            while (right + wordLen <= s.length()) {
                String word = s.substring(right, right + wordLen);
                right += wordLen;
                if (map.containsKey(word)) {
                    int num = tmp.getOrDefault(word, 0) + 1;
                    tmp.put(word, num);
                    hit++;
                    // 出现情况三，遇到了符合的单词，但是次数超了
                    if (map.get(word) < num) {
                        // 一直移除单词，直到次数符合
                        while (map.get(word) < tmp.get(word)) {
                            String deleteWord = s.substring(left, left + wordLen);
                            tmp.put(deleteWord, tmp.get(deleteWord) - 1);
                            left += wordLen;
                            hit--;
                        }
                    }
                } else {
                    // 出现情况二，遇到了不匹配的单词，直接将 left 移动到该单词的后边
                    tmp.clear();
                    hit = 0;
                    left = right;
                }
                if (hit == wordNum) {
                    res.add(left);
                    // 出现情况一，子串完全匹配，我们将上一个子串的第一个单词从tmp中移除，窗口后移wordLen
                    String firstWord = s.substring(left, left + wordLen);
                    tmp.put(firstWord, tmp.get(firstWord) - 1);
                    hit--;
                    left = left + wordLen;
                }
            }
        }
        return res;
    }

    public static List<List<Integer>> threeSum(int[] nums) {

        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        if (nums.length == 3) {
            if (nums[0] + nums[1] + nums[2] == 0) {
                ans.add(List.of(nums[0], nums[1], nums[2]));
                return ans;
            } else {
                return ans;
            }
        }
        for (int i = 0; i < nums.length - 3; i++) {
            if (i != 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int l = i + 1;
            int r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum == 0) {
                    ans.add(List.of(nums[i], nums[l++], nums[r--]));
                    while (l < r && nums[l] == nums[l - 1]) {
                        l++;
                    }
                    while (l < r && nums[r] == nums[r + 1]) {
                        r--;
                    }
                } else if (sum < 0) {
                    l++;
                } else {
                    r--;
                }
            }
        }
        return ans;
    }

    public static int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int ans = Integer.MIN_VALUE;
        while (left < right) {
            int h = Math.min(height[left], height[right]);
            int len = right - left;
            ans = Math.max(ans, h * len);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return ans;
    }

    public static int maxA2rea(int[] height) {
        int ans = 0;
        Deque<Integer> stack = new LinkedList<>();
        for (int i = 0; i < height.length; i++) {
            int h1 = height[i];
            while (!stack.isEmpty() && h1 > height[stack.peekLast()]) {
                Integer last = stack.pollLast();
                if (stack.isEmpty()) {
                    break;
                }
                Integer left = stack.peekFirst();
                int len = last - left;
                ans = Math.max(ans, len * height[last]);
            }
            stack.addLast(i);
        }

        while (!stack.isEmpty()) {
            Integer last = stack.pollLast();
            if (stack.isEmpty()) {
                break;
            }
            Integer left = stack.peekFirst();
            int len = last - left;
            ans = Math.max(ans, len * height[last]);
        }
        return ans;
    }

    public static int[] twoSum(int[] numbers, int target) {
        int[] ans = {-1, -1};
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int all = numbers[left] + numbers[right];
            if (all == target) {
                ans[0] = left + 1;
                ans[1] = right + 1;
                return ans;
            } else if (all > target) {
                right--;
            } else {
                left++;
            }
        }

        return ans;
    }

    public static List<String> fullJustify3(String[] words, int maxWidth) {
        List<String> ans = new ArrayList<String>();
        int right = 0, n = words.length;
        while (true) {
            int left = right; // 当前行的第一个单词在 words 的位置
            int sumLen = 0; // 统计这一行单词长度之和
            // 循环确定当前行可以放多少单词，注意单词之间应至少有一个空格
            while (right < n && sumLen + words[right].length() + right - left <= maxWidth) {
                sumLen += words[right++].length();
            }

            // 当前行是最后一行：单词左对齐，且单词之间应只有一个空格，在行末填充剩余空格
            if (right == n) {
                StringBuffer sb = join(words, left, n, " ");
                sb.append(blank(maxWidth - sb.length()));
                ans.add(sb.toString());
                return ans;
            }

            int numWords = right - left;
            int numSpaces = maxWidth - sumLen;

            // 当前行只有一个单词：该单词左对齐，在行末填充剩余空格
            if (numWords == 1) {
                StringBuffer sb = new StringBuffer(words[left]);
                sb.append(blank(numSpaces));
                ans.add(sb.toString());
                continue;
            }

            // 当前行不只一个单词
            int avgSpaces = numSpaces / (numWords - 1);
            int extraSpaces = numSpaces % (numWords - 1);
            StringBuffer sb = new StringBuffer();
            sb.append(join(words, left, left + extraSpaces + 1, blank(avgSpaces + 1))); // 拼接额外加一个空格的单词
            sb.append(blank(avgSpaces));
            sb.append(join(words, left + extraSpaces + 1, right, blank(avgSpaces))); // 拼接其余单词
            ans.add(sb.toString());
        }
    }

    // blank 返回长度为 n 的由空格组成的字符串
    public static String blank(int n) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; ++i) {
            sb.append(' ');
        }
        return sb.toString();
    }

    // join 返回用 sep 拼接 [left, right) 范围内的 words 组成的字符串
    public static StringBuffer join(String[] words, int left, int right, String sep) {
        StringBuffer sb = new StringBuffer(words[left]);
        for (int i = left + 1; i < right; ++i) {
            sb.append(sep);
            sb.append(words[i]);
        }
        return sb;
    }


    public static int lengthOfLastWord2(String s) {
        String[] split = s.split(" ");
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("")) {
                continue;
            }
            ans = split[i].length();
        }
        return ans == Integer.MAX_VALUE ? 0 : ans;
    }

    public static List<String> fullJustify(String[] words, int maxWidth) {
        int index = 0;
        List<String> ans = new ArrayList<>();
        while (index != words.length) {
            List<String> now = new ArrayList<>();
            int width = maxWidth;
            while (width > 0) {
                String word = words[index];
                int len = word.length();
                now.add(word);
                width -= len;
                index++;
                width--;
            }
            ans.add(now.toString());
        }


        return ans;
    }

    public static String convert2(String s, int numRows) {
        int n = s.length(), r = numRows;
        if (r == 1 || r >= n) {
            return s;
        }
        int len = n;
        char[][] dp = new char[len][r];
        int[][] dr = {{0, 1}, {1, -1}};
        int x = 0, y = 0;
        int d = 0;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            dp[x][y] = c;
            int nx = x + dr[d % 2][0];
            int ny = y + dr[d % 2][1];
            if (nx >= len || ny >= r || ny < 0 || nx < 0) {
                d++;
            }
            x = x + dr[d % 2][0];
            y = y + dr[d % 2][1];
        }
        String ans = "";
        for (int j = 0; j < r; j++) {
            for (int i = 0; i < len; i++) {
                if (dp[i][j] != 0) {
                    ans += dp[i][j];
                }
            }
        }
        return ans;
    }


    public static String convert(String s, int numRows) {
        int n = s.length(), r = numRows;
        if (r == 1 || r >= n) {
            return s;
        }
        List<List<Character>> mat = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            ArrayList<Character> list = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                list.add('\0');
            }
            mat.add(list);
        }
        int[][] dr = {{0, 1}, {1, -1}};
        int d = 0;
        int x = 0, y = 0;
        for (int index = 0; index < n; index++) {
            mat.get(x).set(y, s.charAt(index));
            int nx = x + dr[d % 2][0];
            int ny = y + dr[d % 2][1];
            if (nx >= n || nx < 0 || ny >= r || ny < 0) {
                d++;
                d = d % 2;
            }
            x = x + dr[d % 2][0];
            y = y + dr[d % 2][1];
        }
        StringBuffer ans = new StringBuffer();
        for (int j = 0; j < r; j++) {
            for (int i = 0; i < n; i++) {
                if (mat.get(i).get(j) != '\0') {
                    ans.append(mat.get(i).get(j));
                }
            }
        }
        return ans.toString();
    }

}
