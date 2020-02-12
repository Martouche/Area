FROM node:10-alpine
#RUN mkdir -p /home/node/app/node_modules && chown -R node:node /home/node/app
#ENV APP_HOME=/home/node/app
WORKDIR /home/node/app
#COPY package*.json google_key.json ./
#COPY home.html /home/node/app/
#COPY /vendor /home/node/app/
#COPY /static/main.css /home/node/app/
#COPY /static/util.css /home/node/app/
RUN ["ls"]
RUN ["pwd"]
#COPY /fonts /home/node/app/
#COPY /js /home/node/app/
#COPY googlestyle.css /home/node/app/
#USER node
COPY . .
RUN npm install
EXPOSE 9090
CMD [ "node", "app.js" ]
