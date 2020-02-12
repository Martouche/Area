FROM node:10-alpine
WORKDIR /home/node/app
RUN ["ls"]
RUN ["pwd"]
COPY . .
RUN npm install
EXPOSE 9090
CMD [ "node", "app.js" ]
