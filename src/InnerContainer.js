import _ from 'lodash'
import React from 'react'
import {
  NativeEventEmitter,
  NativeModules,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native'
import { compose } from 'recompose'
import { EVENT_TYPES, VIEW_TYPE } from './constants'
import { withCooklistSDKConsumer } from './utils/hoc'
import { logEventDev, logEventDebug, logError } from './utils/util'
const { StoreLinkModule } = NativeModules

// Walmart
const STORE_ID = "U3RvcmVOb2RlOjM="
const STORE_NAME = "Walmart"

class InnerContainer extends React.Component {

  render() {
    const { viewType } = this.props
    if (viewType === VIEW_TYPE.CONNECT_UPDATE_STORE) {
      return <ConnectStoreContainerView {...this.props} />
    }
    // if (viewType === VIEW_TYPE.STORE_CONNECTIONS_LIST) {
    //   return <StoreConnectionsListContainerView {...this.props} />
    // }
    if (viewType === VIEW_TYPE.BACKGROUND_TASK) {
      return <BackgroundContainerView {...this.props} />
    }
    return null
  }
}

class ConnectStoreContainerView extends React.Component {

  state = {
    flowDisplayed: false,
  }

  onViewComplete = (payload) => {
    try {
      if (this.props.onViewComplete) {
        this.props.onViewComplete(payload)
      }
      this.setState({ flowDisplayed: true })
    } catch (error) {
      logEventDev(this.props.logLevel, error)
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

class BackgroundContainerViewPure extends React.Component {

  eventEmitter = null

  componentDidMount() {
    this.startListeningForEvents()
  }

  componentWillUnmount() {
    this.eventEmitter.removeAllListeners('CooklistDataFromNative')
  }

  startListeningForEvents = () => {
    try {
      this.eventEmitter = new NativeEventEmitter(StoreLinkModule)
      logEventDebug(this.props.logLevel, '[REACT NATIVE] Listening for CooklistDataFromNative')
      this.eventEmitter.addListener('CooklistDataFromNative', data => {
        logEventDebug(this.props.logLevel, '[REACT NATIVE]', { CooklistDataFromNative: data })
        if (
          _.get(data, '_cooklistInternal')
          && _.get(data, 'eventType') === EVENT_TYPES.COOKLIST_SDK_EVENT_EAGER_CHECK_BG_PURCHASES
          && _.get(data, 'storeId')
        ) {
          this.props.checkStoreConnectionForInvoicesFullCrawl({ storeId: _.get(data, 'storeId') })
        }
      })
    } catch (error) {
      logError(error)
    }
  }

  render() {
    return null
  }
}

const BackgroundContainerView = compose(
  withCooklistSDKConsumer,
)(BackgroundContainerViewPure)

export default InnerContainer

const styles = StyleSheet.create({
  button: {
    padding: 5,
    borderWidth: 1,
    borderRadius: 6,
    margin: 10,
  },
})