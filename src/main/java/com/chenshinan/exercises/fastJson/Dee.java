package com.chenshinan.exercises.fastJson;

import difflib.Chunk;
import difflib.Delta;
import difflib.PatchFailedException;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/5/20
 */
public class Dee<T> extends Delta<T> {
    public Dee(Chunk<T> original, Chunk<T> revised) {
        super(original, revised);
    }

    @Override
    public Chunk<T> getOriginal() {
        return super.getOriginal();
    }

    @Override
    public void setOriginal(Chunk<T> original) {
        super.setOriginal(original);
    }

    @Override
    public Chunk<T> getRevised() {
        return super.getRevised();
    }

    @Override
    public void setRevised(Chunk<T> revised) {
        super.setRevised(revised);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public void verify(List<T> target) throws PatchFailedException {

    }

    @Override
    public void applyTo(List<T> target) throws PatchFailedException {

    }

    @Override
    public void restore(List<T> target) {

    }

    @Override
    public TYPE getType() {
        return null;
    }
}
