//
//  StoreLinkCoreView.swift
//  StoreLink
//
import SwiftUI

public struct StoreLinkCoreView: UIViewControllerRepresentable {
    var refreshToken: String
    var logLevel: LogLevel?
    var viewType: ViewType
    var functionParams: [AnyHashable: Any]?
    var onComplete: (([AnyHashable: Any]) -> Void)?

    public init(
      refreshToken: String,
      logLevel: LogLevel? = nil,
      viewType: ViewType = .backgroundTask,
      functionParams: [AnyHashable: Any]? = nil,
      onComplete: (([AnyHashable: Any]) -> Void)? = nil
    ) {
        self.refreshToken = refreshToken
        self.viewType = viewType
        self.logLevel = logLevel
        self.functionParams = functionParams
        self.onComplete = onComplete
    }
  
    public typealias UIViewControllerType = UINavigationController

    public func makeUIViewController(context: Context) -> UINavigationController {
        // Create the SDKViewController with the refreshToken and viewType
      let sdkVC = StoreLinkCore.SDKHandler.SDKViewController(
        refreshToken: refreshToken,
        viewType: viewType,
        logLevel: logLevel,
        functionParams: functionParams,
        onComplete: onComplete
      )
        let navigationController = UINavigationController(rootViewController: sdkVC)
        return navigationController
    }

    public func updateUIViewController(_ uiViewController: UINavigationController, context: Context) {
        // Update code if needed
    }
}
