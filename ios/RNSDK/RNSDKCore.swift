import Foundation
import UIKit

class RNSDKCore {
    
    // Configuration for the SDK
    struct Configuration {
        let apiKey: String
        let onEvent: (([AnyHashable: Any]) -> Void)?
    }
    
    // Possible errors for the SDK
    enum SDKError: Error {
        case invalidAPIKey
        case unknownError
    }
    
    enum PresentationMethod {
        case presentModally(on: UIViewController)
        case pushOnNavigationStack(navigationController: UINavigationController)
        case embedInTab(tabBarController: UITabBarController, at: Int?)
    }
    
    // Define a handler that the SDK uses
    class SDKHandler {
        
        private var notificationName = Notification.Name("openUserScreen")
        private var onEvent: (([AnyHashable: Any]) -> Void)?
        
        init(onEvent: (([AnyHashable: Any]) -> Void)?) {
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
                print("Received a notification!")
                if let params = notification.userInfo,
                   let userId = params["userId"] as? String {
                    NSLog("Log from openUserController: \(userId)")
                    self.onEvent?(params)
                }
            }
        }
        
        private func removeNotificationObserver() {
            NotificationCenter.default.removeObserver(self, name: self.notificationName, object: nil)
        }
    
        // The method to open a UI based on the specified presentation method
        func open(presentUsing method: PresentationMethod) {
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
    static func create(_ config: Configuration) -> Result<SDKHandler, SDKError> {
        // Simulating an API key check
        guard config.apiKey == "VALID_API_KEY" else {
            return .failure(.invalidAPIKey)
        }
        
        // If everything is valid, return a new handler
        return .success(SDKHandler(onEvent: config.onEvent))
    }
}
