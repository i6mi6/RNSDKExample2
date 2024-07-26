DIR=$PWD
RN_AAR=$DIR/node_modules/react-native/android/com/facebook/react/react-native/0.72.6/react-native-0.72.6.aar
HERMES_AAR=$DIR/node_modules/hermes-engine/android/hermes-release.aar

BUILD_LIBS=$DIR/android/build/libs
OUTPUT=$DIR/android/SDKOutput

rm -rf $OUTPUT
mkdir $OUTPUT
mkdir $OUTPUT/libs

cd android
./gradlew clean -x app:assembleRelease assembleRelease
./gradlew createGradleFile
cd -

cd $BUILD_LIBS
find . -name "*-release.aar" | while read f; do
  echo "$f"
  cp $f $OUTPUT/libs
done
cd -

cp $RN_AAR $OUTPUT/libs
cp $HERMES_AAR $OUTPUT/libs