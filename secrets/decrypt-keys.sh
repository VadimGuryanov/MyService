#!/bin/bash
if [ -z "$1" ]; then
    echo 'You must provide secret key as parameter' >&2
    exit 2
else
    openssl aes-256-cbc -d -md sha256 -nosalt -a -pass pass:$1 -in secrets/keys.properties.crypted -out secrets/keys.properties
    openssl aes-256-cbc -d -md sha256 -nosalt -a -pass pass:$1 -in secrets/google-services.crypted -out app/google-services.json
fi