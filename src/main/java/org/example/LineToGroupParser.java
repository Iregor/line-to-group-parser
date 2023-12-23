package org.example;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class LineToGroupParser {
    public static void main(String[] args) throws IOException {
        //O(min(m*n, nLogN)), n - number of lines, m - number of words in line
        System.out.println("Starting algorithm.");
        long start = System.currentTimeMillis();

        URL url = new URL("https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz");
        DsuProcessor dsuProcessor = new DsuProcessor();
        File file = new File(args[0]);
        GroupPrinter printer = new GroupPrinter(file);

        moveRemoveDataToProcessor(url, dsuProcessor);                               //O(m * n), n - number of lines, m - words in line
        //moveCSVDataToProcessor(new File("lng-big.csv"), dsuProcessor);            //used to obtain csv data from big .7z file
        List<List<String>> lineGroups = dsuProcessor.getSortedLineGroupsWithLeastSize(2);         //O(nLogN), n - number of groups
        printer.printGroups(lineGroups);                                            //O(n)

        long end = System.currentTimeMillis();
        System.out.printf("Number of groups: %d.%n", lineGroups.size());
        System.out.printf("File %s is prepared.%n", args[0]);
        System.out.println("Takes time: " + (end - start) / 1000 + " seconds.");
        System.out.println("Algorithm finished.");
    }

    private static void moveRemoveDataToProcessor(URL url, DsuProcessor processor) throws IOException {
        //O(m * n), n - number of lines, m - words in line
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new GZIPInputStream(
                                url.openStream()), StandardCharsets.UTF_8))) {
            br.lines()                                          //O(m * n), n - number of lines, m - words in line
                    .filter(LineValidator::validLineStart)      //or else split() produces wrong word array
                    .distinct()                                 //to retain unique lines
                    .map(str -> str.split(";"))
                    .filter(LineValidator::validWordArr)
                    .forEach(processor::add);
        }
    }

    private static void moveDiskDataToProcessor(File file, DsuProcessor processor) throws IOException {
        //O(m * n), n - number of lines, m - words in line
        try (BufferedReader br = new BufferedReader(
                new FileReader(file))) {
            br.lines()                                          //O(m * n), n - number of lines, m - words in line
                    .filter(LineValidator::validLineStart)      //or else split() produces wrong word array
                    .distinct()                                 //to retain unique lines
                    .map(str -> str.split(";"))
                    .filter(LineValidator::validWordArr)
                    .forEach(processor::add);
        }
    }
}