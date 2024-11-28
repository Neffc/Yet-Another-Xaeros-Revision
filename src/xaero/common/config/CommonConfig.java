package xaero.common.config;

public final class CommonConfig {
   public final boolean registerStatusEffects;
   public boolean allowCaveModeOnServer;
   public boolean allowNetherCaveModeOnServer;
   public boolean allowRadarOnServer;

   private CommonConfig(boolean registerStatusEffects) {
      this.registerStatusEffects = registerStatusEffects;
   }

   public static final class Builder {
      public boolean registerStatusEffects;
      public boolean allowCaveModeOnServer;
      public boolean allowNetherCaveModeOnServer;
      public boolean allowRadarOnServer;

      private Builder() {
      }

      public CommonConfig.Builder setDefault() {
         this.setRegisterStatusEffects(true);
         this.setAllowCaveModeOnServer(true);
         this.setAllowNetherCaveModeOnServer(true);
         this.setAllowRadarOnServer(true);
         return this;
      }

      public CommonConfig.Builder setRegisterStatusEffects(boolean registerStatusEffects) {
         this.registerStatusEffects = registerStatusEffects;
         return this;
      }

      public CommonConfig.Builder setAllowCaveModeOnServer(boolean allowCaveModeOnServer) {
         this.allowCaveModeOnServer = allowCaveModeOnServer;
         return this;
      }

      public CommonConfig.Builder setAllowNetherCaveModeOnServer(boolean allowNetherCaveModeOnServer) {
         this.allowNetherCaveModeOnServer = allowNetherCaveModeOnServer;
         return this;
      }

      public CommonConfig.Builder setAllowRadarOnServer(boolean allowRadarOnServer) {
         this.allowRadarOnServer = allowRadarOnServer;
         return this;
      }

      public CommonConfig build() {
         CommonConfig result = new CommonConfig(this.registerStatusEffects);
         result.allowCaveModeOnServer = this.allowCaveModeOnServer;
         result.allowNetherCaveModeOnServer = this.allowNetherCaveModeOnServer;
         result.allowRadarOnServer = this.allowRadarOnServer;
         return result;
      }

      public static CommonConfig.Builder begin() {
         return new CommonConfig.Builder().setDefault();
      }
   }
}
