//
//  RNSDKCode.swift
//  RNSDK
//
import Foundation
import UIKit

public class RNSDKCore {
    
    // Configuration for the SDK
    public struct Configuration {
        let refreshToken: String
        let onEvent: (([AnyHashable: Any]) -> Void)?
        // Add a public initializer
        public init(refreshToken: String, onEvent: (([AnyHashable: Any]) -> Void)? = nil) {
            self.refreshToken = refreshToken
            self.onEvent = onEvent
        }
    }
    
    // Possible errors for the SDK
    public enum SDKError: Error {
        case invalidRefreshToken
        case unknownError
        case nativeCommunicationError
    }
    
    public enum PresentationMethod {
        case presentModally(on: UIViewController)
        case pushOnNavigationStack(navigationController: UINavigationController)
        case embedInTab(tabBarController: UITabBarController, at: Int?)
    }
    
    
    // Define a handler that the SDK uses
    public class SDKHandler {
        
        private var config: Configuration
        private var notificationName = Notification.Name("cooklist_sdk_event")
        private var onEvent: (([AnyHashable: Any]) -> Void)?
        
        public init(config: Configuration) {
            self.config = config
            self.onEvent = config.onEvent
            setupNotificationObserver()
        }
        
        deinit {
            removeNotificationObserver()
        }
        
        public class SDKViewController: RNSDKViewController {
        }

        public func getRNSDKCoreView() -> RNSDKCoreView {
            return RNSDKCoreView(refreshToken: config.refreshToken)
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
      
        public func sendDataToReactNativeAndWait(data: [AnyHashable: Any]) async throws -> [AnyHashable: Any]? {

            sendDataToReactNative(data: data)

            // Use Task for timeout handling; 10s limit
            return try await withCheckedThrowingContinuation { continuation in
                let timeoutTask = Task {
                    try await Task.sleep(nanoseconds: UInt64(10_000_000_000))
                    continuation.resume(throwing: SDKError.nativeCommunicationError)
                }

                NotificationCenter.default.addObserver(forName: self.notificationName, object: nil, queue: .main) { notification in
                    timeoutTask.cancel()  // Cancel the timeout task if we receive a response

                    if let params = notification.userInfo {
                        continuation.resume(returning: params)
                    }
                }
            }
        }

        // The method to open a UI based on the specified presentation method
        // public func open(presentUsing method: PresentationMethod) {
        //     let sdkUI = RNSDKViewController()
            
        //     switch method {
        //     case .presentModally(let parentVC):
        //         let navigationController = UINavigationController(rootViewController: sdkUI)
        //         parentVC.present(navigationController, animated: true)
                
        //     case .pushOnNavigationStack(let navigationController):
        //         navigationController.pushViewController(sdkUI, animated: true)
                
        //     case .embedInTab(let tabBarController, let position):
        //         if let index = position, index < tabBarController.viewControllers?.count ?? 0 {
        //             tabBarController.viewControllers?.insert(sdkUI, at: index)
        //         } else {
        //             tabBarController.viewControllers?.append(sdkUI)
        //         }
        //     }
        // }
    }
    
    // Create a handler based on configuration
    public static func create(_ config: Configuration) async -> Result<SDKHandler, SDKError> {
        // // Simulating an API key check
        // guard config.refreshToken == "VALID_API_KEY" else {
        //     return .failure(.invalidRefreshToken)
        // }

        // Create an instance of SDKHandler
        let handler = SDKHandler(config: config)

        return .success(handler)

        // // Prepare data to send (for example, config data)
        // let dataToSend: [AnyHashable: Any] = ["refreshToken": config.refreshToken]
        

        // do {
        //     // Attempt to send data and await response
        //     let response = try await handler.sendDataToReactNativeAndWait(data: dataToSend)

        //     // Log the response
        //     print("Response: \(response)")

        //     // Check the response and decide whether to return the handler
        //     // if let responseData = response, responseData["isValid"] as? Bool == true {
        //     //     return .success(handler)
        //     // } else {
        //     //     return .failure(.unknownError)
        //     // }
        //     return .success(handler)
        // } catch let error as SDKError {
        //     // Handle specific SDKError
        //     return .failure(error)
        // } catch {
        //     // Handle other errors
        //     return .failure(.unknownError)
        // }

    }
}
