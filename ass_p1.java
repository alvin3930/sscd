
import java.io.*;
import java.util.*;

public class ass_p1 {

    static String[][] optab = {
        {"START", "AD", "01"}, {"END", "AD", "02"}, {"MOVER", "IS", "01"},
        {"MOVEM", "IS", "02"}, {"ADD", "IS", "03"}, {"SUB", "IS", "04"},
        {"MULT", "IS", "05"}, {"DIV", "IS", "06"}, {"DC", "DL", "01"}, {"DS", "DL", "02"}
    };
    static String[] regtab = {"AREG", "BREG", "CREG", "DREG"};
    static Map<String, Integer> symtab = new LinkedHashMap<>();
    static Map<String, Integer> symbolIdMap = new LinkedHashMap<>();
    static int lc = 0;
    static int symidCounter = 0;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("/home/bitsync/College/SSCD/practice/input.asm"))) {
            String line;
            String constant = "";
            System.out.println("LC OPCODE OP1 OP2");
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 0) {
                    continue;
                }
                
                String label = "", inst = "", op1 = "", op2 = "";
                
                if (isOpcode(parts[0])) {
                    inst = parts[0];
                    if (parts.length > 1) {
                        op1 = parts[1];
                    }
                    if (parts.length > 2) {
                        op2 = parts[2];
                    }
                } else {
                    label = parts[0];
                    inst = parts[1];
                    if (!symtab.containsKey(label)) {
                        PutToSymtab(lc, label);
                    }
                    
                    if (parts.length > 2) {
                        op1 = parts[2];
                    }
                    if (parts.length > 3) {
                        op2 = parts[3];
                    }
                }
                
                if (inst.equals("DC") || inst.equals("DS")) {
                    constant = "(C," + op1 + ")";
                }
                
                System.out.printf("%s (%s) %s %s\n",
                        inst.equals("START") ? "-" : String.valueOf(lc),
                        getOpcode(inst),
                        inst.equals("START") ? "-" : getRegCode(op1),
                        inst.equals("START") ? "-" : (op2.isEmpty() && op1.isEmpty() ? "-" : inst.equals("DC") || inst.equals("DS") ? constant : (getSymbolOperand(op2)) ));
                
                switch (inst) {
                    case "START" ->
                        lc = Integer.parseInt(op1);
                    case "DS" ->
                        lc += Integer.parseInt(op1);
                    default ->
                        lc++;
                }

            }
        }

        System.out.println("\nSYMTAB:");
        int id = 1;
        for (var entry : symtab.entrySet()) {
            System.out.printf("%d\t%s\t%d\n", id++, entry.getKey(), entry.getValue());
        }
    }

    static boolean isOpcode(String s) {
        for (String[] o : optab) {
            if (o[0].equals(s)) {
                return true;
            }
        }
        return false;
    }

    static String getRegCode(String s) {
        for (String r : regtab) {
            if (r.equals(s)) {
                return "0" + (Arrays.asList(regtab).indexOf(r) + 1);
            }
        }
        return "-";
    }

    static String getOpcode(String s) {
        for (String[] o : optab) {
            if (o[0].equals(s)) {
                return o[1] + "," + o[2];
            }
        }
        return "NA";
    }

    static void PutToSymtab(int lc, String label) {
        symtab.put(label, lc);
        symbolIdMap.put(label, ++symidCounter);
    }

    static String getSymbolOperand(String sym) {
        if (!symbolIdMap.containsKey(sym)) {
            PutToSymtab(lc, sym);
        }
        return "(S," + symbolIdMap.get(sym) + ")";
    }

}
