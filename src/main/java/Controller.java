import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.scene.layout.GridPane;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.LinkedHashMap;

public class Controller {
    public GridPane buttonGrid;

    public JFXListView itemsList;
    public JFXButton totalButton;

    double total;

    LinkedHashMap<String, Element> elementsMap = new LinkedHashMap<>();

    public void initialize() {
        loadElements();
        loadButtons();
        total = 0;
        totalButton.setText(moneyFormat());
    }

    public String moneyFormat() {
        String pattern = "$#,###,###,###.## \u27AD";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        decimalFormat.setMinimumFractionDigits(2);

        String format = decimalFormat.format(total);
        System.out.println(format);
        return format;
    }

    public void loadElements() {
        addElement("milk tea", 4.15);
        addElement("smooth tea", 4.15);
        addElement("flavor tea", 4.00);
        addElement("slush tea", 4.00);
        addElement("boba", 0.65);
        addElement("jelly", 0.65);
        addElement("popping pearls", 0.85);
        elementsMap.put("", null);
        addElement("large upgrade", 0.75);
    }

    public void addElement(String name, double price) {
        Element newElement = new Element(name, price);
        elementsMap.put(name, newElement);
    }

    public void loadButtons() {
        int numCols = buttonGrid.getColumnCount();

        int numElements = elementsMap.size();
        String[] elementNames = elementsMap.keySet().toArray(new String[numElements]);
        int col = 0;
        int row = 0;

        for (int i = 0; i < numElements; i++) {
            if (col == numCols) {
                col = 0;
                row++;
            }

            if (elementNames[i].equals("")) {
                col++;
            } else {
                JFXButton button = createButton(elementNames[i]);

                buttonGrid.add(button, col, row);
                col++;
            }
        }
    }

    public JFXButton createButton(String text) {
        JFXButton button = new JFXButton();

        button.setPrefHeight(buttonGrid.getPrefHeight());
        button.setPrefWidth(buttonGrid.getPrefWidth());

        button.getStylesheets().add("/stylesheets//gridbuttons.css");
        button.setText(text);

        button.setOnAction(event -> {
            JFXButton tempButton = (JFXButton) event.getSource();
            String name = tempButton.getText();
            Element element = elementsMap.get(name);
            double price = element.getPrice();
            System.out.println(name + "    " + price);
            itemsList.getItems().add(name + "    " + price);
            total += price;
            totalButton.setText(moneyFormat());
        });

        return button;
   }
}
