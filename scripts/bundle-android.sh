if [ "${OS_NAME}" = "linux" ]; then
  HERMES=./node_modules/hermes-engine/linux64-bin/hermes
else
  HERMES=./node_modules/hermes-engine/osx-bin/hermes
fi

CLI_PATH=./node_modules/react-native/local-cli/cli.js
COMPOSE_SOURCE_MAPS_PATH=./node_modules/react-native/scripts/compose-source-maps.js

INTEGRATION_MODULE_FOLDER=android/RNSDK/src/main
BUNDLE_NAME=rnsdk.jsbundle

if [ -d "${INTEGRATION_MODULE_FOLDER}/assets" ]; then
  rm -rf $INTEGRATION_MODULE_FOLDER/assets
fi
mkdir $INTEGRATION_MODULE_FOLDER/assets

if [ -d "${INTEGRATION_MODULE_FOLDER}/res" ]; then
  rm -rf $INTEGRATION_MODULE_FOLDER/res
fi
mkdir $INTEGRATION_MODULE_FOLDER/res

# Create bundle
node $CLI_PATH bundle \
  --entry-file ./index.js \
  --platform android \
  --dev false \
  --bundle-output $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME \
  --assets-dest $INTEGRATION_MODULE_FOLDER/res \
  --sourcemap-output $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME.packager.map

# Create hermes bundle
$HERMES \
  -emit-binary \
  -out $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME.hbc $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME \
  -O -output-source-map

# # Map packages
mv \
  $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME.hbc \
  $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME
mv \
  $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME.hbc.map \
  $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME.compiler.map
# node \
#   $COMPOSE_SOURCE_MAPS_PATH \
#   $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME.packager.map \
#   $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME.compiler.map \
#   -o $INTEGRATION_MODULE_FOLDER/assets/$BUNDLE_NAME.map

# # Remove map files
find $INTEGRATION_MODULE_FOLDER/assets -name "*.map" -type f -delete

# Remove json files
find $INTEGRATION_MODULE_FOLDER/res -name "*.json" -type f -delete