package xaero.common.server.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import net.minecraft.class_2540;
import xaero.common.message.MinimapMessage;

public class LevelMapProperties extends MinimapMessage<LevelMapProperties> {
   private int id = new Random().nextInt();

   public void write(PrintWriter writer) {
      writer.print("id:" + this.id);
   }

   public void read(BufferedReader reader) throws IOException {
      String line;
      while ((line = reader.readLine()) != null) {
         String[] args = line.split(":");
         if (args[0].equals("id")) {
            try {
               this.id = Integer.parseInt(args[1]);
            } catch (NumberFormatException var5) {
            }
         }
      }
   }

   public int getId() {
      return this.id;
   }

   public static LevelMapProperties read(class_2540 input) {
      LevelMapProperties result = new LevelMapProperties();
      result.id = input.readInt();
      return result;
   }

   public void write(class_2540 u) {
      u.writeInt(this.id);
   }
}
