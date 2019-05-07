package com.chenshinan.exercises.javaDiffUtils.mydiff;

import difflib.myers.Equalizer;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/5/5
 */
public class MyersDiff<T> {
    /**
     * 默认相等规则
     */
    private final Equalizer<T> DEFAULT_EQUALIZER = (original, revised) -> original.equals(revised);
    private final Equalizer<T> equalizer;

    public MyersDiff() {
        equalizer = DEFAULT_EQUALIZER;
    }

    public MyersDiff(Equalizer<T> equalizer) {
        this.equalizer = equalizer;
    }
    /**
     * 寻找最优路径
     */
    public PathNode buildPath(List<T> orig, List<T> rev) throws Exception {
        if (orig == null)
            throw new IllegalArgumentException("original sequence is null");
        if (rev == null)
            throw new IllegalArgumentException("revised sequence is null");

        final int N = orig.size();
        final int M = rev.size();
        //最大步数（先全减后全加）
        final int MAX = N + M + 1;
        final int size = 1 + 2 * MAX;
        final int middle = size / 2;
        //构建纵坐标数组（用于存储每一步的最优路径位置）
        final PathNode diagonal[] = new PathNode[size];

        //用于获取初试位置的辅助节点
        diagonal[middle + 1] = new Snake(0, -1, null);
        //外层循环步数
        for (int d = 0; d < MAX; d++) {
            //内层循环所处斜率，以2为步长，因为从所在位置走一步，斜率只会相差2
            for (int k = -d; k <= d; k += 2) {
                //新增
                //找出对应斜率所在的位置，以及它上一步的位置（高位与低位）
                final int kmiddle = middle + k;
                final int kplus = kmiddle + 1;
                final int kminus = kmiddle - 1;

                //若k为-d，则一定是从上往下走，即i相同
                int i;
                PathNode prev;
                if ((k == -d) || (k != d && diagonal[kminus].i < diagonal[kplus].i)) {
                    i = diagonal[kplus].i;
                    prev = diagonal[kplus];
                } else {
                    //若k为d，则一定是从左往右走，即i+1
                    //若diagonal[kminus].i = diagonal[kplus].i，则最优路径一定是从左往右走，即i+1
                    i = diagonal[kminus].i + 1;
                    prev = diagonal[kminus];
                }
                //根据i与k，计算出j
                int j = i - k;
                //上一步的低位数据不再存储在数组中（每个k只清空低位即可全部清空）
                diagonal[kminus] = null;
                //当前是diff节点
                PathNode node = new DiffNode(i, j, prev);
                //判断是否去到对角线位置，若是，则生成snack节点，前节点为diff节点
                if (i > node.i)
                    node = new Snake(i, j, node);

                //设置当前位置到数组中，改变
                diagonal[kmiddle] = node;
                //新增

                //达到目标位置，返回当前node
                if (i >= N && j >= M) {
                    return diagonal[kmiddle];
                }
            }
        }
        throw new Exception("could not find a diff path");
    }

    private boolean equals(T orig, T rev) {
        return equalizer.equals(orig, rev);
    }
}
