package com.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.*;
import com.shared.FieldVerifier;

import java.util.Arrays;
import java.util.Random;

public class Gwt implements EntryPoint {

    private VerticalPanel introScreenPanel;
    private HorizontalPanel sortScreenPanel;
    private int[] originalNumbers;
    private int[] sortedNumbers;
    private boolean isIncreasingOrder = true;

    public void onModuleLoad() {

        introScreenPanel = createIntroScreen();
        RootPanel.get("panelContainer").add(introScreenPanel);

        TextBox numberField = (TextBox) getWidget(DOM.getElementById("numberField"));
        numberField.setFocus(true);

        class IntroButtonHandler implements ClickHandler, KeyUpHandler {
            public void onClick(ClickEvent event) {
                showSortScreen();
            }

            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showSortScreen();
                }
            }

            private void showSortScreen() {
                Label errorLabel = (Label) getWidget(DOM.getElementById("errorLabel"));
                errorLabel.setText("");
                int numberCount = FieldVerifier.getNumberCount(numberField.getText());
                if (FieldVerifier.isNumberInvalid(numberCount)) {
                    errorLabel.setText("Please enter a number from 1 to 100");
                    return;
                }
                introScreenPanel.setVisible(false);
                sortScreenPanel = createSortScreen(numberCount);
                RootPanel.get("panelContainer").add(sortScreenPanel);

            }
        }

        Button enterButton = (Button) getWidget(DOM.getElementById("enterButton"));

        IntroButtonHandler handler = new IntroButtonHandler();
        enterButton.addClickHandler(handler);
        numberField.addKeyUpHandler(handler);
    }

    private VerticalPanel createIntroScreen() {
        VerticalPanel introScreenPanel = new VerticalPanel();

        Label inputLabel = new Label("How many numbers to display?");
        TextBox numberField = new TextBox();
        Button enterButton = new Button("Enter");
        Label errorLabel = new Label();

        introScreenPanel.setStyleName("screenPanel");
        inputLabel.setStyleName("inputLabel");
        numberField.setStyleName("numberField");
        enterButton.setStyleName("button");
        enterButton.addStyleName("enterButton");
        errorLabel.setStyleName("serverResponseLabelError");

        numberField.getElement().setId("numberField");
        enterButton.getElement().setId("enterButton");
        errorLabel.getElement().setId("errorLabel");

        introScreenPanel.add(inputLabel);
        introScreenPanel.add(numberField);
        introScreenPanel.add(enterButton);
        introScreenPanel.add(errorLabel);

        return introScreenPanel;
    }

    private HorizontalPanel createSortScreen(int numberCount) {
        final int maxButtonsInColumn = 10;
        int columnCount = numberCount / 10 + 1;
        int numberCountInColumn;
        int buttonIndex;
        originalNumbers = generateNumbers(numberCount);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setStyleName("screenPanel");
        VerticalPanel[] columns = new VerticalPanel[columnCount];

        for (int i = 0; i < columnCount; i++) {
            columns[i] = createNewColumn();
            int restNumbers = numberCount - (i * maxButtonsInColumn);
            numberCountInColumn = Math.min(restNumbers, maxButtonsInColumn);
            for (int j = 0; j < numberCountInColumn; j++) {
                buttonIndex = i * maxButtonsInColumn + j;
                Button button = new Button();
                button.setStyleName("button");
                button.addStyleName("numberButton");
                button.getElement().setId("button_" + buttonIndex);
                button.setText(String.valueOf(originalNumbers[buttonIndex]));
                columns[i].add(button);
            }
            horizontalPanel.add(columns[i]);
        }
        VerticalPanel emptyColumn = createNewColumn();
        emptyColumn.setWidth("100px");
        horizontalPanel.add(emptyColumn);
        VerticalPanel functionalColumn = createFunctionalPanel();
        horizontalPanel.add(functionalColumn);
        return horizontalPanel;
    }

    private VerticalPanel createFunctionalPanel() {
        VerticalPanel functionalPanel = createNewColumn();

        Button sortButton = new Button("Sort");
        Button resetButton = new Button("Reset");
        Label speedLabel = new Label("Enter speed show sort [1,30] int (default 0.5 s)");
        TextBox speedInput = new TextBox();

        sortButton.setStyleName("button");
        sortButton.addStyleName("functionalButton");
        resetButton.setStyleName("button");
        resetButton.addStyleName("functionalButton");
        speedLabel.setStyleName("speedLabel");
        speedInput.setStyleName("speedField");
        speedInput.setText("1");

        class ResetButtonHandler implements ClickHandler {
            public void onClick(ClickEvent event) {
                sortedNumbers = null;
                sortScreenPanel.clear();
                introScreenPanel.setVisible(true);
            }
        }
        resetButton.addClickHandler(new ResetButtonHandler());

        class SortButtonHandler implements ClickHandler, KeyUpHandler {
            public void onClick(ClickEvent event) {
                sortNumbers();
            }
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    sortNumbers();
                }
            }
            private void sortNumbers() {
                if(sortedNumbers == null){
                    sortedNumbers = Arrays.copyOf(originalNumbers, originalNumbers.length);
                }
                isIncreasingOrder = !isIncreasingOrder;
                quickSort(sortedNumbers, 0, sortedNumbers.length - 1);
                for (int i = 0; i < sortedNumbers.length; i++) {
                    Button button = (Button) getWidget(DOM.getElementById("button_" + i));
                    button.setText(String.valueOf(sortedNumbers[i]));
                }
            }
        }
        sortButton.addClickHandler(new SortButtonHandler());

        functionalPanel.add(sortButton);
        functionalPanel.add(resetButton);
        functionalPanel.add(speedLabel);
        functionalPanel.add(speedInput);

        return functionalPanel;
    }

    private VerticalPanel createNewColumn() {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setStyleName("column");
        return verticalPanel;
    }

    private static IsWidget getWidget(com.google.gwt.dom.client.Element element) {
        EventListener listener = DOM.getEventListener(element);
        if (listener == null) {
            throw new IllegalArgumentException();
        }
        if (listener instanceof Widget) {
            return (Widget) listener;
        }
        throw new IllegalArgumentException();
    }

    private int[] generateNumbers(int numberCount) {
        int minValue = 1;
        int minLimit = 30;
        int maxValue = 1000;
        int[] result = new int[numberCount];
        boolean isAllValuesOverThanLimit = true;
        Random random = new Random();
        do {
            for (int i = 0; i < numberCount; i++) {
                result[i] = random.nextInt(maxValue - minValue) + minValue;
                isAllValuesOverThanLimit = isAllValuesOverThanLimit && result[i] > minLimit;
            }
        }
        while (isAllValuesOverThanLimit);
        return result;
    }

    private void quickSort(int[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);
            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private int partition(int[] arr, int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);
        for (int j = begin; j < end; j++) {
            if (getSortOrder(arr[j], pivot)) {
                i++;
                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }
        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;
        return i + 1;
    }

    private boolean getSortOrder(int a, int b) {
        return isIncreasingOrder ? a <= b : a >= b;
    }

}