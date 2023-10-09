CLI_PATH=./node_modules/react-native/local-cli/cli.js

INTEGRATION_MODULE_FOLDER=ios/SDKOutput/Rescources
BUNDLE_NAME=rnsdk.jsbundle

if [ -d "${INTEGRATION_MODULE_FOLDER}" ]; then
  rm -rf $INTEGRATION_MODULE_FOLDER
fi
mkdir $INTEGRATION_MODULE_FOLDER

# Create bundle
node $CLI_PATH ram-bundle \
  --entry-file ./index.js \
  --platform ios \
  --dev false \
  --bundle-output $INTEGRATION_MODULE_FOLDER/$BUNDLE_NAME \
  --assets-dest $INTEGRATION_MODULE_FOLDER

# Remove json files
find $INTEGRATION_MODULE_FOLDER -name "*.json" -type f -delete