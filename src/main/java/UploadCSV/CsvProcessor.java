package UploadCSV;

import BuildPropertyGraph.*;
import DetectAdjacentProperties.*;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.io.IOException;
import java.util.List;




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


            // Print and export graph for debugging/analysis
            //printGraph(graph);
            //exportGraphToDot(graph);

            System.out.println("\nTotal de terrenos adjacentes: " + adjacentProperties.size());

            edu.uci.ics.jung.graph.Graph<PropertyPolygon, String> jungGraph = PropertyGraphJungBuilder.buildGraph(properties);

            System.out.println("Vértices: " + jungGraph.getVertexCount());
            System.out.println("Arestas: " + jungGraph.getEdgeCount());

            GraphViewer.showGraph(jungGraph);

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
