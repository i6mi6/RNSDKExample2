import Foundation
import UIKit
import AVFoundation

@objc(StoreConnectionModule)
class StoreConnectionModule: NSObject {
//
//    var callback: ((String) -> Void)?
//
//    init(callback: @escaping (String) -> Void) {
//        self.callback = callback
//        super.init()
//    }
//
//    @objc(triggerCallback:)
//    func triggerCallback(message: String) {
//        NSLog("Log from triggerCallback: \(message)")
//        callback?(message)
//    }

    @objc(onStoreConnected:)
    public func onStoreConnected(connectionStatus: String) {
       NSLog("Log from onStoreConnected: \(connectionStatus)")
     }
  
    @objc(sendNotification:params:)
    func sendNotification(notification: NSString, params: NSDictionary) {
        NSLog("Log from sendNotification1: \(params)")
        NSLog("Log from sendNotification2: \(notification)")
      
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: notification as String), object: nil, userInfo: params as? [AnyHashable: Any])
    }

}
