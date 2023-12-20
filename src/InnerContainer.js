import _ from 'lodash'
import React from 'react'
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native'
import { compose } from 'recompose'
import { VIEW_TYPE } from './constants'
import { withCooklistSDKConsumer } from './utils/hoc'
import { logLevelDev } from './utils/util'

// Walmart
const STORE_ID = "U3RvcmVOb2RlOjM="
const STORE_NAME = "Walmart"

class InnerContainer extends React.Component {

  render() {
    const { viewType } = this.props
    if (viewType === VIEW_TYPE.CONNECT_UPDATE_STORE) {
      return <ConnectStoreContainerView {...this.props} />
    }
    if (viewType === VIEW_TYPE.STORE_CONNECTIONS_LIST) {
      return <StoreConnectionsListContainerView {...this.props} />
    }
    return null
  }
}

class ConnectStoreContainerView extends React.Component {

  state = {
    flowDisplayed: false,
  }

  onViewComplete = () => {
    try {
      if (this.props.onViewComplete) {
        this.props.onViewComplete()
      }
      this.setState({ flowDisplayed: true })
    } catch (error) {
      logLevelDev(this.props.logLevel, error)
    }
  }

  render() {
    const { flowDisplayed } = this.state
    if (flowDisplayed) {
      return null
    }
    return (
      <ConnectStoreInnerContainer
        {...this.props}
        onComplete={this.onViewComplete}/>
    )
  }
}

class ConnectStoreInnerContainerPure extends React.Component {

  componentDidMount() {
    if (_.get(this.props, 'functionParams.storeId')) {
      this.props.connectOrUpdateStoreConnection({
        storeId: this.props.functionParams.storeId,
        onComplete: this.props.onComplete,
      })
    }
  }

  render() {
    return null
  }
}

const ConnectStoreInnerContainer = compose(
  withCooklistSDKConsumer,
)(ConnectStoreInnerContainerPure)

class StoreConnectionsListContainerViewPure extends React.Component {

  renderContent = () => {
    return (
      <TouchableOpacity
        style={styles.button}
        onPress={() => this.props.connectOrUpdateStoreConnection({ storeId: STORE_ID })}>
        <Text>{`Connect`} {STORE_NAME}</Text>
      </TouchableOpacity>
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

const StoreConnectionsListContainerView = compose(
  withCooklistSDKConsumer,
)(StoreConnectionsListContainerViewPure)

export default InnerContainer

const styles = StyleSheet.create({
  button: {
    padding: 5,
    borderWidth: 1,
    borderRadius: 6,
    margin: 10,
  },
})