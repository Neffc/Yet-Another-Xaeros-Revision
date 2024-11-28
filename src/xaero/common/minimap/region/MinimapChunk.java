package xaero.common.minimap.region;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import xaero.common.graphics.PixelBuffers;
import xaero.common.minimap.MinimapInterface;

public class MinimapChunk {
   public static final int SIZE_TILES = 4;
   public static final int INT_BUFFER_SIZE = 4096;
   public static final int LIGHT_LEVELS = 5;
   private boolean blockTextureUpload;
   private int X;
   private int Z;
   private boolean hasSomething;
   private MinimapTile[][] tiles;
   private int[] glTexture;
   private boolean[] refreshRequired;
   private boolean refreshed;
   private IntBuffer[] buffer;
   private boolean changed;
   private int levelsBuffered = 0;
   private int workaroundPBO;

   public MinimapChunk(int X, int Z) {
      this.X = X;
      this.Z = Z;
      this.tiles = new MinimapTile[4][4];
      this.glTexture = new int[5];
      this.refreshRequired = new boolean[5];
      this.buffer = new IntBuffer[5];
   }

   public void reset(int X, int Z) {
      this.X = X;
      this.Z = Z;
      this.hasSomething = false;

      for (int i = 0; i < this.glTexture.length; i++) {
         this.glTexture[i] = 0;
         this.refreshRequired[i] = false;
         if (this.buffer[i] != null) {
            this.buffer[i].clear();
         }
      }

      this.refreshed = false;
      this.changed = false;
      this.levelsBuffered = 0;

      for (int ix = 0; ix < this.tiles.length; ix++) {
         for (int j = 0; j < this.tiles.length; j++) {
            this.tiles[ix][j] = null;
         }
      }

      this.blockTextureUpload = false;
   }

   public void recycleTiles() {
      for (int i = 0; i < this.tiles.length; i++) {
         for (int j = 0; j < this.tiles.length; j++) {
            MinimapTile tile = this.tiles[i][j];
            if (tile != null) {
               if (!tile.isWasTransfered()) {
                  tile.recycle();
               } else {
                  tile.setWasTransfered(false);
               }
            }
         }
      }
   }

   public int getLevelToRefresh(int currentLevel) {
      if (!this.refreshed && this.levelsBuffered != 0 && currentLevel != -1) {
         int prev = currentLevel - 1;
         if (prev < 0) {
            prev = this.levelsBuffered - 1;
         }

         int i;
         for (i = currentLevel; !this.refreshRequired[i]; i = (i + 1) % this.levelsBuffered) {
            if (i == prev) {
               this.refreshed = true;
               return -1;
            }
         }

         return i;
      } else {
         return -1;
      }
   }

   public int bindTexture(int level) {
      synchronized (this) {
         if (!this.hasSomething) {
            RenderSystem.bindTexture(0);
            return 0;
         } else {
            if (!this.blockTextureUpload) {
               int levelToRefresh = this.getLevelToRefresh(Math.min(level, this.levelsBuffered - 1));
               if (levelToRefresh != -1) {
                  boolean result = false;
                  if (this.glTexture[levelToRefresh] == 0) {
                     this.glTexture[levelToRefresh] = GL11.glGenTextures();
                     result = true;
                  }

                  GlStateManager._bindTexture(this.glTexture[levelToRefresh]);
                  if (result) {
                     GL11.glTexParameteri(3553, 33085, 0);
                     GL11.glTexParameterf(3553, 33082, 0.0F);
                     GL11.glTexParameterf(3553, 33083, 0.0F);
                     GL11.glTexParameterf(3553, 34049, 0.0F);
                     GL11.glTexParameteri(3553, 10240, 9728);
                     GL11.glTexParameteri(3553, 10242, 33071);
                     GL11.glTexParameteri(3553, 10243, 33071);
                     GL11.glTexImage2D(3553, 0, 32856, 64, 64, 0, 32993, 32821, (ByteBuffer)null);
                  }

                  if (this.workaroundPBO == 0) {
                     this.workaroundPBO = PixelBuffers.glGenBuffers();
                  }

                  PixelBuffers.glBindBuffer(35052, this.workaroundPBO);
                  PixelBuffers.glBufferData(35052, 16384L, 35040);
                  IntBuffer mappedPBO = PixelBuffers.glMapBuffer(35052, 35001, 16384L, null).asIntBuffer();
                  mappedPBO.put(this.buffer[levelToRefresh]);
                  PixelBuffers.glUnmapBuffer(35052);
                  GL11.glTexSubImage2D(3553, 0, 0, 0, 64, 64, 32993, 32821, 0L);
                  PixelBuffers.glBindBuffer(35052, 0);
                  this.refreshRequired[levelToRefresh] = false;
               }
            }

            int levelTexture = this.glTexture[level];
            if (levelTexture != 0) {
               GlStateManager._bindTexture(levelTexture);
               RenderSystem.setShaderTexture(0, levelTexture);
            }

            return levelTexture;
         }
      }
   }

