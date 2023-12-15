//
//  RNSDKViewController.swift
//  RNSDK
//
import Foundation
import React

public enum ViewType {
    case backgroundTask
    case storeConnectionsList
    case connectUpdateStore

    var stringValue: String {
        switch self {
            case .backgroundTask: return "BACKGROUND_TASK"
            case .storeConnectionsList: return "STORE_CONNECTIONS_LIST"
            case .connectUpdateStore: return "CONNECT_UPDATE_STORE"
        }
    }
}

open class RNSDKViewController: UIViewController {
    var refreshToken: String
    var viewType: ViewType
    var functionParams: [AnyHashable: Any]?

    init(refreshToken: String, viewType: ViewType, functionParams: [AnyHashable: Any]?) {
        self.refreshToken = refreshToken
        self.viewType = viewType
        self.functionParams = functionParams
        super.init(nibName: nil, bundle: nil)
    }

    required public init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override public func loadView() {
        let bundle: Bundle = Bundle.main
        var bundleURL = bundle.resourceURL

        if let url = bundleURL?.appendingPathComponent("RNSDK.bundle/rnsdk.jsbundle") {
            var initialProperties: [String: Any] = [
                "refreshToken": refreshToken,
                "viewType": viewType.stringValue
            ]
            if let functionParams = functionParams {
                initialProperties["functionParams"] = functionParams
            }

            let view = RCTRootView(bundleURL: url, moduleName: "RNSDKExample2", initialProperties: initialProperties)
            self.view = view
        }
    }
}

