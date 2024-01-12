import Foundation
import React
import UIKit
import AVFoundation

@objc(StorelinkModule)
class StorelinkModule: RCTEventEmitter {

    private var observer: NSObjectProtocol?
  
    @objc override func supportedEvents() -> [String] {
        return ["CooklistDataFromNative"]
    }

    override init() {
        super.init()
        
        let notificationNameToObserve = Notification.Name("CooklistDataFromNative")
      
        observer = NotificationCenter.default.addObserver(forName: notificationNameToObserve, object: nil, queue: .main) { notification in
              if let params = notification.userInfo {
                self.sendEvent(withName: "CooklistDataFromNative", body: params)
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
