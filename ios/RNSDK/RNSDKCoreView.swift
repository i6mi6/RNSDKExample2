import SwiftUI

public struct RNSDKCoreView: UIViewControllerRepresentable {
  
  public init() {}
  
  public typealias UIViewControllerType = UINavigationController

  public func makeUIViewController(context: Context) -> UINavigationController {
        // Here, we create and return our SDK's UI
        let sdkVC = RNSDKCore.SDKHandler.SDKViewController()
        let navigationController = UINavigationController(rootViewController: sdkVC)
        return navigationController
    }

  public func updateUIViewController(_ uiViewController: UINavigationController, context: Context) {
        // Update code if needed
    }
}
