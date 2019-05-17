package com.chenshinan.exercises.javaDiffUtils;

import difflib.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextComparator {

    private final String original;

    private final String revised;

    public TextComparator(String original, String revised) {
        this.original = original;
        this.revised = revised;
    }

    public List<Chunk> getChangesFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.CHANGE);
    }

    public List<Chunk> getInsertsFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.INSERT);
    }

    public List<Chunk> getDeletesFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.DELETE);
    }

    private List<Chunk> getChunksByType(Delta.TYPE type) throws IOException {
        final List<Chunk> listOfChanges = new ArrayList<Chunk>();
        final List<Delta> deltas = getDeltas();
        for (Delta delta : deltas) {
            if (delta.getType() == type) {
                listOfChanges.add(delta.getRevised());
            }
        }
        return listOfChanges;
    }

    public List<Delta> getDeltas() throws IOException {

        final List<String> originalFileLines = textToLines(original);
        final List<String> revisedFileLines = textToLines(revised);

        final Patch patch = DiffUtils.diff(originalFileLines, revisedFileLines);

        return patch.getDeltas();
    }

    public List<String> applyTo() throws IOException, PatchFailedException {
        final List<String> originalFileLines = textToLines(original);
        final List<String> revisedFileLines = textToLines(revised);

        final Patch patch = DiffUtils.diff(originalFileLines, revisedFileLines);
        return patch.applyTo(originalFileLines);
    }

    private List<String> textToLines(String text) throws IOException {
        return Arrays.asList(text.split("\\n"));
    }

}