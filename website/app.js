const { google } = require('googleapis');
const express = require('express');
const OAuth2Data = require('./google_key.json');

const app = express();

const CLIENT_ID = "377968007025-013sa07vehs51n1rau6qfmplp7esq964.apps.googleusercontent.com";
const CLIENT_SECRET = "dXw6n2fh3lNh6URBVW_0P6xO";
const REDIRECT_URL = "http://localhost:8080/oauth2/google";

const oAuth2Client = new google.auth.OAuth2(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
var authed = false;

app.use(express.static('static'));

app.get('/login', (req, res) => {
    console.log(Object.keys(req.query).length);
    if (Object.keys(req.query).length === 0)
        res.sendFile('login.html', { root: __dirname });
    else {
        console.log(req.query);
        var username = req.query.username;
        var pwd = req.query.pass;
        const url = "http://localhost:8080/login?name=" + username + "&pwd=" + pwd + "";
        res.redirect(url);
    }
});

app.get('/login/github', (req, res) => {
    const url = "https://github.com/login/oauth/authorize?scope=user:email,repo&client_id=1b8ddffb28f26996c08f";
    res.redirect(url);
});

app.get('/login/google', (req, res) => {
    if (!authed) {
        // Generate an OAuth URL and redirect there
        const url = oAuth2Client.generateAuthUrl({
            access_type: 'offline',
            scope: 'https://www.googleapis.com/auth/gmail.readonly',
            scope: 'https://www.googleapis.com/auth/userinfo.profile',
            scope: 'https://www.googleapis.com/auth/userinfo.email'
        });
        console.log(url);
        res.redirect(url);
    } else {
        const gmail = google.gmail({ version: 'v1', auth: oAuth2Client });
        gmail.users.labels.list({
            userId: 'me',
        }, (err, res) => {
            if (err) return console.log('The API returned an error: ' + err);
            const labels = res.data.labels;
            if (labels.length) {
                console.log('Labels:');
                labels.forEach((label) => {
                    console.log(`- ${label.name}`);
                });
            } else {
                console.log('No labels found.');
            }
        });
        res.send('Logged in')
    }
});

app.get('/auth/google/callback', function (req, res) {
    const code = req.query.code;
    if (code) {
        // Get an access token based on our OAuth code
        oAuth2Client.getToken(code, function (err, tokens) {
            if (err) {
                console.log('Error authenticating');
                console.log(err);
            } else {
                console.log('Successfully authenticated');
                oAuth2Client.setCredentials(tokens);
                console.log(oAuth2Client);
                authed = true;
                res.redirect('/');
            }
        });
    }
});

const port = process.env.port || 9090;
app.listen(port, () => console.log(`Server running at ${port}`));
