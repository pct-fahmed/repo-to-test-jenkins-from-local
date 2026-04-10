#!/bin/sh
set -eu

DEPLOY_ENV="${1:?deployment environment is required}"
IMAGE_REF="${2:?image reference is required}"

if [ -z "${DEPLOY_COMMAND:-}" ]; then
    echo "DEPLOY_COMMAND is not set. Refusing to fake a deployment for ${IMAGE_REF} to ${DEPLOY_ENV}." >&2
    exit 1
fi

echo "Deploying ${IMAGE_REF} to ${DEPLOY_ENV}"
sh -c "${DEPLOY_COMMAND}"
