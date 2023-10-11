import React from 'react';
import {View, Text} from 'react-native';
import {WebView} from 'react-native-webview';

export default () => {
  return (
    <View
      style={{
        flex: 1,
      }}>
      <WebView source={{uri: 'https://cooklist.com'}} style={{flex: 1}} />
      <Text>Test webview</Text>
    </View>
  );
};
