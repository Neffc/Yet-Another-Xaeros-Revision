package xaero.common.patreon.decrypt;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Date;
import javax.crypto.Cipher;

public class DecryptInputStream extends InputStream {
   private InputStream src;
   private Cipher cipher;
   private byte[] encryptedBuffer = new byte[256];
   private byte[] currentBlock;
   private int blockCount;
   private int blockOffset;
   private boolean endReached;
   private long prevExpirationTime = -1L;

   public DecryptInputStream(InputStream src, Cipher cipher) {
      this.src = src;
      this.cipher = cipher;
   }

   @Override
   public int read() throws IOException {
      if (this.endReached) {
         return -1;
      } else {
         if (this.currentBlock == null || this.currentBlock.length == this.blockOffset) {
            int offset = 0;

            while (offset < 256) {
               int read = this.src.read(this.encryptedBuffer, offset, 256 - offset);
               if (read == -1) {
                  this.endReached = true;
                  if (offset == 0) {
                     throw new RuntimeException("Online mod data missing confirmation block!");
                  }

                  throw new RuntimeException("Encrypted block too short!");
               }

               offset += read;
            }

            try {
               this.currentBlock = this.cipher.doFinal(this.encryptedBuffer);
               long expirationTime = 0L;
               int blockIndex = 0;

               for (this.blockOffset = 0; this.blockOffset < 8; this.blockOffset++) {
                  expirationTime |= (long)(this.currentBlock[this.blockOffset] & 255) << 8 * this.blockOffset;
               }

               for (int i = 0; i < 2; i++) {
                  blockIndex |= (this.currentBlock[this.blockOffset] & 255) << 8 * i;
                  this.blockOffset++;
               }

               if (System.currentTimeMillis() > expirationTime) {
                  this.endReached = true;
                  throw new RuntimeException("Online mod data expired! Date: " + new Date(expirationTime));
               }

               if (this.prevExpirationTime != -1L && expirationTime != this.prevExpirationTime) {
                  this.endReached = true;
                  throw new RuntimeException(
                     "Online mod data expiration date mismatch! Dates: " + new Date(expirationTime) + " VS " + new Date(this.prevExpirationTime)
                  );
               }

               if (blockIndex != this.blockCount) {
                  this.endReached = true;
                  throw new RuntimeException("Online mod data block index mismatch! " + blockIndex + " VS " + this.blockCount);
               }

               this.prevExpirationTime = expirationTime;
               this.blockCount++;
               if (this.blockOffset == this.currentBlock.length) {
                  this.endReached = true;
                  return -1;
               }
            } catch (GeneralSecurityException var6) {
               throw new RuntimeException(var6);
            }
         }

         return this.currentBlock[this.blockOffset++];
      }
   }

   @Override
   public void close() throws IOException {
      super.close();
      this.src.close();
      this.encryptedBuffer = null;
      this.currentBlock = null;
   }
}
