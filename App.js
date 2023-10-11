import React from 'react';
import {View, Text, NativeModules} from 'react-native';
import {WebView} from 'react-native-webview';
const {StoreConnectionModule} = NativeModules;

export default class App extends React.Component {
  componentDidMount() {
    StoreConnectionModule.onStoreConnected('someStatus');
  }

  render() {
    return (
      <View
        style={{
          flex: 1,
        }}>
        <WebView source={{uri: 'https://cooklist.com'}} style={{flex: 1}} />
        <Text>Test webview</Text>
      </View>
    );
  }
}
