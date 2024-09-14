#!/bin/sh

aws --endpoint-url=http://localhost:4566 --region=eu-central-1 \
    secretsmanager create-secret \
    --name weather/api/credentials \
    --description "Weather API related Credentials" \
    --secret-string file://secrets.json
