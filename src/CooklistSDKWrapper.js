import React from 'react'
import { ActivityIndicator, NativeEventEmitter, NativeModules, Text, View } from 'react-native'
import CooklistSDK from 'react-native-cooklist'
import { GestureHandlerRootView } from 'react-native-gesture-handler'
import InnerContainer from './InnerContainer'
const { StoreConnectionModule } = NativeModules

class CooklistSDKWrapper extends React.Component {

  eventEmitter = null
  state = {
    loading: true,
  }

  UNSAFE_componentWillMount() {
    this.startListeningForEvents()
    // this.initialSetup()
  }

  componentWillUnmount() {
    this.eventEmitter.removeAllListeners('DataFromNative')
  }

  startListeningForEvents = () => {
    this.eventEmitter = new NativeEventEmitter(StoreConnectionModule)
    console.log('[REACT NATIVE] Listening for DataFromNative')
    this.eventEmitter.addListener('DataFromNative', data => {
      console.log('[REACT NATIVE]', { dataFromNative: data })
    })
  }

  // initialSetup = async () => {
  //   try {
  //     const refreshToken = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo2ODMwOTIsIm9pZCI6MywidmVyc2lvbiI6MSwianRpIjoiOGFhOGVmZDQtNDlmMS00ZWQyLTlkMTUtOWVhMDE2ODBmYzU1IiwiaXNzdWVkX2F0IjoxNzAyMzk2MTExLjM4NzIyOSwiZXhwaXJlc19hdCI6MTczMzkzMjExMS4zODcyMjksInRva2VuX3R5cGUiOiJyZWZyZXNoIn0.hI3sT7YJq-dGBCXiDClTFWH3Sbc0pLPya1M_2OUTl1E'

  //     const [sdkResponse] = await Promise.all([
  //       CooklistSDK.configure({
  //         refreshToken,
  //         onStoreConnectionEvent: this.onStoreConnectionEvent,
  //         onInvoiceEvent: this.onInvoiceEvent,
  //         onCheckingStoreConnectionEvent: this.onCheckingStoreConnectionEvent,
  //       }),
  //     ])
  //     if (sdkResponse.success) {
  //       this.setState({ loading: false })
  //       console.log('SDK initialized!')
  //     } else {
  //       console.log(`Error initializing CooklistSDK: ${sdkResponse.message}`)
  //     }
  //   } catch (error) {
  //     console.log(error)
  //   }
  // }

  onStoreConnectionEvent = ({ storeConnectionId, credentialsStatus }) => {
    console.log(`onStoreConnectionEvent:`, { storeConnectionId, credentialsStatus })
  }

  onInvoiceEvent = ({ storeConnectionId, orderIds }) => {
    console.log(`onInvoiceEvent:`, { storeConnectionId, orderIds })
  }

  onCheckingStoreConnectionEvent = (payload) => {
    console.log(`onCheckingStoreConnectionEvent:`, payload)
  }

  render() {
    const { loading } = this.state
    console.log('[REACT NATIVE] props:', this.props)
    if (loading) {
      return (
        <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
          <Text>{this.props.refreshToken}</Text>
          <ActivityIndicator />
        </View>
      )
    }
    return (
      <GestureHandlerRootView style={{ flex: 1 }}>
        <CooklistSDK.Provider>
          <InnerContainer />
        </CooklistSDK.Provider>
      </GestureHandlerRootView>
    )
  }
}

export default CooklistSDKWrapper