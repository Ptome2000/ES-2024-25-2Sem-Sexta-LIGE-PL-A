package Services;

import Models.PropertyPolygon;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

import javax.swing.*;
import java.awt.*;


/**
 * The {@code PropertyInfoDialog} class is a custom dialog that displays detailed information
 * about a specific property. It extends {@link JDialog} and provides a user-friendly interface
 * to view property details.
 */
@Layer(LayerType.FRONT_END)
public class PropertyInfoDialog extends JDialog {

    /**
     * Creates a new dialog displaying information about the given property.
     *
     * @param parent   the parent frame
     * @param property the property to display
     */
    public PropertyInfoDialog(JFrame parent, PropertyPolygon property) {
        super(parent, "Property Details", true);
        setLayout(new BorderLayout(10, 10));
        setSize(350, 250);
        setLocationRelativeTo(parent);

        JPanel content = new JPanel(new GridLayout(0, 1));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        content.add(new JLabel("Property ID: " + property.getObjectId()));
        content.add(new JLabel("Owner ID: " + property.getOwner()));
        content.add(new JLabel(String.format("Area: %.2f m²", property.getShapeArea())));
        content.add(new JLabel("Parish: " + property.getFreguesia()));
        content.add(new JLabel("Municipality: " + property.getMunicipio()));
        content.add(new JLabel("District: " + property.getIlha()));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        add(content, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}