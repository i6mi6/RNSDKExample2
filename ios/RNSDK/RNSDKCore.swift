//
//  RNSDKCode.swift
//  RNSDK
//
import Foundation
import UIKit

public class RNSDKCore {
    
    // Configuration for the SDK
    public struct Configuration {
        let apiKey: String
        let onEvent: (([AnyHashable: Any]) -> Void)?
        // Add a public initializer
        public init(apiKey: String, onEvent: (([AnyHashable: Any]) -> Void)? = nil) {
            self.apiKey = apiKey
            self.onEvent = onEvent
        }
    }
    
    // Possible errors for the SDK
    public enum SDKError: Error {
        case invalidAPIKey
        case unknownError
    }
    
    public enum PresentationMethod {
        case presentModally(on: UIViewController)
        case pushOnNavigationStack(navigationController: UINavigationController)
        case embedInTab(tabBarController: UITabBarController, at: Int?)
    }
    
    // Define a handler that the SDK uses
    public class SDKHandler {
        
        private var notificationName = Notification.Name("store_credentials_event")
        private var onEvent: (([AnyHashable: Any]) -> Void)?
        
        public init(onEvent: (([AnyHashable: Any]) -> Void)?) {
            self.onEvent = onEvent
            setupNotificationObserver()
        }
        
        deinit {
            removeNotificationObserver()
        }
        
        class SDKViewController: RNSDKViewController {
        }
        
        private func setupNotificationObserver() {
            NotificationCenter.default.addObserver(forName: self.notificationName, object: nil, queue: .main) { notification in
                if let params = notification.userInfo {
                    self.onEvent?(params)
                }
            }
        }
        
        private func removeNotificationObserver() {
            NotificationCenter.default.removeObserver(self, name: self.notificationName, object: nil)
        }
      
        public func sendDataToReactNative(data: [AnyHashable: Any]) {
            NotificationCenter.default.post(name: Notification.Name("DataFromNative"), object: nil, userInfo: data)
        }
      
        // The method to open a UI based on the specified presentation method
        public func open(presentUsing method: PresentationMethod) {
            let sdkUI = RNSDKViewController()
            
            switch method {
            case .presentModally(let parentVC):
                let navigationController = UINavigationController(rootViewController: sdkUI)
                parentVC.present(navigationController, animated: true)
                
            case .pushOnNavigationStack(let navigationController):
                navigationController.pushViewController(sdkUI, animated: true)
                
            case .embedInTab(let tabBarController, let position):
                if let index = position, index < tabBarController.viewControllers?.count ?? 0 {
                    tabBarController.viewControllers?.insert(sdkUI, at: index)
                } else {
                    tabBarController.viewControllers?.append(sdkUI)
                }
            }
        }
    }
    
    // Create a handler based on configuration
    public static func create(_ config: Configuration) -> Result<SDKHandler, SDKError> {
        // Simulating an API key check
        guard config.apiKey == "VALID_API_KEY" else {
            return .failure(.invalidAPIKey)
        }
        
        // If everything is valid, return a new handler
        return .success(SDKHandler(onEvent: config.onEvent))
    }
}
