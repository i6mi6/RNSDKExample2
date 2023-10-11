#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(StoreConnectionModule, NSObject)

RCT_EXTERN_METHOD(onStoreConnected:(NSString *)connectionStatus)
RCT_EXTERN_METHOD(sendNotification:(NSString*)notification params:(NSDictionary*)params)

@end
