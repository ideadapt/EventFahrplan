# docker build -t eventfahrplan-oauth --file apache.Dockerfile .
FROM php:8.2-apache

RUN a2enmod rewrite \
    && a2enmod proxy \
    && a2enmod proxy_http \
    && a2enmod ssl
# Allow .htaccess overrides
# RUN sed -ri 's/AllowOverride None/AllowOverride All/g' /etc/apache2/apache2.conf

WORKDIR ${APACHE_DOCUMENT_ROOT}
