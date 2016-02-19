#!/bin/sh

./gradlew bintrayUpload -Pbintray.user="$BINTRAY_USER" -Pbintray.apikey="$BINTRAY_API_KEY" -Pbintray.gpg.password="$BINTRAY_GPG_PASSWORD"
