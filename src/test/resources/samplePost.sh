#!/usr/bin/env bash
curl -v -X POST -H "Content-Type: application/json" --data @x509.json http://localhost:8080/api/healthChecks
