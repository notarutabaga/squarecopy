import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Controller {
    public GridPane buttonGrid;
    public JFXListView<String> itemsList = new JFXListView<>();
    public JFXButton totalButton;

    public Pane payPane;
    public Label totalLabel;
    public JFXButton exactButton;
    public JFXButton roundedButton;
    public JFXButton round2Button;
    public JFXButton round3Button;
    public JFXButton customButton;

    double total;

    LinkedHashMap<String, Element> elementsMap = new LinkedHashMap<>();
    double[] cashAmounts = {1.0, 5.0, 10.0, 20.0, 30.0, 40.0, 50.0, 100.0};

    public void initialize() {
        loadElements();
        loadButtons();
        total = 0;
        totalButton.setText(moneyFormat());
        payPane.setVisible(false);
        itemsList.getItems().clear();
    }

    public String moneyFormat() {
        String pattern = "$#,###,###,###.## \u27AD";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        decimalFormat.setMinimumFractionDigits(2);

        String format = decimalFormat.format(total);
        return format;
    }

    public String payFormat() {
        String pattern = "- $#,###,###,###.## -";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        decimalFormat.setMinimumFractionDigits(2);

        String format = decimalFormat.format(total);
        return format;
    }

    public String exactFormat(double amount) {
        String pattern = "$#,###,###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        decimalFormat.setMinimumFractionDigits(2);

        String format = decimalFormat.format(amount);
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
            itemsList.getItems().add(name + "    " + exactFormat(price));
            total += price;
            totalButton.setText(moneyFormat());
        });

        return button;
   }

    public void startPayment() {
        if (total > 0) {
            payPane.setVisible(true);
            totalLabel.setText(payFormat());

            exactButton.setText(exactFormat(total));

            double rounded = Math.ceil(total);
            int i;
            if (rounded == total) {
                i = nextGreatest(rounded);
                rounded = cashAmounts[i];
            }
            roundedButton.setText(exactFormat(rounded));

            int round2 = nextGreatest(rounded);
            if (round2 == -1) {
                round2Button.setText("");
                round3Button.setText("");
            } else {
                round2Button.setText(exactFormat(cashAmounts[round2]));
                int round3 = nextGreatest(cashAmounts[round2]);
                if (round3 == -1) {
                    round3Button.setText("");
                } else {
                    round3Button.setText(exactFormat(cashAmounts[round3]));
                }
            }
        }
    }

    public int nextGreatest(double amount) {
        int len = cashAmounts.length;

        for (int i = 0; i < len; i++) {
            if (cashAmounts[i] > amount) {
                return i;
            }
        }

        return -1;
    }

    public void finishPayment() {
        initialize();
    }
}
