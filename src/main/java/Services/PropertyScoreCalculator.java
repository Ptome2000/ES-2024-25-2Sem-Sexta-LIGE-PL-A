package Services;

import Models.District;
import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Responsible for calculating tourism and urbanization scores
 * and assigning them to the corresponding Municipality and Parish.
 */
public class PropertyScoreCalculator {

    /**
     * Assigns tourism scores to municipalities and urbanization scores to parishes.
     * @param districts List of Districts containing municipalities and parishes.
     * @param allProperties Flat list of all PropertyPolygons.
     */
    public static void assignScoresToRegions(List<District> districts, List<PropertyPolygon> allProperties) {
        // === 1. Count monuments (negative ID) per municipality ===
        Map<String, Integer> tourismByMunicipality = new HashMap<>();

        for (PropertyPolygon property : allProperties) {
            if (property.getObjectId() < 0) {
                String municipality = property.getMunicipio();
                tourismByMunicipality.merge(municipality, 1, Integer::sum);
            }
        }

        // === 2. Count number of properties per parish ===
        Map<String, Integer> urbanizationByParish = new HashMap<>();

        for (PropertyPolygon property : allProperties) {
            String parish = property.getFreguesia();
            urbanizationByParish.merge(parish, 1, Integer::sum);
        }

        // === 3. Normalize both scores between 0 and 1 ===
        int maxTourism = tourismByMunicipality.values().stream().max(Integer::compare).orElse(1);
        int maxUrbanization = urbanizationByParish.values().stream().max(Integer::compare).orElse(1);

        // === 4. Assign scores to the corresponding models ===
        for (District district : districts) {
            for (Municipality municipality : district.getMunicipalities()) {
                int rawTourism = tourismByMunicipality.getOrDefault(municipality.name(), 0);
                double normalizedTourism = (double) rawTourism / maxTourism;
                municipality.setTourismScore(normalizedTourism);

                for (Parish parish : municipality.getParishes()) {
                    int rawUrban = urbanizationByParish.getOrDefault(parish.name(), 0);
                    double normalizedUrban = (double) rawUrban / maxUrbanization;
                    parish.setUrbanizationScore(normalizedUrban);

                    for (PropertyPolygon property : parish.getPropertyPolygons()) {
                        property.setTourismScore(normalizedTourism);
                        property.setUrbanizationScore(normalizedUrban);
                    }
                }
            }
        }
        //TODO Retirar depois de testar
        System.out.println("\n--- Tourism Scores by Municipality ---");
        for (Map.Entry<String, Integer> entry : tourismByMunicipality.entrySet()) {
            System.out.println("Municipality: " + entry.getKey() + ", Tourism Score: " + entry.getValue());
            double normalized = (double) entry.getValue() / maxTourism;
            System.out.printf("Municipality: %s, Tourism Score: %.3f%n", entry.getKey(), normalized);

        }
        System.out.println("\n--- Urbanization Scores by Parish ---");
        for (Map.Entry<String, Integer> entry : urbanizationByParish.entrySet()) {
            System.out.println("Parish: " + entry.getKey() + ", Urbanization Score: " + entry.getValue());
            double normalized = (double) entry.getValue() / maxUrbanization;
            System.out.printf("Parish: %s, Urbanization Score: %.3f%n", entry.getKey(), normalized);
        }

        System.out.println("\n--- Tourism & Urbanization Scores by Property ---");
        for (PropertyPolygon property : allProperties) {
            if(Objects.equals(property.getMunicipio(), "Funchal") || Objects.equals(property.getMunicipio(), "Machico") ){
                System.out.printf("Property ID: %d | Municipality: %s | Parish: %s | Tourism Score: %.3f | Urbanization Score: %.3f%n",
                        property.getObjectId(),
                        property.getMunicipio(),
                        property.getFreguesia(),
                        property.getTourismScore(),
                        property.getUrbanizationScore());
            }


            }



    }
}
