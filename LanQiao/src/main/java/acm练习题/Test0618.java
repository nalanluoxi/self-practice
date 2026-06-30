package acm练习题;

import java.util.*;

public class Test0618 {


    // 人员实体
    static class Person {
        int id;         // 原始编号，输出结果对应顺序
        int start;      // 起点
        int end;        // 终点
        int curPos;     // 当前所在位置
        long umbrellaTime; // 撑伞总时长
        boolean inTeam; // 是否还在队伍中

        Person(int id, int start, int end) {
            this.id = id;
            this.start = start;
            this.end = end;
            this.curPos = start;
            this.umbrellaTime = 0;
            this.inTeam = true;
            // 终点<=起点，直接不在队伍
            if (end <= start) {
                this.inTeam = false;
            }
        }
    }

    public static long[] calcUmbrellaTime(int[] startArr, int[] endArr) {
        int n = startArr.length;
        Person[] people = new Person[n];
        for (int i = 0; i < n; i++) {
            people[i] = new Person(i, startArr[i], endArr[i]);
        }

        // 1. 初始队伍筛选：还能行走的人
        List<Person> team = new ArrayList<>();
        for (Person p : people) {
            if (p.inTeam) team.add(p);
        }
        if (team.isEmpty()) {
            long[] res = new long[n];
            Arrays.fill(res, 0);
            return res;
        }

        // 初始领队：队伍里起点最小（最先出发）
        Person leader = getMinPosPerson(team);

        // 模拟行走，直到队伍没人
        while (!team.isEmpty()) {
            // 2. 找出下一站：队伍所有人终点最小值
            int nextStop = Integer.MAX_VALUE;
            for (Person p : team) {
                nextStop = Math.min(nextStop, p.end);
            }
            // 本次行走距离 = 领队当前位置到下一站
            int walkDis = nextStop - leader.curPos;
            leader.umbrellaTime += walkDis;

            // 3. 全队同步移动到 nextStop
            for (Person p : team) {
                p.curPos = nextStop;
            }

            // 4. 移除到达终点的人
            List<Person> newTeam = new ArrayList<>();
            for (Person p : team) {
                if (p.curPos < p.end) {
                    newTeam.add(p);
                } else {
                    p.inTeam = false;
                }
            }
            team = newTeam;

            // 队伍空了直接结束
            if (team.isEmpty()) break;

            // 5. 判断原领队是否下车，更换新领队
            if (!leader.inTeam) {
                leader = getMinPosPerson(team);
            }
        }

        // 按原始id整理输出结果
        long[] result = new long[n];
        for (Person p : people) {
            result[p.id] = p.umbrellaTime;
        }
        return result;
    }

    // 获取队伍中当前位置最小的人（新领队）
    private static Person getMinPosPerson(List<Person> team) {
        Person minP = team.get(0);
        for (Person p : team) {
            if (p.curPos < minP.curPos) {
                minP = p;
            }
        }
        return minP;
    }

    // 测试入口
    public static void main2 (String[] args) {
        // 示例测试用例1
        int[] start = {1, 3, 2};
        int[] end = {10, 6, 12};
        long[] ans = calcUmbrellaTime(start, end);
        System.out.println(Arrays.toString(ans));
        // 输出解释看下方示例推演
    }


    // 方向：上、右、下、左 0,1,2,3
    private static final int[][] DIRS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    // / 反射映射
    private static final int[] SLASH = {3, 2, 1, 0};
    // \ 反射映射
    private static final int[] BACK_SLASH = {1, 0, 3, 2};

    private static char[][] map;
    private static int row, col;

    public static void main(String[] args) {
        // ========== 写死测试用例，无需控制台输入 ==========
        // 1. 地图大小 3行3列
        row = 3;
        col = 3;
        map = new char[row][col];
        for (int i = 0; i < row; i++) {
            Arrays.fill(map[i], '.');
        }

        // 2. k=2 个特殊格子
        // 0 1 /
        map[0][1] = '/';
        // 1 1 \
        map[1][1] = '\\';

        // 3. 起点 (0,0)
        int startX = 0;
        int startY = 0;

        // 4. 需要经过的点：2个 (2,2)、(1,1)
        Set<String> targetPoints = new HashSet<>();
        targetPoints.add("2,2");
        targetPoints.add("1,1");

        // ========== 逻辑不变 ==========
        boolean canPassAll = false;
        for (int dir = 0; dir < 4; dir++) {
            Set<String> passed = walk(startX, startY, dir);
            boolean allContain = true;
            for (String p : targetPoints) {
                if (!passed.contains(p)) {
                    allContain = false;
                    break;
                }
            }
            if (allContain) {
                canPassAll = true;
                break;
            }
        }

        System.out.println("是否存在方向经过所有必经点：" + (canPassAll ? "YES" : "NO"));
    }

    /**
     * 模拟行走，返回所有经过的坐标
     */
    private static Set<String> walk(int sx, int sy, int initDir) {
        Set<String> path = new HashSet<>();
        boolean[][][] vis = new boolean[row][col][4];

        int currX = sx;
        int currY = sy;
        int currDir = initDir;

        while (true) {
            // 越界停止
            if (currX < 0 || currX >= row || currY < 0 || currY >= col) {
                break;
            }
            // 同位置同方向重复，出现环路
            if (vis[currX][currY][currDir]) {
                break;
            }

            vis[currX][currY][currDir] = true;
            path.add(currX + "," + currY);

            char cell = map[currX][currY];
            // 墙体直接终止
            if (cell == '#') {
                break;
            }
            // 镜面改变方向
            if (cell == '/') {
                currDir = SLASH[currDir];
            } else if (cell == '\\') {
                currDir = BACK_SLASH[currDir];
            }

            // 向前移动一格
            currX += DIRS[currDir][0];
            currY += DIRS[currDir][1];
        }
        return path;
    }


}
