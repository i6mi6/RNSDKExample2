import React from 'react'
import { ActivityIndicator, NativeModules, View } from 'react-native'
import CooklistSDK from 'react-native-cooklist'
import InnerContainer from './InnerContainer'
import { EVENT_TYPES, VIEW_TYPE } from './constants'
import { logError, logEventDebug, logEventDev } from './utils/util'
const { StoreLinkModule } = NativeModules

class CooklistSDKWrapper extends React.Component {

  state = {
    loading: true,
  }

  UNSAFE_componentWillMount() {
    this.initialSetup()
  }

  initialSetup = async () => {
    try {
      const { viewType, logLevel, refreshToken } = this.props
      const [sdkResponse] = await Promise.all([
        CooklistSDK.configure({
          refreshToken,
          onStoreConnectionEvent: this.onStoreConnectionEvent,
          onInvoiceEvent: this.onInvoiceEvent,
          onCheckingStoreConnectionEvent: this.onCheckingStoreConnectionEvent,
          _backgroundDisabled: viewType !== VIEW_TYPE.BACKGROUND_TASK,
          _logLevel: logLevel,
        }),
      ])
      if (sdkResponse.success) {
        this.setState({ loading: false })
        logEventDebug(this.props.logLevel, 'SDK initialized!')
      } else {
        logEventDebug(this.props.logLevel, `Error initializing CooklistSDK: ${sdkResponse.message}`)
      }
    } catch (error) {
      logError(error)
    }
  }

  onStoreConnectionEvent = ({ storeConnectionId, credentialsStatus }) => {
    StoreLinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_EVENT, {
      functionName: 'onStoreConnectionEvent',
      storeConnectionId,
      credentialsStatus,
    })
    logEventDev(this.props.logLevel, `[REACT NATIVE] onStoreConnectionEvent:`, { storeConnectionId, credentialsStatus })
  }

  onInvoiceEvent = ({ storeConnectionId, orderIds }) => {
    StoreLinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_EVENT, {
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
    StoreLinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_EVENT, {
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
      StoreLinkModule.sendNotification(EVENT_TYPES.COOKLIST_SDK_VIEW_COMPLETE_EVENT, {
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
      <CooklistSDK.Provider>
        <InnerContainer
          viewType={viewType}
          functionParams={functionParams}
          onViewComplete={this.onViewComplete}
        />
      </CooklistSDK.Provider>
    )
  }
}

export default CooklistSDKWrapper