//
//  StorelinkCore.swift
//  Storelink
//
import Foundation
import UIKit

public class StorelinkCore {
    
    // Configuration for the SDK
    public struct Configuration {
        let refreshToken: String
        let logLevel: LogLevel?
        let onConfigurationSuccess: (([AnyHashable: Any]) -> Void)?
        let onStoreConnectionEvent: (([AnyHashable: Any]) -> Void)?
        let onInvoiceEvent: (([AnyHashable: Any]) -> Void)?
        let onCheckingStoreConnectionEvent: (([AnyHashable: Any]) -> Void)?
        let brandName: String?
        let logoUrl: String?
        let _devApiLocation: String?
        // Add a public initializer
        public init(
            refreshToken: String,
            logLevel: LogLevel? = nil,
            onConfigurationSuccess: (([AnyHashable: Any]) -> Void)? = nil,
            onStoreConnectionEvent: (([AnyHashable: Any]) -> Void)? = nil,
            onInvoiceEvent: (([AnyHashable: Any]) -> Void)? = nil,
            onCheckingStoreConnectionEvent: (([AnyHashable: Any]) -> Void)? = nil,
            brandName: String? = nil,
            logoUrl: String? = nil,
            _devApiLocation: String? = nil
        ) {
            self.refreshToken = refreshToken
            self.logLevel = logLevel
            self.onConfigurationSuccess = onConfigurationSuccess
            self.onStoreConnectionEvent = onStoreConnectionEvent
            self.onInvoiceEvent = onInvoiceEvent
            self.onCheckingStoreConnectionEvent = onCheckingStoreConnectionEvent
            self.brandName = brandName
            self.logoUrl = logoUrl
            self._devApiLocation = _devApiLocation
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
        private var onConfigurationSuccess: (([AnyHashable: Any]) -> Void)?
        private var onStoreConnectionEvent: (([AnyHashable: Any]) -> Void)?
        private var onInvoiceEvent: (([AnyHashable: Any]) -> Void)?
        private var onCheckingStoreConnectionEvent: (([AnyHashable: Any]) -> Void)?
        
        public init(config: Configuration) {
            self.config = config
            self.onConfigurationSuccess = config.onConfigurationSuccess
            self.onStoreConnectionEvent = config.onStoreConnectionEvent
            self.onInvoiceEvent = config.onInvoiceEvent
            self.onCheckingStoreConnectionEvent = config.onCheckingStoreConnectionEvent
            setupNotificationObservers()
        }
        
        deinit {
          removeNotificationObservers()
        }
        
        public class SDKViewController: StorelinkViewController {
        }

        public func getBackgroundView() -> StorelinkCoreView {
            return StorelinkCoreView(refreshToken: config.refreshToken, logLevel: config.logLevel, viewType: .backgroundTask, _devApiLocation: config._devApiLocation)
        }
//        
//        public func getStoreConnectionsListView() -> StorelinkCoreView {
//            return StorelinkCoreView(refreshToken: config.refreshToken, logLevel: config.logLevel, viewType: .storeConnectionsList)
//        }
      
        public func getConnectUpdateStoreView(storeId: String, onComplete: (([AnyHashable: Any]) -> Void)? = nil) -> StorelinkCoreView {
            return StorelinkCoreView(refreshToken: config.refreshToken, logLevel: config.logLevel, viewType: .connectUpdateStore, functionParams: ["storeId": storeId], onComplete: onComplete, brandName: config.brandName, logoUrl: config.logoUrl, _devApiLocation: config._devApiLocation)
        }
      
        public func getTransferCartView(cartId: String, onComplete: (([AnyHashable: Any]) -> Void)? = nil) -> StorelinkCoreView {
            return StorelinkCoreView(refreshToken: config.refreshToken, logLevel: config.logLevel, viewType: .transferCart, functionParams: ["cartId": cartId], onComplete: onComplete, brandName: config.brandName, logoUrl: config.logoUrl, _devApiLocation: config._devApiLocation)
        }

        private func setupNotificationObservers() {
            NotificationCenter.default.addObserver(forName: self.notificationName, object: nil, queue: .main) { notification in
                if let params = notification.userInfo as? [String: Any],
                let functionName = params["functionName"] as? String {
                    switch functionName {
                    case "onConfigurationSuccess":
                        self.onConfigurationSuccess?(params)
                    case "onStoreConnectionEvent":
                        self.onStoreConnectionEvent?(params)
                    case "onInvoiceEvent":
                        self.onInvoiceEvent?(params)
                    case "onCheckingStoreConnectionEvent":
                        self.onCheckingStoreConnectionEvent?(params)
                    default:
                        break // or handle unknown functionName
                    }
                }
            }
        }
        
        private func removeNotificationObservers() {
            NotificationCenter.default.removeObserver(self, name: self.notificationName, object: nil)
        }

        public func sendDataToReactNative(data: [AnyHashable: Any]) {
            NotificationCenter.default.post(name: Notification.Name("CooklistDataFromNative"), object: nil, userInfo: data)
        }

        // public func sendDataToReactNativeAndWait(data: [AnyHashable: Any]) async throws -> [AnyHashable: Any]? {

        //     sendDataToReactNative(data: data)

        //     // Use Task for timeout handling; 10s limit
        //     return try await withCheckedThrowingContinuation { continuation in
        //         let timeoutTask = Task {
        //             try await Task.sleep(nanoseconds: UInt64(10_000_000_000))
        //             continuation.resume(throwing: SDKError.nativeCommunicationError)
        //         }

        //         NotificationCenter.default.addObserver(forName: self.notificationName, object: nil, queue: .main) { notification in
        //             timeoutTask.cancel()  // Cancel the timeout task if we receive a response

        //             if let params = notification.userInfo {
        //                 continuation.resume(returning: params)
        //             }
        //         }
        //     }
        // }

        // The method to open a UI based on the specified presentation method
        // public func open(presentUsing method: PresentationMethod) {
        //     let sdkUI = StorelinkViewController()
            
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
        do {
          let handler = SDKHandler(config: config)
          return .success(handler)
        } catch let error as SDKError {
            // Handle specific SDKError
            return .failure(error)
        } catch {
            // Handle other errors
            return .failure(.unknownError)
        }
    }
}
