import React from 'react'
import { ActivityIndicator, NativeModules, View } from 'react-native'
import Storelink from 'react-native-storelink'
import InnerContainer from './InnerContainer'
import { EVENT_TYPES, VIEW_TYPE } from './constants'
import { logError, logEventDebug, logEventDev } from './utils/util'
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
      const { viewType, logLevel, refreshToken, brandName, logoUrl } = this.props
      const [sdkResponse] = await Promise.all([
        Storelink.configure({
          refreshToken,
          onStoreConnectionEvent: this.onStoreConnectionEvent,
          onInvoiceEvent: this.onInvoiceEvent,
          onCheckingStoreConnectionEvent: this.onCheckingStoreConnectionEvent,
          _backgroundDisabled: viewType !== VIEW_TYPE.BACKGROUND_TASK,
          _logLevel: logLevel,
          brandName,
          logoUrl,
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