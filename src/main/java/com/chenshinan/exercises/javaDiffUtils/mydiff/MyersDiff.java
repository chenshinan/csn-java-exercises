package com.chenshinan.exercises.javaDiffUtils.mydiff;

import difflib.myers.Equalizer;

import java.util.ArrayList;
import java.util.Collections;
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
                //找出对应斜率所在的位置，以及它上一步的位置（高位与低位）
                final int kmiddle = middle + k;
                final int kplus = kmiddle + 1;
                final int kminus = kmiddle - 1;
                //若k为-d，则一定是从上往下走，即i相同
                //若diagonal[kminus].i < diagonal[kplus].i，则最优路径一定是从上往下走，即i相同
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
                //上一步的低位数据不再存储在数组中（每个k只清空低位即可全部清空）
                diagonal[kminus] = null;
                //根据i与k，计算出j
                int j = i - k;
                //当前是diff节点
                PathNode node = new DiffNode(i, j, prev);
                //判断被比较的两个数组中，当前位置的数据是否相同，相同，则去到对角线位置
                while (i < N && j < M && equals(orig.get(i), rev.get(j))) {
                    i++;
                    j++;
                }
                //判断是否去到对角线位置，若是，则生成snack节点，前节点为diff节点
                if (i > node.i)
                    node = new Snake(i, j, node);
                //设置当前位置到数组中
                diagonal[kmiddle] = node;
                //达到目标位置，返回当前node
                if (i >= N && j >= M) {
                    return diagonal[kmiddle];
                }
            }
            diagonal[middle + d - 1] = null;
        }
        throw new Exception("could not find a diff path");
    }

    /**
     * 打印diff
     */
    public void buildDiff(PathNode path, List<T> orig, List<T> rev) {
        List<String> result = new ArrayList<>();
        if (path == null)
            throw new IllegalArgumentException("path is null");
        if (orig == null)
            throw new IllegalArgumentException("original sequence is null");
        if (rev == null)
            throw new IllegalArgumentException("revised sequence is null");
        while (path != null && path.prev != null && path.prev.j >= 0) {
            if (path.isSnake()) {
                int endi = path.i;
                int begini = path.prev.i;
                for (int i = endi - 1; i >= begini; i--) {
                    result.add(" "+orig.get(i));
                }
            } else {
                int i = path.i;
                int j = path.j;
                int prei = path.prev.i;
                if (prei < i) {
                    result.add("【删除】" + orig.get(i - 1));
                } else {
                    result.add("【增加】" + rev.get(j - 1));
                }
            }
            path = path.prev;
        }
        Collections.reverse(result);
        for (String line : result) {
            System.out.println(line);
        }
    }

    private boolean equals(T orig, T rev) {
        return equalizer.equals(orig, rev);
    }
}
