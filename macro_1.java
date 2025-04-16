import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class macro_1 {
    public static void main(String[] args) {
        BufferedReader reader;

        ArrayList<String[]> alaList = new ArrayList<>();
        ArrayList<String[]> mntList = new ArrayList<>();
        HashMap<String, ArrayList<String>> mdtMap = new HashMap<>();

        try {
            String filePath = "/home/bitsync/College/SSCD/Assignment-3/alp_program.asm";
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            boolean macroStarted = false;
            int mntIndex = 1;
            String macroName = "";
            String[] formalArguments = null;
            ArrayList<String> mdtListForMacro = new ArrayList<>();

            while (line != null) {
                String[] tokens = line.trim().split("\\s+");

                if (tokens.length == 0) {
                    line = reader.readLine();
                    continue;
                }

                for (String token : tokens) {
                    if (macroStarted) {
                        if (macroName.isEmpty()) {
                            macroName = tokens[0];
                            if (tokens.length > 1) {
                                formalArguments = new String[tokens.length - 1];
                                System.arraycopy(tokens, 1, formalArguments, 0, tokens.length - 1);
                            }
                            // Add the macro definition line to the MDT directly (first line)
                            // Process formal arguments and replace them in the macro definition line
                            String macroDefLine = "M1";
                            for (String arg : formalArguments) {
                                macroDefLine += " " + arg;
                            }
                            mdtListForMacro.add(macroDefLine);
                        } else {
                            // Add the formal arguments to the ALA
                            if (formalArguments != null) {
                                for (int i = 0; i < formalArguments.length; i++) {
                                    String[] alaEntry = {"#" + (i + 1), formalArguments[i].replace(",", "")};
                                    alaList.add(alaEntry);
                                }
                            }

                            // Add macro to MNT
                            String[] mntEntry = {String.valueOf(mntIndex), macroName, String.valueOf(mntIndex)};
                            mntList.add(mntEntry);
                            mntIndex++;

                            // Process each line in the macro body and substitute arguments with ALA index
                            while ((line = reader.readLine()) != null) {
                                tokens = line.trim().split("\\s+");
                                if (tokens.length == 0) continue;

                                String instruction = String.join(" ", tokens);
                                if (instruction.toLowerCase().equals("mend")) {
                                    mdtListForMacro.add("MEND");
                                    break;
                                } else {
                                    // Replace formal arguments in the body with ALA index
                                    for (int i = 0; i < formalArguments.length; i++) {
                                        instruction = instruction.replace("&" + formalArguments[i], "#" + (i + 1));
                                    }
                                    mdtListForMacro.add(instruction);
                                }
                            }

                            mdtMap.put(macroName, new ArrayList<>(mdtListForMacro));
                            macroStarted = false;
                            macroName = "";
                            formalArguments = null;
                            mdtListForMacro.clear();
                        }
                        break;
                    }

                    if (token.toLowerCase().equals("macro")) {
                        macroStarted = true;
                        break;
                    }
                }
                line = reader.readLine();
            }

            // Print ALA
            System.out.println("\n-------------------------------------------------");
            System.out.println("Argument List Array:");
            System.out.println("-------------------------------------------------");
            System.out.println("Index\tFormal Arg.\tActual Arg.");
            for (String[] ala : alaList) {
                System.out.println(ala[0] + "\t" + ala[1]);
            }
            System.out.println("-------------------------------------------------");

            // Print MNT
            System.out.println("\n-------------------------------------------------");
            System.out.println("Macro Name Table:");
            System.out.println("-------------------------------------------------");
            System.out.println("Index\tName\tMDT Index");
            System.out.println("-------------------------------------------------");
            for (String[] mnt : mntList) {
                System.out.println(mnt[0] + "\t" + mnt[1] + "\t" + mnt[2]);
            }
            System.out.println("-------------------------------------------------");

            // Print MDT
            System.out.println("\n-------------------------------------------------");
            System.out.println("Macro Definition Table:");
            System.out.println("-------------------------------------------------");
            System.out.println("Index\tInstruction");
            System.out.println("-------------------------------------------------");
            int mdtIndex = 1;
            for (String macro : mdtMap.keySet()) {
                ArrayList<String> mdtInstructions = mdtMap.get(macro);
                for (String instruction : mdtInstructions) {
                    System.out.println(mdtIndex++ + "\t" + instruction);
                }
                System.out.println("-------------------------------------------------");
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
