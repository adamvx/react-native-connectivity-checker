#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ConnectivityChecker, NSObject)

RCT_EXTERN_METHOD(isLocationEnabled: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
