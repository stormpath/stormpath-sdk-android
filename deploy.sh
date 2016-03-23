#!/bin/sh

./gradlew :sdk:bintrayUpload -Pbintray.user="$BINTRAY_USER" -Pbintray.apikey="$BINTRAY_API_KEY" -Pbintray.gpg.password="$BINTRAY_GPG_PASS"
./gradlew :sdk-ui:bintrayUpload -Pbintray.user="$BINTRAY_USER" -Pbintray.apikey="$BINTRAY_API_KEY" -Pbintray.gpg.password="$BINTRAY_GPG_PASS"