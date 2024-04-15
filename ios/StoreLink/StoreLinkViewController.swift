//
//  StorelinkViewController.swift
//  Storelink
//
import Foundation
import React

public enum ViewType {
    case backgroundTask
    case storeConnectionsList
    case connectUpdateStore
    case transferCart
    case deviceUuid

    var stringValue: String {
        switch self {
            case .backgroundTask: return "BACKGROUND_TASK"
            case .storeConnectionsList: return "STORE_CONNECTIONS_LIST"
            case .connectUpdateStore: return "CONNECT_UPDATE_STORE"
            case .transferCart: return "TRANSFER_CART"
            case .deviceUuid: return "DEVICE_UUID"
        }
    }
}

public enum LogLevel {
    case none
    case debug
    case dev

    var intValue: Int {
        switch self {
            case .none: return 1
            case .debug: return 2
            case .dev: return 3
        }
    }
}

open class StorelinkViewController: UIViewController {
  
    private var viewUUID: String = UUID().uuidString
    private var viewCompleteNotificationName = Notification.Name("cooklist_sdk_view_complete_event")

    var refreshToken: String
    var viewType: ViewType
    var logLevel: LogLevel?
    var functionParams: [AnyHashable: Any]?
    var onComplete: (([AnyHashable: Any]) -> Void)?
    var brandName: String?
    var logoUrl: String?

    init(refreshToken: String, viewType: ViewType, logLevel: LogLevel?, functionParams: [AnyHashable: Any]?, onComplete: (([AnyHashable: Any]) -> Void)?, brandName: String?, logoUrl: String?) {
        self.refreshToken = refreshToken
        self.viewType = viewType
        self.logLevel = logLevel
        self.functionParams = functionParams
        self.onComplete = onComplete
        self.brandName = brandName
        self.logoUrl = logoUrl
        super.init(nibName: nil, bundle: nil)
    }

    required public init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    deinit {
        removeNotificationObservers()
        onViewUnmount()
    }

    private func removeNotificationObservers() {
        NotificationCenter.default.removeObserver(self, name: self.viewCompleteNotificationName, object: nil)
    }
  
    private func onViewUnmount(){
      guard viewType == .connectUpdateStore,
            let storeId = functionParams?["storeId"] as? String else {
        return
      }
      let data: [String: Any] = [
          "_cooklistInternal": true,
          "eventType": "cooklist_sdk_event_eager_check_bg_purchases",
          "storeId": storeId,
      ]
      NotificationCenter.default.post(name: Notification.Name("CooklistDataFromNative"), object: nil, userInfo: data)
    }

    override public func loadView() {
        let bundle: Bundle = Bundle.main
        var bundleURL = bundle.resourceURL

        if let url = bundleURL?.appendingPathComponent("Storelink.bundle/storelink.jsbundle") {
            var initialProperties: [String: Any] = [
                "refreshToken": refreshToken,
                "logLevel": logLevel?.intValue,
                "viewType": viewType.stringValue,
                "viewUUID": viewUUID,
                "brandName": brandName,
                "logoUrl": logoUrl
            ]
            if let functionParams = functionParams {
                initialProperties["functionParams"] = functionParams
            }

            NotificationCenter.default.addObserver(forName: self.viewCompleteNotificationName, object: nil, queue: .main) { [weak self] notification in
                guard let self = self,
                      let params = notification.userInfo as? [String: Any],
                      let notificationUUID = params["viewUUID"] as? String,
                      notificationUUID == self.viewUUID else {
                          return
                      }
                self.onComplete?(params)
            }

            let view = RCTRootView(bundleURL: url, moduleName: "StorelinkProject", initialProperties: initialProperties)
            self.view = view
        }
    }
}

