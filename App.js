import React from 'react';
import {View} from 'react-native';
import FastImage from 'react-native-fast-image';

export default () => {
  return (
    <View
      style={{
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#ffffff',
      }}>
      <FastImage
        style={{width: 326, height: 348}}
        source={{
          uri: 'https://cooklist.com/images/logo-desktop.png',
        }}
        resizeMode={FastImage.resizeMode.contain}
      />
    </View>
  );
};
