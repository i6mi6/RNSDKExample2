import Foundation
import UIKit
import AVFoundation

@objc(StoreConnectionModule)
class StoreConnectionModule: NSObject {
  
  @objc(onStoreConnected:)
  public func onStoreConnected(connectionStatus: String) {
     NSLog("Log from Swift: \(connectionStatus)")
   }

}
