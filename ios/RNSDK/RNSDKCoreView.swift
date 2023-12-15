//
//  RNSDKCoreView.swift
//  RNSDK
//
import SwiftUI

public struct RNSDKCoreView: UIViewControllerRepresentable {
    var refreshToken: String
    var viewType: ViewType
    var functionParams: [AnyHashable: Any]?

    public init(refreshToken: String, viewType: ViewType = .backgroundTask, functionParams: [AnyHashable: Any]? = nil) {
        self.refreshToken = refreshToken
        self.viewType = viewType
        self.functionParams = functionParams
    }
  
    public typealias UIViewControllerType = UINavigationController

    public func makeUIViewController(context: Context) -> UINavigationController {
        // Create the SDKViewController with the refreshToken and viewType
        let sdkVC = RNSDKCore.SDKHandler.SDKViewController(refreshToken: refreshToken, viewType: viewType, functionParams: functionParams)
        let navigationController = UINavigationController(rootViewController: sdkVC)
        return navigationController
    }

    public func updateUIViewController(_ uiViewController: UINavigationController, context: Context) {
        // Update code if needed
    }
}
