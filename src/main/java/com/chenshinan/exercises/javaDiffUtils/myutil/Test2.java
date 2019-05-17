package com.chenshinan.exercises.javaDiffUtils.myutil;

import com.chenshinan.exercises.javaDiffUtils.ChangeDto;
import com.chenshinan.exercises.javaDiffUtils.TextComparator;

import java.io.IOException;

/**
 * @author shinan.chen
 * @since 2019/4/25
 */
public class Test2 {
    public static void main(String[] args) {
        String oldText = "A\nB\nC\nA\nB\nB\nA\nX";
        String newText = "C\nB\nA\nB\nA\nC\nY";
        TextDiffDTO diffDTO = DiffUtil.diff(oldText, newText);
        String newnew = DiffUtil.applyTo(diffDTO, oldText);
        String oldold = DiffUtil.restore(diffDTO, newText);
        TextComparator comparator = new TextComparator(oldText, newText);
        ChangeDto changeDto = new ChangeDto();
        try {
            changeDto.setChangeData(comparator.getChangesFromOriginal());
            changeDto.setDeleteData(comparator.getDeletesFromOriginal());
            changeDto.setInsertData(comparator.getInsertsFromOriginal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(diffDTO);
    }
}
