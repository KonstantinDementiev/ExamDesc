package com.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.shared.FieldVerifier;
import com.shared.NumberGenerator;

import java.util.Arrays;

import static com.client.WidgetExtractor.getWidget;

public class Gwt implements EntryPoint {

    private final static int DEFAULT_TIME_DELAY = 500;
    private final GwtServiceAsync gwtServiceAsync = GWT.create(GwtService.class);
    private VerticalPanel introScreenPanel;
    private HorizontalPanel sortScreenPanel;
    private DialogBox dialogBox;
    private int[] originalNumbers;
    private int[] sortedNumbers;
    private boolean isIncreasingOrder = true;
    private boolean isSortingGoOn = true;
    private Timer elapsedTimer;

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
                if (FieldVerifier.isCountInvalid(numberCount)) {
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

        originalNumbers = NumberGenerator.generateNumbers(numberCount);

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setStyleName("screenPanel");

        createNumberColumns(numberCount, horizontalPanel);

        VerticalPanel emptyColumn = createNewColumn();
        emptyColumn.setWidth("100px");
        horizontalPanel.add(emptyColumn);

        VerticalPanel functionalColumn = createFunctionalPanel();
        horizontalPanel.add(functionalColumn);
        return horizontalPanel;
    }

    private void createNumberColumns(int numberCount, HorizontalPanel hp){
        final int maxButtonsInColumn = 10;
        int columnCount = numberCount / 10 + 1;
        int numberCountInColumn;
        int buttonIndex;
        VerticalPanel[] columns = new VerticalPanel[columnCount];

        for (int i = 0; i < columnCount; i++) {
            columns[i] = createNewColumn();
            int restNumbers = numberCount - (i * maxButtonsInColumn);
            numberCountInColumn = Math.min(restNumbers, maxButtonsInColumn);
            for (int j = 0; j < numberCountInColumn; j++) {
                buttonIndex = i * maxButtonsInColumn + j;
                columns[i].add(createNumberButton(buttonIndex));
            }
            hp.add(columns[i]);
        }
    }

    private Button createNumberButton(int buttonIndex){
        int number = originalNumbers[buttonIndex];

        Button button = new Button();
        button.setStyleName("numberButtonSorted");
        button.getElement().setId("button_" + buttonIndex);
        button.setText(String.valueOf(number));

        class NumberButtonHandler implements ClickHandler {
            public void onClick(ClickEvent event) {
                if (Integer.parseInt(button.getText()) > 30) {
                    showMessageBox("Please select a value smaller or equal to 30");
                } else {
                    sortScreenPanel.clear();
                    sortScreenPanel = createSortScreen(number);
                    RootPanel.get("panelContainer").add(sortScreenPanel);
                }
            }
        }
        button.addClickHandler(new NumberButtonHandler());
        return button;
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

        class ResetButtonHandler implements ClickHandler {
            public void onClick(ClickEvent event) {
                clickResetButton();
            }
        }
        resetButton.addClickHandler(new ResetButtonHandler());

        class SortButtonHandler implements ClickHandler, KeyDownHandler {
            public void onClick(ClickEvent event) {
                sortNumbers();
            }
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    sortNumbers();
                }
            }
            private void sortNumbers() {
                clickSortButton(speedInput.getText());
            }
        }
        sortButton.addClickHandler(new SortButtonHandler());

        functionalPanel.add(sortButton);
        functionalPanel.add(resetButton);
        functionalPanel.add(speedLabel);
        functionalPanel.add(speedInput);

        return functionalPanel;
    }

    private void clickResetButton(){
        sortedNumbers = null;
        sortScreenPanel.clear();
        introScreenPanel.setVisible(true);
        if (elapsedTimer != null) elapsedTimer.cancel();
    }

    private void clickSortButton(String speedText){
        isSortingGoOn = true;
        if (elapsedTimer != null) elapsedTimer.cancel();
        int timeDelayInput = FieldVerifier.getThreadDelay(speedText);
        int timeDelay = timeDelayInput == DEFAULT_TIME_DELAY
                ? DEFAULT_TIME_DELAY : timeDelayInput * 1000;
        if (sortedNumbers == null) {
            sortedNumbers = Arrays.copyOf(originalNumbers, originalNumbers.length);
        }
        allButtonsSetSortedView("numberButtonUnsorted", false);
        isIncreasingOrder = !isIncreasingOrder;
        sendOriginalArrayToServer();
        elapsedTimer = new Timer() {
            public void run() {
                if (!isSortingGoOn) {
                    allButtonsSetSortedView("numberButtonSorted", true);
                    elapsedTimer.cancel();
                } else {
                    getCurrentArrayFromServer();
                }
            }
        };
        elapsedTimer.scheduleRepeating(timeDelay);
    }

    private void sendOriginalArrayToServer() {
        gwtServiceAsync.sendOriginalArray(isIncreasingOrder, sortedNumbers, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                showMessageBox("Sending array to server - Failure");
            }

            @Override
            public void onSuccess(String result) {
                changeButtonsView(new int[]{0});
            }
        });
    }

    private void getCurrentArrayFromServer() {
        gwtServiceAsync.getCurrentArray(new AsyncCallback<int[][]>() {
            @Override
            public void onFailure(Throwable throwable) {
                showMessageBox("Receiving array from server - Failure");
            }

            @Override
            public void onSuccess(int[][] result) {
                if (result[1][0] == 1) isSortingGoOn = false;
                sortedNumbers = result[0];
                changeButtonsView(result[1]);
            }
        });
    }

    private VerticalPanel createNewColumn() {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setStyleName("column");
        return verticalPanel;
    }

    private void showMessageBox(String message) {
        if (dialogBox == null) dialogBox = new DialogBox();
        dialogBox.setHTML(message);
        dialogBox.center();
        Button closeDialogButton = new Button("OK");
        dialogBox.add(closeDialogButton);
        dialogBox.show();
        closeDialogButton.setFocus(true);
        closeDialogButton.addClickHandler(clickEvent -> {
            dialogBox.clear();
            dialogBox.hide(true);
        });
    }

    private void changeButtonsView(int[] indexes) {
        for (int k = 0; k < sortedNumbers.length; k++) {
            Button button = (Button) getWidget(DOM.getElementById("button_" + k));
            button.setText(String.valueOf(sortedNumbers[k]));
            button.setStyleName("numberButtonUnsorted");
            for (int i = 1; i < indexes.length; i++) {
                if (k == indexes[i]) {
                    button.addStyleName("buttonIterated" + i);
                }
            }
        }
    }

    private void allButtonsSetSortedView(String styleName, boolean enableButtons) {
        for (int k = 0; k < originalNumbers.length; k++) {
            Button button = (Button) getWidget(DOM.getElementById("button_" + k));
            button.setStyleName(styleName);
            button.setEnabled(enableButtons);
        }
    }
}
