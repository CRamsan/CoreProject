#!/bin/bash

ARTIFACT_FILE="service-all.jar"
ARTIFACT_FOLDER="build/libs"

INSTALLATION_FOLDER="$HOME/.local/ps2link"

PROPERTIES_FILE="override.conf"
SYSTEMD_FILE="ps2link.service"
SYSTEMD_FOLDER="$HOME/.config/systemd/user"

echo "Building Server Executable"
sleep 2
./gradlew buildFatJar

echo "Creating $INSTALLATION_FOLDER"
mkdir -p $INSTALLATION_FOLDER

touch $INSTALLATION_FOLDER/$PROPERTIES_FILE

export XDG_RUNTIME_DIR=/run/user/$(id -u $USER)
export DBUS_SESSION_BUS_ADDRESS=/run/user/$(id -u $USER)/bus

echo "Stopping server..."
systemctl --user stop $SYSTEMD_FILE

echo "Copying file $ARTIFACT_FOLDER/$ARTIFACT_FILE to $INSTALLATION_FOLDER/$ARTIFACT_FILE"
cp $ARTIFACT_FOLDER/$ARTIFACT_FILE $INSTALLATION_FOLDER/$ARTIFACT_FILE

echo "Installing systemd unit $SYSTEMD_FILE in $SYSTEMD_FOLDER/$SYSTEMD_FILE"
mkdir -p $SYSTEMD_FOLDER
cp -f $SYSTEMD_FILE $SYSTEMD_FOLDER/$SYSTEMD_FILE

echo "Starting server"
systemctl --user daemon-reload
systemctl --user restart $SYSTEMD_FILE
systemctl --user enable $SYSTEMD_FILE