//
//  RNSDKViewController.swift
//  RNSDK
//
import Foundation
import React

open class RNSDKViewController: UIViewController {
    var refreshToken: String

    public init(refreshToken: String) {
        self.refreshToken = refreshToken
        super.init(nibName: nil, bundle: nil)
    }

    required public init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override public func loadView() {
        let bundle: Bundle = Bundle.main
        var bundleURL = bundle.resourceURL
        
        if let url = bundleURL?.appendingPathComponent("RNSDK.bundle/rnsdk.jsbundle") {
            let initialProperties = ["refreshToken": refreshToken]
            let view = RCTRootView(bundleURL: url, moduleName: "RNSDKExample2", initialProperties: initialProperties)
            self.view = view
        }
    }
}

