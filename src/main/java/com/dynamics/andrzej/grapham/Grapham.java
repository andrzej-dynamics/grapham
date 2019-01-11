package com.dynamics.andrzej.grapham;
import lombok.extern.slf4j.Slf4j;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

@Slf4j
public class Grapham {
    public static void main(String[] args) {
        log.info("Hello");
        final SingleGraph first = new SingleGraph("First");
        first.addNode("A");
        first.addNode("B");
        first.addNode("C");
        first.addEdge("AB", "A", "B");
        first.addEdge("BC", "B", "C");
        first.addEdge("CA", "C", "A");

        final Viewer viewer = first.display();
        final ViewerPipe viewerPipe = viewer.newViewerPipe();
        viewerPipe.addSink(first);
        viewerPipe.addViewerListener(new ViewerListener() {
            @Override
            public void viewClosed(String s) {

            }

            @Override
            public void buttonPushed(String s) {

            }

            @Override
            public void buttonReleased(String s) {

            }
        });
    }
}
