import React, { useState, useEffect } from 'react'
import { ActivityIndicator } from 'react-native'
import InnerContainer from './InnerContainer'
import CooklistSDK from 'react-native-cooklist'

export default function CooklistSDKWrapper() {

  const [loading, setLoading] = useState(true)

  useEffect(() => {
    initialSetup()
  }, [])

  const initialSetup = async () => {
    try {
      const refreshToken = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo2ODMwOTIsIm9pZCI6MywidmVyc2lvbiI6MSwianRpIjoiOGFhOGVmZDQtNDlmMS00ZWQyLTlkMTUtOWVhMDE2ODBmYzU1IiwiaXNzdWVkX2F0IjoxNzAyMzk2MTExLjM4NzIyOSwiZXhwaXJlc19hdCI6MTczMzkzMjExMS4zODcyMjksInRva2VuX3R5cGUiOiJyZWZyZXNoIn0.hI3sT7YJq-dGBCXiDClTFWH3Sbc0pLPya1M_2OUTl1E'

      const [sdkResponse] = await Promise.all([
        CooklistSDK.configure({
          refreshToken,
          onStoreConnectionEvent,
          onInvoiceEvent,
          onCheckingStoreConnectionEvent,
        }),
      ])
      if (sdkResponse.success) {
        setLoading(false)
        console.log('SDK initialized!')
      } else {
        console.log(`Error initializing CooklistSDK: ${sdkResponse.message}`)
      }
    } catch (error) {
      console.log(error)
    }
  }

  const onStoreConnectionEvent = ({ storeConnectionId, credentialsStatus }) => {
    console.log(`onStoreConnectionEvent:`, { storeConnectionId, credentialsStatus })
  }

  const onInvoiceEvent = ({ storeConnectionId, orderIds }) => {
    console.log(`onInvoiceEvent:`, { storeConnectionId, orderIds })
  }

  const onCheckingStoreConnectionEvent = (payload) => {
    console.log(`onCheckingStoreConnectionEvent:`, payload)
  }

  if (loading) {
    return <ActivityIndicator />
  }

  return (
    <CooklistSDK.Provider>
      <InnerContainer />
    </CooklistSDK.Provider>
  )
}
