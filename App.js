import React from 'react';
import {View, Text, NativeModules} from 'react-native';
import {WebView} from 'react-native-webview';
const {StoreConnectionModule} = NativeModules;

export default class App extends React.Component {
  componentDidMount() {
    StoreConnectionModule.onStoreConnected('someStatus');
    setTimeout(() => {
      const params = {rootTag: this.props.rootTag, userId: '525'};
      StoreConnectionModule.sendNotification('openUserScreen', params);
    }, 1000);
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
