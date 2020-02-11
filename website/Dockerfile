FROM node:10-alpine
RUN mkdir -p /home/node/app/node_modules && chown -R node:node /home/node/app
ENV APP_HOME=/home/node/app
WORKDIR $APP_HOME
COPY package*.json google_key.json ./
COPY home.html /home/node/app/
COPY googlestyle.css /home/node/app/
USER node
RUN npm install
COPY --chown=node:node . .
EXPOSE 9090
CMD [ "ls" ]
CMD [ "node", "app.js" ]