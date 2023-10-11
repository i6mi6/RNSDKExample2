#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(StoreConnectionModule, NSObject)

RCT_EXTERN_METHOD(onStoreConnected:(NSString *)connectionStatus)

@end
