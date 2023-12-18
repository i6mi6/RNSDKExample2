import React from 'react'
import { ActivityIndicator, NativeModules, View } from 'react-native'
import CooklistSDK from 'react-native-cooklist'
import InnerContainer from './InnerContainer'
import { VIEW_TYPE } from './constants'
const { StoreConnectionModule } = NativeModules

class CooklistSDKWrapper extends React.Component {

  // eventEmitter = null
  state = {
    loading: true,
  }

  UNSAFE_componentWillMount() {
    // this.startListeningForEvents()
    this.initialSetup()
  }

  // componentWillUnmount() {
  //   this.eventEmitter.removeAllListeners('DataFromNative')
  // }

  // startListeningForEvents = () => {
  //   this.eventEmitter = new NativeEventEmitter(StoreConnectionModule)
  //   console.log('[REACT NATIVE] Listening for DataFromNative')
  //   this.eventEmitter.addListener('DataFromNative', data => {
  //     console.log('[REACT NATIVE]', { dataFromNative: data })
  //   })
  // }

  initialSetup = async () => {
    try {
      const { viewType, refreshToken } = this.props
      const [sdkResponse] = await Promise.all([
        CooklistSDK.configure({
          refreshToken,
          onStoreConnectionEvent: this.onStoreConnectionEvent,
          onInvoiceEvent: this.onInvoiceEvent,
          onCheckingStoreConnectionEvent: this.onCheckingStoreConnectionEvent,
          backgroundDisabled: viewType !== VIEW_TYPE.BACKGROUND_TASK,
        }),
      ])
      if (sdkResponse.success) {
        this.setState({ loading: false })
        console.log('SDK initialized!')
      } else {
        console.log(`Error initializing CooklistSDK: ${sdkResponse.message}`)
      }
    } catch (error) {
      console.log(error)
    }
  }

  onStoreConnectionEvent = ({ storeConnectionId, credentialsStatus }) => {
    StoreConnectionModule.sendNotification('cooklist_sdk_event', {
      functionName: 'onStoreConnectionEvent',
      storeConnectionId,
      credentialsStatus,
    })
    console.log(`[REACT NATIVE] onStoreConnectionEvent:`, { storeConnectionId, credentialsStatus })
  }

  onInvoiceEvent = ({ storeConnectionId, orderIds }) => {
    StoreConnectionModule.sendNotification('cooklist_sdk_event', {
      functionName: 'onInvoiceEvent',
      storeConnectionId,
      orderIds,
    })
    console.log(`[REACT NATIVE] onInvoiceEvent:`, {
      storeConnectionId,
      orderIds,
    })
  }

  onCheckingStoreConnectionEvent = (payload) => {
    StoreConnectionModule.sendNotification('cooklist_sdk_event', {
      functionName: 'onCheckingStoreConnectionEvent',
      ...(payload || {}),
    })
    console.log(`[REACT NATIVE] onCheckingStoreConnectionEvent:`, payload)
  }

  render() {
    const { viewType, functionParams } = this.props
    const { loading } = this.state
    console.log('[REACT NATIVE] props:', this.props)
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
        <InnerContainer viewType={viewType} functionParams={functionParams} />
      </CooklistSDK.Provider>
    )
  }
}

export default CooklistSDKWrapper