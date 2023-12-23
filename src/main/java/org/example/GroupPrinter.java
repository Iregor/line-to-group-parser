package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GroupPrinter {
    private File outputFile;

    public GroupPrinter(File outputFile) {
        this.outputFile = outputFile;
    }

    public void printGroups(List<List<String>> lineGroups) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write(String.format("Число групп с более чем одним элементом: %d%n", lineGroups.size()));
            for (int i = 0; i < lineGroups.size(); i++) {
                bw.write(String.format("Группа %d%n", i + 1));
                printGroup(lineGroups.get(i), bw);
            }
        }
    }

    public void printGroup(List<String> group, BufferedWriter bw) throws IOException {
        for (String line : group) {
            bw.write(line + "\n");
        }
    }
}
