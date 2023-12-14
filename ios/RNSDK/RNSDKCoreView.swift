//
//  RNSDKCoreView.swift
//  RNSDK
//
import SwiftUI

public struct RNSDKCoreView: UIViewControllerRepresentable {
    var refreshToken: String

    public init(refreshToken: String) {
        self.refreshToken = refreshToken
    }
  
    public typealias UIViewControllerType = UINavigationController

    public func makeUIViewController(context: Context) -> UINavigationController {
        // Create the SDKViewController with the refreshToken
        let sdkVC = RNSDKCore.SDKHandler.SDKViewController(refreshToken: refreshToken)
        let navigationController = UINavigationController(rootViewController: sdkVC)
        return navigationController
    }

    public func updateUIViewController(_ uiViewController: UINavigationController, context: Context) {
        // Update code if needed
    }
}
