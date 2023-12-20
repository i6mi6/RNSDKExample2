//
//  StoreLinkViewController.swift
//  StoreLink
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

open class StoreLinkViewController: UIViewController {
  
    private var viewUUID: String = UUID().uuidString
    private var notificationName = Notification.Name("cooklist_sdk_view_complete_event")

    var refreshToken: String
    var viewType: ViewType
    var functionParams: [AnyHashable: Any]?
    var onComplete: (([AnyHashable: Any]) -> Void)?

    init(refreshToken: String, viewType: ViewType, functionParams: [AnyHashable: Any]?, onComplete: (([AnyHashable: Any]) -> Void)?) {
        self.refreshToken = refreshToken
        self.viewType = viewType
        self.functionParams = functionParams
        self.onComplete = onComplete
        super.init(nibName: nil, bundle: nil)
    }

    required public init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override public func loadView() {
        let bundle: Bundle = Bundle.main
        var bundleURL = bundle.resourceURL

        if let url = bundleURL?.appendingPathComponent("StoreLink.bundle/storelink.jsbundle") {
            var initialProperties: [String: Any] = [
                "refreshToken": refreshToken,
                "viewType": viewType.stringValue,
                "viewUUID": viewUUID
            ]
            if let functionParams = functionParams {
                initialProperties["functionParams"] = functionParams
            }

            NotificationCenter.default.addObserver(forName: self.notificationName, object: nil, queue: .main) { [weak self] notification in
                guard let self = self,
                      let params = notification.userInfo as? [String: Any],
                      let notificationUUID = params["viewUUID"] as? String,
                      notificationUUID == self.viewUUID else {
                          return
                      }
                self.onComplete?(params)
            }

            let view = RCTRootView(bundleURL: url, moduleName: "StoreLinkProject", initialProperties: initialProperties)
            self.view = view
        }
    }
}

