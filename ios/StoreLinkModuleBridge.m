#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(StorelinkModule, NSObject)

RCT_EXTERN_METHOD(sendNotification:(NSString*)notification params:(NSDictionary*)params)

@end
