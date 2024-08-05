export const LOG_LEVEL = {
  NONE: 1,
  DEBUG: 2,
  DEV: 3,
}

export const logEventDebug = (logLevel, ...args) => {
  console.log(...args)
  if (logLevel && logLevel >= LOG_LEVEL.DEBUG) {
    console.log(...args)
  }
}

export const logEventDev = (logLevel, ...args) => {
  console.log(...args)
  if (logLevel && logLevel >= LOG_LEVEL.DEV) {
    console.log(...args)
  }
}

export const logError = (logLevel, error) => {
  console.log(error)
  if (logLevel && logLevel > LOG_LEVEL.NONE) {
    console.log(error)
  }
}