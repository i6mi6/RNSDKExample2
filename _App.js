import React from 'react'
import {
  View,
  Text,
  NativeModules,
  TouchableOpacity,
  NativeEventEmitter,
} from 'react-native'
import householdPurchases from './householdPurchases.json'
const { StoreConnectionModule } = NativeModules

export default class App extends React.Component {
  state = {
    event: null,
  };

  componentDidMount() {
    this.eventEmitter = new NativeEventEmitter(StoreConnectionModule)
    this.eventEmitter.addListener('DataFromNative', data => {
      console.log(data)
      this.setState({
        event: JSON.stringify(data),
      })
    })
  }

  componentWillUnmount() {
    this.eventEmitter.removeAllListeners('DataFromNative')
  }

  onConnect = () => {
    const params = {
      // rootTag: this.props.rootTag,
      credentialsStatus: 'Verified',
      storeId: 3,
      userId: 525,
      payload: householdPurchases,
    }
    StoreConnectionModule.sendNotification('cooklist_sdk_event', params)
  };

  onError = () => {
    const params = {
      // rootTag: this.props.rootTag,
      credentialsStatus: 'Invalid',
      storeId: 3,
      userId: null,
    }
    StoreConnectionModule.sendNotification('cooklist_sdk_event', params)
  };

  render() {
    const { event } = this.state
    return (
      <View
        style={{
          flex: 1,
          alignItems: 'center',
          justifyContent: 'center',
        }}>
        {/* <WebView source={{uri: 'https://cooklist.com'}} style={{flex: 1}} /> */}
        <Text>Inside React Native!</Text>
        <Text>{`Event: ${event}`}</Text>
        <TouchableOpacity
          onPress={this.onConnect}
          style={{ padding: 10, backgroundColor: 'green' }}>
          <Text style={{ color: '#fff' }}>Tap to Connect Store</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={this.onError}
          style={{ padding: 10, backgroundColor: 'red' }}>
          <Text style={{ color: '#fff' }}>Tap for Invalid Creds</Text>
        </TouchableOpacity>
      </View>
    )
  }
}
