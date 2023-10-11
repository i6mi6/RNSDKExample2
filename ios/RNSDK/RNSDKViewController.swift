//
//  RNSDKViewController.swift
//  RNSDK
//
import Foundation
import React

open class RNSDKViewController: UIViewController {
  override public func loadView() {
    let bundle: Bundle = Bundle.main
    var bundleURL = bundle.resourceURL
    
    if (bundleURL != nil) {
      bundleURL!.appendPathComponent("RNSDK.bundle/rnsdk.jsbundle")
      
      let view = RCTRootView(bundleURL: bundleURL!, moduleName: "RNSDKExample2", initialProperties: [:])
      self.view = view
    }
  }
  
//  override open func viewDidLoad() {
//      super.viewDidLoad()
//      NotificationCenter.default.addObserver(self, selector: #selector(didReceiveOpenUserNotification(_:)), name: NSNotification.Name("openUserScreen"), object: nil)
//  }
//
//  @objc func didReceiveOpenUserNotification(_ notification: Notification) {
//      NSLog("Log from openUserController----")
//      if let params = notification.userInfo,
//         let reactTag = (self.view as? RCTRootView)?.reactTag,
//         let rootTag = params["rootTag"] as? NSNumber,
//         reactTag == rootTag,
//         let userId = params["userId"] as? String {
//          
//          openUserController(userId)
//      }
//  }
//  
//  func openUserController(_ userId: String) {
//      // Implement the function body here
//    NSLog("Log from openUserController: \(userId)")
//  }
}
