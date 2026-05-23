    public static void main(String[] args) throws IOException {
        // 单目录：assets 目录 → 输出目录
        run(
            "ALlOutput测试3/news_gujin_shop_com_d_60698_html/assets",
            "output3/news_gujin_shop_com_d_60698_html",
            1280, 800, 1, 2, 3000, 42L
        );

        // 批量：扫描整个 ALlOutputTest，结果统一写到 output/ 对应子目录
        // batchRun("oldData/ALlOutputTest", "output", false, 1280, 720, 1, 2, 3000, 42L);
    }