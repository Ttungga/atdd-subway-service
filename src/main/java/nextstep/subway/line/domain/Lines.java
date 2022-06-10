package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Lines {
    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines valueOf(List<Line> lines) {
        return new Lines(lines);
    }

    public List<Line> lines() {
        return lines;
    }

    public GraphPath<Station, DefaultWeightedEdge> shortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(getGraph());
        return dijkstraShortestPath.getPath(source, target);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            line.makeVertexOn(graph);
            line.makeEdgeWeightOn(graph);
        }
        return graph;
    }
}
