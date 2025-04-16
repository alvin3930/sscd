import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MacroExpander {
   private static Map<String, Integer> mntTable = new HashMap();
   private static List<String> mdtTable = new ArrayList();
   private static Map<Integer, String> alaTable = new HashMap();

   public MacroExpander() {
   }

   public static void main(String[] var0) throws IOException {
      loadMNT("/home/bitsync/College/SSCD/Assignment-4/MNT.txt");
      loadMDT("/home/bitsync/College/SSCD/Assignment-4/MDT.txt");
      loadALA("/home/bitsync/College/SSCD/Assignment-4/ALA.txt");
      expandMacros("/home/bitsync/College/SSCD/Assignment-4/alp_program.asm");
   }

   private static void loadMNT(String var0) throws IOException {
      BufferedReader var1 = new BufferedReader(new FileReader(var0));

      String var2;
      try {
         while((var2 = var1.readLine()) != null) {
            String[] var3 = var2.trim().split("\\s+");
            if (var3.length == 3 && var3[0].matches("\\d+")) {
               mntTable.put(var3[1], Integer.parseInt(var3[2]));
            }
         }
      } catch (Throwable var5) {
         try {
            var1.close();
         } catch (Throwable var4) {
            var5.addSuppressed(var4);
         }

         throw var5;
      }

      var1.close();
   }

   private static void loadMDT(String var0) throws IOException {
      BufferedReader var1 = new BufferedReader(new FileReader(var0));

      String var2;
      try {
         while((var2 = var1.readLine()) != null) {
            mdtTable.add(var2);
         }
      } catch (Throwable var5) {
         try {
            var1.close();
         } catch (Throwable var4) {
            var5.addSuppressed(var4);
         }

         throw var5;
      }

      var1.close();
   }

   private static void loadALA(String var0) throws IOException {
      BufferedReader var1 = new BufferedReader(new FileReader(var0));

      String var2;
      try {
         while((var2 = var1.readLine()) != null) {
            String[] var3 = var2.trim().split("\\s+");
            if (var3.length == 3 && var3[0].matches("#\\d+")) {
               alaTable.put(Integer.parseInt(var3[0].substring(1)), var3[2]);
            }
         }
      } catch (Throwable var5) {
         try {
            var1.close();
         } catch (Throwable var4) {
            var5.addSuppressed(var4);
         }

         throw var5;
      }

      var1.close();
   }

   private static void expandMacros(String var0) throws IOException {
      BufferedReader var1 = new BufferedReader(new FileReader(var0));

      String var2;
      try {
         while((var2 = var1.readLine()) != null) {
            String[] var3 = var2.trim().split("\\s+");
            if (var3.length > 0 && mntTable.containsKey(var3[0])) {
               int var4 = (Integer)mntTable.get(var3[0]);
               expandMDT(var4, var3);
            } else {
               System.out.println(var2);
            }
         }
      } catch (Throwable var6) {
         try {
            var1.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var1.close();
   }

   private static void expandMDT(int var0, String[] var1) {
      for(int var2 = var0; var2 < mdtTable.size(); ++var2) {
         String var3 = ((String)mdtTable.get(var2)).trim();
         if (var3.equals("MEND")) {
            break;
         }

         for(int var4 = 1; var4 < var1.length; ++var4) {
            var3 = var3.replace("#" + var4, var1[var4]);
         }

         System.out.println(var3);
      }

   }
}
