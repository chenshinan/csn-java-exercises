package com.chenshinan.exercises.commonmark;

import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentWriter;

import java.util.Collections;
import java.util.Set;

class ImageRenderer implements NodeRenderer {

    private final TextContentWriter text;

    ImageRenderer(TextContentNodeRendererContext context) {
        this.text = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        // Return the node types we want to use this renderer for.
        return Collections.<Class<? extends Node>>singleton(IndentedCodeBlock.class);
    }

    @Override
    public void render(Node node) {
        // We only handle one type as per getNodeTypes, so we can just cast it here.
        IndentedCodeBlock codeBlock = (IndentedCodeBlock) node;
//        html.line();
//        html.tag("pre");
//        html.text(codeBlock.getLiteral());
//        html.tag("/pre");
//        html.line();
    }
}