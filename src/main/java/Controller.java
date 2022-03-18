import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.text.DecimalFormat;
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
    double tax;

    LinkedHashMap<String, Element> elementsMap = new LinkedHashMap<>();
    double[] cashAmounts = {1.0, 5.0, 10.0, 20.0, 30.0, 40.0, 50.0, 100.0};

    public void initialize() {
        loadElements();
        loadButtons();
        itemsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        itemsList.getItems().clear();

        total = 0;
        tax = 0;
        totalButton.setText(moneyFormat(0, "total button"));

        payPane.setVisible(false);

        itemsList.getItems().clear();
    }

    public String moneyFormat(double amount, String formatType) {
        String pattern = null;

        if (formatType.equals("total button")) {
            pattern = "$#,###,###,###.## \u27AD";
        } else if (formatType.equals("default")) {
            pattern = "$#,###,###,###.##";
        } else if (formatType.equals("pay screen")) {
            pattern = "- $#,###,###,###.## -";
        }

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
            total += price;
            tax = 0.07 * total;
            if (itemsList.getItems().size() == 0) {
                itemsList.getItems().add(name + "    " + moneyFormat(price, "default"));
                itemsList.getItems().add("tax    " + moneyFormat(tax, "default"));
            } else {
                itemsList.getItems().remove(itemsList.getItems().size() - 1);
                itemsList.getItems().add(name + "    " + moneyFormat(price, "default"));
                itemsList.getItems().add("tax    " + moneyFormat(tax, "default"));
            }
            totalButton.setText(moneyFormat(total + tax, "total button"));
        });

        return button;
   }

    public void startPayment() {
        if (total > 0) {
            payPane.setVisible(true);
            totalLabel.setText(moneyFormat(total + tax, "pay screen"));

            exactButton.setText(moneyFormat(total + tax, "default"));

            double rounded = Math.ceil(total + tax);
            int i;
            if (rounded == (total + tax )) {
                i = nextGreatest(rounded);
                rounded = cashAmounts[i];
            }
            roundedButton.setText(moneyFormat(rounded, "default"));

            int round2 = nextGreatest(rounded);
            if (round2 == -1) {
                round2Button.setText("");
                round3Button.setText("");
            } else {
                round2Button.setText(moneyFormat(cashAmounts[round2], "default"));
                int round3 = nextGreatest(cashAmounts[round2]);
                if (round3 == -1) {
                    round3Button.setText("");
                } else {
                    round3Button.setText(moneyFormat(cashAmounts[round3], "default"));
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
