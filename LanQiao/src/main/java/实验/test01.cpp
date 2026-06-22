
#include<iostream>
#include<queue>
#include<algorithm>
#include<cstring>
using namespace std;

const int N = 1005;
const int INF = 0x7f7f7f7f;
int mp[N][N], depth[N][N];
bool vis[N][N];
int dx[4] = {0, 0, -1, 1};
int dy[4] = {-1, 1, 0, 0};
int m, x, y, t;

int main(){
    ios::sync_with_stdio(0), cin.tie(0), cout.tie(0);

    memset(mp, 0x7f, sizeof(mp));
    cin >> m;
    for(int i = 1; i <= m; i++){
        cin >> x >> y >> t;
        // 更新当前点和四周的点
        mp[x][y] = min(mp[x][y], t);
        for(int j = 0; j < 4; j++){
            int nx = x + dx[j], ny = y + dy[j];
            if(nx >= 0 && ny >= 0) mp[nx][ny] = min(mp[nx][ny], t);
        }
    }

    queue<pair<int,int>> q;
    q.push({0, 0});
    vis[0][0] = 1;
    depth[0][0] = 0;
    int ans = INF;
    bool found = 0;

    while(!q.empty() && !found){
        auto [cx, cy] = q.front();
        q.pop();

        for(int i = 0; i < 4; i++){
            int nx = cx + dx[i], ny = cy + dy[i];
            int nt = depth[cx][cy] + 1;

            if(nx >= 0 && ny >= 0 && !vis[nx][ny] && mp[nx][ny] > nt){
                vis[nx][ny] = 1;
                depth[nx][ny] = nt;
                q.push({nx, ny});

                // 如果到达安全区（未被炸弹波及的区域）
                if(mp[nx][ny] == INF){
                    ans = min(ans, depth[nx][ny]);
                    found = 1;
                    break;
                }
            }
        }
    }

    if(ans == INF) cout << "-1\n";
    else cout << ans << "\n";

    return 0;
}
