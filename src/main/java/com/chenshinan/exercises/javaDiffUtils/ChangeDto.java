package com.chenshinan.exercises.javaDiffUtils;

import difflib.Chunk;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/25
 */
public class ChangeDto {

    List<Chunk> insertData;
    List<Chunk> deleteData;
    List<Chunk> changeData;

    public List<Chunk> getInsertData() {
        return insertData;
    }

    public void setInsertData(List<Chunk> insertData) {
        this.insertData = insertData;
    }

    public List<Chunk> getDeleteData() {
        return deleteData;
    }

    public void setDeleteData(List<Chunk> deleteData) {
        this.deleteData = deleteData;
    }

    public List<Chunk> getChangeData() {
        return changeData;
    }

    public void setChangeData(List<Chunk> changeData) {
        this.changeData = changeData;
    }
}
