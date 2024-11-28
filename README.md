# Yet-Another-Xaeros-Revision  
What if cave mode just worked instead  
  
### Ignore serverside radar settings
###### classes > xaero > common > minimap > MinimapProcessor.class | getForcedFairPlay
```
LINE A *(unchanged)
ICONST_0
IRETURN
```
  
### Ignore serverside minimap disabler
###### classes > xaero > common > minimap > MinimapProcessor.class | getNoMinimapMessageReceived
```
LINE A *(unchanged)
ICONST_0
IRETURN
```  
  
### Disable update checker
###### classes > xaero > common > misc > Internet.class | checkModVersion
```
ALOAD modMain
ICONST_0
INVOKEINTERFACE xaero/common/IXaeroMinimap.setOutdated(Z)V
```  
