import java.io.*;
import java.util.*;

public class alp_pass1_simple {
    private static final String[][] Optab = {
        {"START", "AD", "01"},
        {"END", "AD", "02"},
        {"MOVER", "IS", "01"},
        {"MOVEM", "IS", "02"},
        {"ADD", "IS", "03"},
        {"SUB", "IS", "04"},
        {"MULT", "IS", "05"},
        {"DIV", "IS", "06"},
        {"DC", "DL", "01"},
        {"DS", "DL", "02"}
    };

    private static final String[] Regtab = {"AREG", "BREG", "CREG", "DREG"};
    private static final List<String> symtabLabels = new ArrayList<>();
    private static final List<Integer> symtabAddresses = new ArrayList<>();
    private static int symtabCount = 0;

    private static class IntermediateCode {
        int locationCounter;
        String opcode;
        String operand1;
        String operand2;

        public IntermediateCode(int lc, String opcode, String operand1, String operand2) {
            this.locationCounter = lc;
            this.opcode = opcode;
            this.operand1 = operand1;
            this.operand2 = operand2;
        }

        @Override
        public String toString() {
            return String.format("%-15d%-20s%-20s%-20s", locationCounter, opcode, operand1, operand2);
        }
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("/home/bitsync/College/SSCD/Assignment-1/alp_program2.asm"))) {
            String line;
            int locationCounter = 0;
            List<IntermediateCode> intermediateCodes = new ArrayList<>();

            // Reading and parsing the file
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Skip empty lines

                String[] tokens = line.split("\\s+");
                String label = "-", instruction = "-", operand1 = "-", operand2 = "-";

                if (tokens.length >= 1) {
                    instruction = tokens[0];
                    final String instruction_ = instruction;

                    if (Arrays.stream(Optab).anyMatch(optab -> optab[0].equals(instruction_))) {
                        if (instruction.equals("START")) {
                            locationCounter = (tokens.length > 1) ? Integer.parseInt(tokens[1]) : 0;
                        } else if (instruction.equals("END")) {
                            operand1 = "-";
                            operand2 = "-";
                        } else {
                            operand1 = (tokens.length > 1) ? tokens[1] : "-";
                            operand2 = (tokens.length > 2) ? getOperandCode(tokens[2], locationCounter) : "-";
                        }

                        String opcode = Arrays.stream(Optab)
                                               .filter(optab -> optab[0].equals(instruction_))
                                               .findFirst().get()[1] + "," +
                                        Arrays.stream(Optab)
                                               .filter(optab -> optab[0].equals(instruction_))
                                               .findFirst().get()[2];

                        intermediateCodes.add(new IntermediateCode(locationCounter, "(" + opcode + ")", operand1, operand2));

                    } else {
                        label = tokens[0];
                        if (!symtabLabels.contains(label)) {
                            symtabLabels.add(label);
                            symtabAddresses.add(locationCounter);
                            symtabCount++;
                        }
                    }

                    // Increment location counter if not DS/START/END
                    if (!instruction.equals("DS") && !instruction.equals("START") && !instruction.equals("END")) {
                        locationCounter++;
                    } else if (instruction.equals("DS")) {
                        locationCounter += Integer.parseInt(operand2.replace("(C,", "").replace(")", ""));
                    }
                }
            }

            // Output Intermediate Code
            System.out.println("\nIntermediate Code:");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("%-15s%-20s%-20s%-20s\n", "LC", "Opcode", "Operand 1", "Operand 2");
            System.out.println("-------------------------------------------------------------------------------");
            for (IntermediateCode code : intermediateCodes) {
                System.out.println(code);
            }
            System.out.println("-------------------------------------------------------------------------------");

            // Output SYMTAB
            System.out.println("\nSYMTAB:");
            System.out.println("-------------------------------------------------");
            System.out.printf("%-15s%-15s%-15s\n", "ID", "Symbol Name", "Address");
            System.out.println("-------------------------------------------------");
            for (int i = 0; i < symtabCount; i++) {
                System.out.printf("%-15d%-15s%-15d\n", i + 1, symtabLabels.get(i), symtabAddresses.get(i));
            }
            System.out.println("-------------------------------------------------");

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static String getOperandCode(String operand, int locationCounter) {
        if (Arrays.asList(Regtab).contains(operand)) {
            return "(R," + (Arrays.asList(Regtab).indexOf(operand) + 1) + ")";
        } else if (operand.matches("\\d+")) {
            symtabLabels.add(operand);
            symtabAddresses.add(locationCounter);
            symtabCount++;
            return "(C," + operand + ")";
        } else {
            for (int i = 0; i < symtabCount; i++) {
                if (symtabLabels.get(i).equals(operand)) {
                    return "(S," + (i + 1) + ")";
                }
            }
            return "(S," + symtabCount + ")";
        }
    }
}
