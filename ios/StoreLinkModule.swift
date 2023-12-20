import Foundation
import React
import UIKit
import AVFoundation

@objc(StoreLinkModule)
class StoreLinkModule: RCTEventEmitter {

    private var observer: NSObjectProtocol?
  
    @objc override func supportedEvents() -> [String] {
        return ["DataFromNative"]
    }

    override init() {
        super.init()
        
        let notificationNameToObserve = Notification.Name("DataFromNative")
      
        observer = NotificationCenter.default.addObserver(forName: notificationNameToObserve, object: nil, queue: .main) { notification in
              if let params = notification.userInfo {
                self.sendEvent(withName: "DataFromNative", body: params)
              }
          }
        
    }
    
    deinit {
        if let observer = observer {
            NotificationCenter.default.removeObserver(observer)
        }
    }

    @objc(sendNotification:params:)
    func sendNotification(notification: NSString, params: NSDictionary) {
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: notification as String), object: nil, userInfo: params as? [AnyHashable: Any])
    }

}
