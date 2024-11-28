# Yet-Another-Xaeros-Revision  
What if cave mode just worked instead  
  
### Ignore serverside radar settings
###### classes > xaero > common > minimap > MinimapProcessor.class | getForcedFairPlay

Change from...
```
ALOAD this
GETFIELD xaero/common/minimap/MinimapProcessor.fairPlayOnlyMessageReceived Z
IRETURN
```
...to:
```
ICONST_0
IRETURN
```
  
### Ignore serverside minimap disabler
###### classes > xaero > common > minimap > MinimapProcessor.class | getNoMinimapMessageReceived

Change from...
```
ALOAD this
GETFIELD xaero/common/minimap/MinimapProcessor.noMinimapMessageReceived Z
IRETURN
```
...to:
```
ICONST_0
IRETURN
```
  
### Disable update checker
###### classes > xaero > common > misc > Internet.class | checkModVersion

Change from...
```
ALOAD modMain
ICONST_1
INVOKEINTERFACE xaero/common/IXaeroMinimap.setOutdated(Z)V
```
...to:
```
ALOAD modMain
ICONST_0
INVOKEINTERFACE xaero/common/IXaeroMinimap.setOutdated(Z)V
```
