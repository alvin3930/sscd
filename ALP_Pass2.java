
import java.io.*;
import java.util.*;

public class ALP_Pass2 {

    public static void main(String[] args) {
        List<String[]> intermediateCode = new ArrayList<>();
        Map<Integer, String> symbolTable = new HashMap<>();

        try {
            System.out.println("Using contents:");
            System.out.println("\nSymbol Table:");
            try (BufferedReader symReader = new BufferedReader(new FileReader("/home/bitsync/College/SSCD/Assignment-2/symbol_table.txt"))) {
                String line;
                while ((line = symReader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            System.out.println("\nIntermediate Code:");
            try (BufferedReader icReader = new BufferedReader(new FileReader("/home/bitsync/College/SSCD/Assignment-2/intermediate_code.txt"))) {
                String line;
                while ((line = icReader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            try (
                    var icReader = new BufferedReader(new FileReader("/home/bitsync/College/SSCD/Assignment-2/intermediate_code.txt")); var symReader = new BufferedReader(new FileReader("/home/bitsync/College/SSCD/Assignment-2/symbol_table.txt"))) {
                String line;
                while ((line = symReader.readLine()) != null) {
                    if (line.contains("ID") || line.contains("----")) {
                        continue;
                    }
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length == 3) {
                        int id = Integer.parseInt(parts[0]);
                        String address = parts[2];
                        symbolTable.put(id, address);
                    }
                }

                while ((line = icReader.readLine()) != null) {
                    if (line.contains("LC") || line.contains("----")) {
                        continue;
                    }
                    String[] tokens = line.trim().split("\\s+");
                    if (!tokens[1].contains("AD")) {
                        intermediateCode.add(tokens);
                    }
                }
            }

            System.out.println("\nMachine Language Program:");
            System.out.println("------------------------------------------------------------------");
            System.out.printf("%-15s %-15s %-15s %-15s\n", "LC", "Opcode", "Operand 1", "Operand 2");
            System.out.println("------------------------------------------------------------------");

            for (String[] ic : intermediateCode) {
                int lc = Integer.parseInt(ic[0]);
                String opcode = ic[1].replaceAll("[()IS,DL,]", "");
                String operand1 = ic[2];
                String operand2 = ic[3];

                if (operand2.startsWith("(S,")) {
                    int symbolId = Integer.parseInt(operand2.substring(3, operand2.length() - 1));
                    operand2 = symbolTable.getOrDefault(symbolId, "?");
                }
                if (operand2.startsWith("(C,")) {
                    int constant = Integer.parseInt(operand2.substring(3, operand2.length() - 1));
                    operand2 = String.format("%03d", constant);
                }

                System.out.printf("%-15d %-15s %-15s %-15s\n", lc, opcode, operand1.replace("-", ""), operand2.replace("-", ""));
            }
            System.out.println("------------------------------------------------------------------");
        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
        }
    }
}
