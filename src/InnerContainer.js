import _ from 'lodash'
import React from 'react'
import {
  NativeEventEmitter,
  NativeModules,
} from 'react-native'
import { compose } from 'recompose'
import { EVENT_TYPES, VIEW_TYPE } from './constants'
import { withStorelinkConsumer } from './utils/hoc'
import { logError, logEventDebug, logEventDev } from './utils/util'
const { StorelinkModule } = NativeModules

class InnerContainer extends React.Component {

  render() {
    const { viewType } = this.props
    if (viewType === VIEW_TYPE.CONNECT_UPDATE_STORE) {
      return <ConnectStoreContainerView {...this.props} />
    }
    if (viewType === VIEW_TYPE.TRANSFER_CART) {
      return <TransferCartContainerView {...this.props} />
    }
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
  withStorelinkConsumer,
)(ConnectStoreInnerContainerPure)


class TransferCartContainerView extends React.Component {

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
      <TransferCartInnerContainer
        {...this.props}
        onComplete={this.onViewComplete}/>
    )
  }
}

class TransferCartInnerContainerPure extends React.Component {

  componentDidMount() {
    if (_.get(this.props, 'functionParams.cartId')) {
      this.props.transferShoppingCart({
        cartId: this.props.functionParams.cartId,
        onComplete: this.props.onComplete,
      })
    }
  }

  render() {
    return null
  }
}

const TransferCartInnerContainer = compose(
  withStorelinkConsumer,
)(TransferCartInnerContainerPure)

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
      this.eventEmitter = new NativeEventEmitter(StorelinkModule)
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
  withStorelinkConsumer,
)(BackgroundContainerViewPure)

export default InnerContainer