   public void updateBuffers(int levelsToLoad, int[][] intArrayBuffer) {
      this.refreshed = true;

      for (int l = 0; l < levelsToLoad; l++) {
         this.refreshRequired[l] = false;
         if (this.buffer[l] == null) {
            this.buffer[l] = IntBuffer.allocate(4096);
         }
      }

      for (int o = 0; o < this.tiles.length; o++) {
         int offX = o * 16;

         for (int p = 0; p < this.tiles.length; p++) {
            MinimapTile tile = this.tiles[o][p];
            int offZ = p * 16;

            for (int z = 0; z < 16; z++) {
               for (int x = 0; x < 16; x++) {
                  for (int i = 0; i < levelsToLoad; i++) {
                     if (tile == null) {
                        this.putColour(offX + x, offZ + z, 0, 0, 0, intArrayBuffer[i], 64);
                     } else {
                        this.putColour(offX + x, offZ + z, tile.getRed(i, x, z), tile.getGreen(i, x, z), tile.getBlue(i, x, z), intArrayBuffer[i], 64);
                     }
                  }
               }
            }
         }
      }

      for (int ix = 0; ix < levelsToLoad; ix++) {
         synchronized (this) {
            this.blockTextureUpload = true;
         }

         this.buffer[ix].clear();
         this.buffer[ix].put(intArrayBuffer[ix]);
         this.buffer[ix].flip();
         this.refreshRequired[ix] = true;
         synchronized (this) {
            this.blockTextureUpload = false;
         }
      }

      this.refreshed = false;
   }

   public void putColour(int x, int y, int red, int green, int blue, int[] texture, int size) {
      int pos = y * size + x;
      texture[pos] = blue << 24 | green << 16 | red << 8 | 0xFF;
   }

   public void copyBuffer(int level, IntBuffer toCopy) {
      if (this.buffer[level] == null) {
         this.buffer[level] = IntBuffer.allocate(4096);
      } else {
         this.buffer[level].clear();
      }

      this.buffer[level].put(toCopy);
      this.buffer[level].flip();
   }

   public int getLevelsBuffered() {
      return this.levelsBuffered;
   }

   public boolean isHasSomething() {
      return this.hasSomething;
   }

   public void setHasSomething(boolean hasSomething) {
      this.hasSomething = hasSomething;
   }

   public int getX() {
      return this.X;
   }

   public int getZ() {
      return this.Z;
   }

   public int getGlTexture(int l) {
      return this.glTexture[l];
   }

   public void setGlTexture(int l, int t) {
      this.glTexture[l] = t;
   }

   public MinimapTile getTile(int x, int z) {
      return this.tiles[x][z];
   }

   public void setTile(int x, int z, MinimapTile t) {
      this.tiles[x][z] = t;
   }

   public boolean isChanged() {
      return this.changed;
   }

   public void setChanged(boolean changed) {
      this.changed = changed;
   }

   public void setLevelsBuffered(int levelsBuffered) {
      this.levelsBuffered = levelsBuffered;
   }

   public boolean isBlockTextureUpload() {
      return this.blockTextureUpload;
   }

   public void setBlockTextureUpload(boolean blockTextureUpload) {
      this.blockTextureUpload = blockTextureUpload;
   }

   public boolean isRefreshRequired(int l) {
      return this.refreshRequired[l];
   }

   public void setRefreshRequired(int l, boolean r) {
      this.refreshRequired[l] = r;
   }

   public IntBuffer getBuffer(int l) {
      return this.buffer[l];
   }

   public void cleanup(MinimapInterface minimapInterface) {
      for (int l = 0; l < this.glTexture.length; l++) {
         if (this.glTexture[l] != 0) {
            GL11.glDeleteTextures(this.glTexture[l]);
         }

         if (this.workaroundPBO != 0) {
            PixelBuffers.glDeleteBuffers(this.workaroundPBO);
         }
      }
   }
}
