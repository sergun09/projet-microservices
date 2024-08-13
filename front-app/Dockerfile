FROM php:8.1.1-fpm-alpine
ARG USERNAME
ARG UID

# installation bash
RUN apk --no-cache update && apk --no-cache add bash git && apk --no-cache add npm && apk --no-cache add nodejs

# installation de Angular
RUN npm install -g typescript  && npm install -g @angular/cli@15.1.4

# gestion utilisateur
RUN adduser -h /home/$USERNAME -D -s /bin/bash -u $UID $USERNAME
USER $USERNAME

WORKDIR /var/www/html