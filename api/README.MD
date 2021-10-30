## .:: Dev3 Auctions ::.

# Warning
Before you begin to configure API on server, check timestamp machine.

## Set timezone with `timedatectl`
``sudo timedatectl set-timezone America/Sao_Paulo``

## Set timezone Mysql
``SELECT @@global.time_zone;``
``SET GLOBAL time_zone = '-3:00';``
``SET GLOBAL time_zone = 'America/Sao_Paulo';``
``SET @@global.time_zone = '-03:00';``

``SELECT @@session.time_zone;``
``SET time_zone = 'America/Sao_Paulo';``
``SET time_zone = "-03:00";``
``SET @@session.time_zone = "-03:00";``

# Mysql scheduler
## check
``SHOW GLOBAL VARIABLES LIKE 'event_scheduler';``
## Activate
``SET GLOBAL event_scheduler = ON;``


# Environment configurations
## .env files
create `.env.development` with configurations for development (local)
create `.env.production` with configurations for production (server)

## Before run
install packages: `npm install`

### Running project
Local: `npm run dev`

# Release project to server
`npm run build`

# Start project on server
`npm run start:server`

# Config '.env.*' file
- duplicate '.env.sample' file, renaming to '.env' and add values like below

```
    PORT='' #Default port to run server 
    SECRET='' #secret for jwt
    DEFAULT_PASS='' #default pass to login

    # Database configs
    USERNAME=''
    PASSWORD=''
    DATABASE=''
    HOST=''

    #E-mail config
    MAIL_HOST='mail.dev3s.com.br'
    MAIL_PORT='465'
    SERVICE='gmail' #if use gmail service
    USER_MAIL=''
    USER_PASS=''
    TZ= #timezone
    #E-mail test config
    MAIL_TEST_FROM=''
    MAIL_TEST_TO=''
    VERSION=
```

# Initial base structure folders
```
.
├── README.MD
├── app.js
├── pm2.config.js
├── release.sh
├── .env.sample
├── assets
│   └── auctions
│       └── thumb
├── emails
│   └── register
│       ├── html.pug
│       └── style.css
├── package.json
├── public
│   ├── images
│   ├── javascripts
│   └── stylesheets
├── routes
├── src
│   ├── config
│   │   └── database.js
│   ├── modules
│   │   ├── auctions
│   │   │   ├── auctions.controller.js
│   │   │   ├── auctions.model.js
│   │   │   └── auctions.routes.js
│   │   ├── auth
│   │   │   ├── auth.controller.js
│   │   │   └── auth.routes.js
│   │   └── users
│   │       ├── users.controller.js
│   │       ├── users.model.js
│   │       └── users.routes.js
│   └── utils
│       ├── constants.js
│       ├── helper.js
│       ├── http_headers.js
│       ├── isAuth.js
│       ├── paginatedResults.js
│       ├── response.js
│       └── sendMail.js

```

# E-mail templates
```
.
├── emails
│   └── register
│       ├── html.pug
│       └── style.css
```

#### Observations
- gm lib depends graphicsmagick
- MacOs installation 'brew install graphicsmagick'
- linux: https://stackoverflow.com/questions/31214724/gm-conversion-issue-in-node-js

# LOGS
Logs on the server should be written on a file and without pino-pretty to not impact performance
pino-pretty should be used only to analyze logs
Example:
    cat logs/app.log | pino-pretty -t 'SYS:standard'