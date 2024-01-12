import React from 'react'
import Storelink from 'react-native-storelink'

export const withStorelinkConsumer = (WrappedComponent) => {
  return class extends React.Component {
    render() {
      return (
        <Storelink.Consumer>
          {(params) => (
            <>
              <WrappedComponent
                {...this.props}
                {...params}/>
            </>
          )}
        </Storelink.Consumer>
      )
    }
  }
}