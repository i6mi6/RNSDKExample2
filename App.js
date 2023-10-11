import React from 'react';
import {View, Text, NativeModules, TouchableOpacity} from 'react-native';
import {WebView} from 'react-native-webview';
const {StoreConnectionModule} = NativeModules;

export default class App extends React.Component {
  componentDidMount() {
    // StoreConnectionModule.onStoreConnected('someStatus');
    // setTimeout(() => {
    //   const params = {rootTag: this.props.rootTag, userId: '525'};
    //   StoreConnectionModule.sendNotification('openUserScreen', params);
    // }, 1000);
  }

  onConnect = () => {
    const params = {
      // rootTag: this.props.rootTag,
      credentialsStatus: 'Verified',
      storeId: 3,
      userId: 525,
    };
    StoreConnectionModule.sendNotification('openUserScreen', params);
  };

  onError = () => {
    const params = {
      // rootTag: this.props.rootTag,
      credentialsStatus: 'Invalid',
      storeId: 3,
      userId: null,
    };
    StoreConnectionModule.sendNotification('openUserScreen', params);
  };

  render() {
    return (
      <View
        style={{
          flex: 1,
          alignItems: 'center',
          justifyContent: 'center',
        }}>
        {/* <WebView source={{uri: 'https://cooklist.com'}} style={{flex: 1}} /> */}
        <Text>Inside React Native!</Text>
        <TouchableOpacity
          onPress={this.onConnect}
          style={{padding: 10, backgroundColor: 'green'}}>
          <Text style={{color: '#fff'}}>Tap to Connect Store</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={this.onError}
          style={{padding: 10, backgroundColor: 'red'}}>
          <Text style={{color: '#fff'}}>Tap for Invalid Creds</Text>
        </TouchableOpacity>
      </View>
    );
  }
}
