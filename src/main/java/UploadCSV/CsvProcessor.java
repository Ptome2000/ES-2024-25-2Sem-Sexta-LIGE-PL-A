package UploadCSV;

import BuildPropertyGraph.PropertyGraphBuilder;
import DetectAdjacentProperties.*;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

import static BuildPropertyGraph.PropertyGraphBuilder.*;

/**
 * CsvProcessor is responsible for uploading, validating, and processing a CSV file
 * representing property polygons. It builds a graph based on shared coordinates and
 * visualizes it using the GraphStream library.
 * The graph can be exported to DOT format and printed as a text representation in the console.
 *
 */
public class CsvProcessor {

    /**
     * Main method to read the CSV, validate the data, create a graph based on adjacency,
     * and display the result using GraphStream.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        CsvUploader uploader = new CsvUploader();
        CsvValidator validator = new CsvValidator();

        try {
            // Start logging
            CsvLogger.logStart();

            // Upload and validate the CSV file
            List<String[]> data = uploader.uploadCsv("src/main/resources/Madeira-Moodle-1.1.csv");
            validator.validate(data);
            System.out.println("Ficheiro carregado e validado com sucesso!");

            // Convert CSV data to property polygons and detect adjacency
            List<PropertyPolygon> properties = AdjacencyDetector.convertToProperties(data);
            List<AdjacentPropertyPair> adjacentProperties = AdjacencyDetector.findAdjacentProperties(properties);

            // Build the JGraphT graph representation
            Graph<PropertyPolygon, DefaultEdge> graph = PropertyGraphBuilder.buildGraph(properties);

            System.out.println("NÚMERO DE PROPRIEDADES: " + properties.size());
            System.out.println("NÓS NO GRAFO JGRAPHT: " + graph.vertexSet().size());
            System.out.println("ARESTAS NO GRAFO JGRAPHT: " + graph.edgeSet().size());

            // Print and export graph for debugging/analysis
            printGraph(graph);
            exportGraphToDot(graph);

            // Set GraphStream to use Swing for visualization
            System.setProperty("org.graphstream.ui", "swing");

            // Convert JGraphT graph to GraphStream and set rendering attributes
            org.graphstream.graph.Graph gsGraph = convertToGraphStream(graph);
            gsGraph.setAttribute("ui.quality");
            gsGraph.setAttribute("ui.antialias");

            // Set up GraphStream viewer and view panel
            Viewer viewer = new SwingViewer(gsGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            viewer.enableAutoLayout();

            ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);

            // Create and show the custom Swing window for graph visualization
            JFrame frame = new JFrame("Visualização do Grafo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            frame.add(viewPanel);
            frame.setVisible(true);

            System.out.println("Visualização pronta!");
            System.out.println("\nTotal de terrenos adjacentes: " + adjacentProperties.size());

            CsvLogger.logEnd();

        } catch (IOException e) {
            CsvLogger.logError("Erro ao ler ficheiro CSV: " + e.getMessage());
            System.err.println("Erro ao ler ficheiro: " + e.getMessage());
        } catch (UploadCSV.CsvException e) {
            CsvLogger.logError("Erro ao validar ficheiro CSV: " + e.getMessage());
            System.err.println("Erro ao validar ficheiro: " + e.getMessage());
        } catch (Exception e) {
            CsvLogger.logError("Erro inesperado: " + e.getMessage());
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }
}
