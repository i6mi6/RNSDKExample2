import React from 'react'
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native'
import CooklistSDK from 'react-native-cooklist'
import { VIEW_TYPE } from './constants'
import { withCooklistSDKConsumer } from './utils/hoc'
import { compose } from 'recompose'
import _ from 'lodash'

// Walmart
const STORE_ID = "U3RvcmVOb2RlOjM="
const STORE_NAME = "Walmart"

class InnerContainer extends React.Component {

  render() {
    const { viewType } = this.props
    if (viewType === VIEW_TYPE.CONNECT_UPDATE_STORE) {
      return <ConnectStoreContainer {...this.props} />
    }
    if (viewType === VIEW_TYPE.STORE_CONNECTIONS_LIST) {
      return <StoreConnectionsListContainer {...this.props} />
    }
    return null
  }
}

class ConnectStoreContainerPure extends React.Component {

  componentDidMount() {
    if (_.get(this.props, 'functionParams.storeId')) {
      this.props.connectOrUpdateStoreConnection({ storeId: this.props.functionParams.storeId })
    }
  }

  render() {
    return null
  }
}

const ConnectStoreContainer = compose(
  withCooklistSDKConsumer,
)(ConnectStoreContainerPure)

class StoreConnectionsListContainer extends React.Component {

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
      <View style={{ flex: 1 }}>
        <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
          <Text>CooklistSDK Wrapper</Text>
          {this.renderContent()}
        </View>
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