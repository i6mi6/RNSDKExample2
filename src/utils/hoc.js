import React from 'react'
import CooklistSDK from 'react-native-cooklist'

export const withCooklistSDKConsumer = (WrappedComponent) => {
  return class extends React.Component {
    render() {
      <CooklistSDK.Consumer>
        {(params) => (
          <>
            <WrappedComponent
              {...this.props}
              {...params}/>
          </>
        )}
      </CooklistSDK.Consumer>
    }
  }
}