import React from 'react'
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native'
import CooklistSDK from 'react-native-cooklist'

// Walmart
const STORE_ID = "U3RvcmVOb2RlOjM="
const STORE_NAME = "Walmart"

class InnerContainer extends React.Component {

  renderContent = () => {
    return (
      <CooklistSDK.Consumer>
        {({
          connectOrUpdateStoreConnection,
        }) => (
          <>
            <TouchableOpacity
              style={styles.button}
              onPress={() => connectOrUpdateStoreConnection({ storeId: STORE_ID })}>
              <Text>{`Connect`} {STORE_NAME}</Text>
            </TouchableOpacity>
          </>
        )}
      </CooklistSDK.Consumer>
    )
  }

  render() {
    return (
      <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
        <Text>CooklistSDK Wrapper</Text>
        {this.renderContent()}
      </View>
    )
  }
}

export default InnerContainer

const styles = StyleSheet.create({
  button: {
    padding: 5,
    borderWidth: 1,
    borderRadius: 6,
    margin: 10,
  },
})