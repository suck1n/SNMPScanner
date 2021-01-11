package it.duck.gui.utility;

import it.duck.gui.GUI;
import it.duck.utility.ThreadHandler;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TimerTask;

public class Logger {

    private final Queue<String> queue;
    private final int MAX_LINES = 255;

    public Logger() {
        queue = new PriorityQueue<>();

        ThreadHandler.registerNewTimer(new TimerTask() {
            @Override
            public void run() {
                printMessages();
            }
        }, 0, 500);
    }


    private void printMessages() {
        StringBuilder message = new StringBuilder();

        synchronized (queue) {
            int length = 0;
            while (!queue.isEmpty() && length != MAX_LINES) {
                message.append(queue.poll());
                length++;
            }
        }

        if(message.length() > 0) {
            Platform.runLater(() -> getTextArea().appendText(message.toString()));
        }
    }

    private TextArea getTextArea() {
        TextArea console = GUI.getInstance().getConsole();
        String[] lines = console.getText().split("\n");

        if(lines.length > MAX_LINES) {
            StringBuilder builder = new StringBuilder();

            for(int i = MAX_LINES - 1; i != 0; i--) {
                builder.append(lines[lines.length - i]).append("\n");
            }

            console.setText(builder.toString());
            console.positionCaret(builder.length());
        }

        return console;
    }

    private void log(String message) {
        synchronized (queue) {
            queue.add(message + "\n");
        }
    }

    public void info(String message) {
        log("[INFO] " + message);
    }

    public void warning(String message) {
        log("[WARNUNG] " + message);
    }

    public void error(String message) {
        log("[FEHLER] " + message);
    }
}
