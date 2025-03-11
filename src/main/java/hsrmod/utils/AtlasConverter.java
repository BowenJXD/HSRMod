package hsrmod.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AtlasConverter {
    public static void main(String[] args) {
        String inputFile = "C:\\Games\\SlayTheSpireMod\\HSRMod\\src\\main\\resources\\HSRModResources\\img\\spine\\TheHerta.atlas";
        String outputFile = "C:\\Games\\SlayTheSpireMod\\HSRMod\\src\\main\\resources\\HSRModResources\\img\\spine\\TheHerta3.4.atlas";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            String line;
            List<String> sectionLines = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                line = reader.readLine();
                writer.println(line);
            }
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Blank line indicates end of a section.
                if (!line.contains(":")) {
                    if (!sectionLines.isEmpty()) {
                        convertSection(sectionLines, writer);
                        // writer.println(); // Separate sections with a blank line.
                        sectionLines.clear();
                    }
                }
                sectionLines.add(line);
            }
            // Convert the last section if file does not end with a blank line.
            if (!sectionLines.isEmpty()) {
                convertSection(sectionLines, writer);
            }

            System.out.println("Conversion complete. Output written to " + outputFile);
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void convertSection(List<String> sectionLines, PrintWriter writer) {
        // A valid section must have at least a region name and bounds line.
        if (sectionLines.size() < 2) {
            System.err.println("Skipping invalid section: " + sectionLines);
            return;
        }

        // First line is the region name.
        String regionName = sectionLines.get(0);

        // Second line should be the bounds line: "bounds:x,y,width,height"
        String boundsLine = sectionLines.get(1);
        if (!boundsLine.startsWith("bounds:")) {
            System.err.println("Invalid bounds line in section: " + regionName);
            return;
        }

        int[] bounds = parseNumbers(boundsLine.substring(boundsLine.indexOf(":") + 1));
        if (bounds == null || bounds.length != 4) {
            System.err.println("Invalid bounds numbers in section: " + regionName);
            return;
        }
        int x = bounds[0];
        int y = bounds[1];
        int width = bounds[2];
        int height = bounds[3];

        // Set default values for offsets and rotation.
        int offsetX = 0;
        int offsetY = 0;
        int origWidth = width;
        int origHeight = height;
        boolean rotated = false;

        // Process additional lines, if any.
        for (int i = 2; i < sectionLines.size(); i++) {
            String currentLine = sectionLines.get(i);
            if (currentLine.startsWith("offsets:")) {
                int[] offsets = parseNumbers(currentLine.substring(currentLine.indexOf(":") + 1));
                if (offsets != null && offsets.length == 4) {
                    offsetX = offsets[0];
                    offsetY = offsets[1];
                    origWidth = offsets[2];
                    origHeight = offsets[3];
                } else {
                    System.err.println("Invalid offsets in section: " + regionName + ". Using defaults.");
                }
            } else if (currentLine.startsWith("rotate:")) {
                String rotateValue = currentLine.substring(currentLine.indexOf(":") + 1).trim();
                // Parse the rotation angle.
                try {
                    int angle = Integer.parseInt(rotateValue);
                    // For the older format, set rotate flag to true if angle is 90 or 270, false otherwise.
                    rotated = (angle == 90 || angle == 270);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid rotation value in section: " + regionName + ". Defaulting rotate to false.");
                    rotated = false;
                }
            } else {
                System.err.println("Unrecognized line in section " + regionName + ": " + currentLine);
            }
        }

        // Write out the section in the older format.
        writer.println(regionName);
        writer.println("  rotate: " + rotated);
        writer.println("  xy: " + x + ", " + y);
        writer.println("  size: " + width + ", " + height);
        writer.println("  orig: " + origWidth + ", " + origHeight);
        writer.println("  offset: " + offsetX + ", " + offsetY);
        writer.println("  index: -1");
    }

    /**
     * Parses a comma-separated list of integers from a string.
     */
    private static int[] parseNumbers(String s) {
        String[] parts = s.split(",");
        int[] nums = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                nums[i] = Integer.parseInt(parts[i].trim());
            }
            return nums;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
