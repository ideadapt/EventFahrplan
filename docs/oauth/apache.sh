#!/usr/bin/env bash

podman run --rm -v $(pwd):/var/www/html:z -v $(pwd)/site-default.conf:/etc/apache2/sites-available/000-default.conf:z --name apache2-container -e TZ=UTC -p 8081:80 eventfahrplan-oauth
