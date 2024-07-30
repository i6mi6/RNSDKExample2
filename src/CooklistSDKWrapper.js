import React from 'react'
import { ActivityIndicator, NativeModules, View } from 'react-native'
import Storelink from 'react-native-storelink'
import InnerContainer from './InnerContainer'
import { EVENT_TYPES, VIEW_TYPE } from './constants'
import { LOG_LEVEL, logError, logEventDebug, logEventDev } from './utils/util'
const { StorelinkModule } = NativeModules

class CooklistSDKWrapper extends React.Component {

  state = {
    loading: true,
  }

  UNSAFE_componentWillMount() {
    this.initialSetup()
  }

  initialSetup = async () => {
    try {
      const { viewType, logLevel, refreshToken, brandName, logoUrl, _devApiLocation } = this.props

      StorelinkModule.sampleMethod('test', console.log)

      const [sdkResponse] = await Promise.all([
        // Storelink.configure({
        //   refreshToken,
        //   onStoreConnectionEvent: this.onStoreConnectionEvent,
        //   onInvoiceEvent: this.onInvoiceEvent,
        //   onCheckingStoreConnectionEvent: this.onCheckingStoreConnectionEvent,
        //   _backgroundDisabled: viewType !== VIEW_TYPE.BACKGROUND_TASK,
        //   _devApiLocation: _devApiLocation,
        //   _logLevel: logLevel,
        //   brandName,
        //   logoUrl,
        // }),
        Storelink.configure({
          refreshToken: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo2ODMxMjEsIm9pZCI6MTAwOSwidmVyc2lvbiI6MSwianRpIjoiYjFjYjgzMWItMmY0MS00YTg1LThhNTUtMDk5M2U4MGY3OTBiIiwiaXNzdWVkX2F0IjoxNzIyMzY5NTQxLjk3MzE5NSwiZXhwaXJlc19hdCI6MTc1MzkwNTU0MS45NzMxOTUsInRva2VuX3R5cGUiOiJyZWZyZXNoIn0.BsjOG1ZwtuHx51xntqnnGrt5KWZ2_WKHrio0QeeuqZM',
          onStoreConnectionEvent: this.onStoreConnectionEvent,
          onInvoiceEvent: this.onInvoiceEvent,
          onCheckingStoreConnectionEvent: this.onCheckingStoreConnectionEvent,
          _devApiLocation: 'http://192.168.0.10:8000/gql',
          _logLevel: LOG_LEVEL.DEV,
          brandName: 'Cooklist',
          logoUrl: 'https://play-lh.googleusercontent.com/1MgS_1nBA858MqMzhu-cqeXpbkTC3tVrshkj79IAuKhDlN7LZXdH4ECw6wiwA86vUQ',
        }),
      ])
      if (sdkResponse.success) {
        this.setState({ loading: false })
        logEventDebug(this.props.logLevel, 'SDK initialized!')
      } else {
        logEventDebug(this.props.logLevel, `Error initializing CooklistSDK: ${sdkResponse.message}`)
      }
      const deviceUuid = Storelink.getDeviceUuid()
      logEventDev(this.props.logLevel, `deviceUuid: ${deviceUuid}`)
      this.onConfigurationSuccess({
        deviceUuid,
      })
    } catch (error) {
      logError(error)
    }
  }

  onConfigurationSuccess = (payload) => {
    StorelinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_EVENT, {
      functionName: 'onConfigurationSuccess',
      ...payload,
    })
    logEventDev(this.props.logLevel, `[REACT NATIVE] onConfigurationSuccess:`, payload)
  }

  onStoreConnectionEvent = ({ storeConnectionId, credentialsStatus }) => {
    StorelinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_EVENT, {
      functionName: 'onStoreConnectionEvent',
      storeConnectionId,
      credentialsStatus,
    })
    logEventDev(this.props.logLevel, `[REACT NATIVE] onStoreConnectionEvent:`, { storeConnectionId, credentialsStatus })
  }

  onInvoiceEvent = ({ storeConnectionId, orderIds }) => {
    StorelinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_EVENT, {
      functionName: 'onInvoiceEvent',
      storeConnectionId,
      orderIds,
    })
    logEventDev(this.props.logLevel, `[REACT NATIVE] onInvoiceEvent:`, {
      storeConnectionId,
      orderIds,
    })
  }

  onCheckingStoreConnectionEvent = (payload) => {
    StorelinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_EVENT, {
      functionName: 'onCheckingStoreConnectionEvent',
      ...(payload || {}),
    })
    logEventDev(this.props.logLevel, `[REACT NATIVE] onCheckingStoreConnectionEvent:`, payload)
  }

  onViewComplete = (payload) => {
    try {
      const { viewUUID } = this.props
      if (!viewUUID) {
        return
      }
      StorelinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_VIEW_COMPLETE_EVENT, {
        viewUUID,
        ...(payload || {}),
      })
      logEventDev(this.props.logLevel, `[REACT NATIVE] onViewComplete:`, payload)
    } catch (error) {
      logError(this.props.logLevel, error)
    }
  }

  render() {
    const { viewType, functionParams } = this.props
    const { loading } = this.state
    logEventDebug(this.props.logLevel, '[REACT NATIVE] props:', this.props)
    if (viewType === VIEW_TYPE.DEVICE_UUID) {
      return null
    }
    if (loading) {
      if (viewType === VIEW_TYPE.BACKGROUND_TASK) {
        return null
      }
      return (
        <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
          <ActivityIndicator />
        </View>
      )
    }
    return (
      <Storelink.Provider>
        <InnerContainer
          viewType={viewType}
          functionParams={functionParams}
          onViewComplete={this.onViewComplete}
        />
      </Storelink.Provider>
    )
  }
}

export default CooklistSDKWrapper