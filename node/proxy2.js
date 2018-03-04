'use strict';
const crypto = require('crypto');
const argv = require('minimist')(process.argv.slice(2));
var https = require('https'),
  fs = require('fs'),
  connect = require('connect'),
  httpProxy = require('http-proxy'),
  transformerProxy = require('transformer-proxy');

  var ssecurity = "";
  var nonce = "";
  var payload = "";
  var action = "";

  var transformerLogin = function(_data,req,res){
    var respuesta = _data.toString();
    ssecurity = respuesta.substring(respuesta.lastIndexOf('&&&START&&&')+11);
    ssecurity = JSON.parse(ssecurity).ssecurity;
    console.log('ssecurity: ',ssecurity);
    return _data;
  }

  var transformerFunction = function (_data, req, res) {
    return new Promise(function(resolve,reject){
      var spawn = require('child_process').spawn;
      var prc = spawn('java',  ['-jar', 'downgrader.jar', 'd', ssecurity, nonce, payload]);
      prc.stdout.setEncoding('utf8');
      prc.stdout.on('data', function (data) {
          var str = data.toString();
          var lines = str.split(/(\r?\n)/g);
          console.log(lines[0]);
            console.log('nonce',nonce);
            console.log('ssecurity',ssecurity);
            switch (action) {
              case 'firmware':
                var prc2 = spawn('java',  ['-jar', 'downgrader.jar', 'c', ssecurity, nonce, 'firmware']);
                break;
              case 'bms':
                var prc2 = spawn('java',  ['-jar', 'downgrader.jar', 'c', ssecurity, nonce, 'bms']);
                break;
              case 'cfw':
                var prc2 = spawn('java',  ['-jar', 'downgrader.jar', 'c', ssecurity, nonce, 'cfw']);
                break;
              default:
                var prc2 = spawn('java',  ['-jar', 'downgrader.jar', 'c', ssecurity, nonce]);
            }

            prc2.stdout.setEncoding('utf8');
            prc2.stdout.on('data', function (data2) {
                var str2 = data2.toString();
                var lines2 = str2.split(/(\r?\n)/g);
                console.log('<-', lines2[0]);

                resolve(lines2[0]);
              });
        });
    });
  };

var secureContext = {
    'account.xiaomi.com': crypto.createCredentials({
        key: fs.readFileSync('xiaomiKey.pem', 'utf8'),
        cert: fs.readFileSync('xiaomi.pem', 'utf8'),
        ca: fs.readFileSync('JaimeCA.pem', 'utf8') // this ca property is optional
    }),
    'de.api.io.mi.com': crypto.createCredentials({
        key: fs.readFileSync('miKey.pem', 'utf8'),
        cert: fs.readFileSync('mi.pem', 'utf8'),
        ca: fs.readFileSync('JaimeCA.pem', 'utf8') // this ca property is optional
    }),
    'sg.api.io.mi.com': crypto.createCredentials({
        key: fs.readFileSync('api.io.mi.com-Key.pem', 'utf8'),
        cert: fs.readFileSync('api.io.mi.com.pem', 'utf8'),
        ca: fs.readFileSync('JaimeCA.pem', 'utf8') // this ca property is optional
    }),
    'us.api.io.mi.com': crypto.createCredentials({
        key: fs.readFileSync('api.io.mi.com-Key.pem', 'utf8'),
        cert: fs.readFileSync('api.io.mi.com.pem', 'utf8'),
        ca: fs.readFileSync('JaimeCA.pem', 'utf8') // this ca property is optional
    }),
    'api.io.mi.com': crypto.createCredentials({
        key: fs.readFileSync('io.mi.com-Key.pem', 'utf8'),
        cert: fs.readFileSync('io.mi.com.pem', 'utf8'),
        ca: fs.readFileSync('JaimeCA.pem', 'utf8') // this ca property is optional
    })
}


var options = {
  SNICallback: function (domain, cb) {
      if (secureContext[domain]) {
          if (cb) {
              cb(null, secureContext[domain]);
          } else {
              // compatibility for older versions of node
              return secureContext[domain];
          }
      } else {
          console.log('No keys/certificates for domain requested', domain);
      }
  },
 // must list a default key and cert because required by tls.createServer()
  key: fs.readFileSync('JaimeCAKey.pem'),
  cert: fs.readFileSync('JaimeCA.pem')
};


var app = connect();
var proxy;

switch (argv.region) {
  case 'china':
    proxy = httpProxy.createProxyServer({
      changeOrigin: true,
      target: 'https://api.io.mi.com',
      secure: false
    });
    console.log('Region: CHINA');
    break;
  default:
    proxy = httpProxy.createProxyServer({
      changeOrigin: true,
      target: 'https://de.api.io.mi.com',
      secure: false
    });
    console.log('Region : EUROPE');
}

/*var proxy = httpProxy.createProxyServer({
  changeOrigin: true,
  target: 'https://de.api.io.mi.com',
  secure: false
});*/

var proxy2 = httpProxy.createProxyServer({
  changeOrigin: true,
  target: 'https://account.xiaomi.com',
  secure: false
});

app.use(transformerProxy(transformerFunction,{match : /\/app\/home\/latest_version/}));
app.use(transformerProxy(transformerLogin,{match: /\/pass\/serviceLoginAuth2/}));

app.use(function (req, res) {
  console.log(req.connection.remoteAddress, '->[', req.url , ']');
  if(req.url==='/app/home/latest_version'){
    req.headers['accept-encoding'] = 'deflate';
    let body = [];
    req.on('data', (chunk) => {
      body.push(chunk);
    }).on('end', () => {
      body = Buffer.concat(body).toString();
      console.log(req.headers);
      if(req.headers['user-agent'].toString().indexOf('Android') > -1){
        nonce = body.substring(body.lastIndexOf('_nonce=')+7,body.lastIndexOf('&ssecurity'));
        nonce = decodeURIComponent(nonce);
        ssecurity = body.substring(body.lastIndexOf('&ssecurity=')+11);
        ssecurity = decodeURIComponent(ssecurity);
        payload = body.substring(body.lastIndexOf('data=')+5,body.lastIndexOf('&rc4_hash__='));
        payload = decodeURIComponent(payload);
        console.log('nonce android: ', nonce);
        console.log('ssecurity android: ', ssecurity);
        console.log('payload android:', payload);
      }else{
        var n = body.lastIndexOf('_nonce=');
        var result = body.substring(n + 1);
        result = result.substring(0,result.indexOf('&data='));
        nonce = result.substring(result.indexOf('=')+1);
        nonce = decodeURIComponent(nonce);
        payload = body.substring(body.lastIndexOf('&data=')+6,body.lastIndexOf('&rc4_hash__='));
        payload = decodeURIComponent(payload);
      }
    });
    proxy.web(req, res);
  }else if(req.headers.host==='account.xiaomi.com'){
    console.log('->', req.url);
    req.headers['accept-encoding'] = 'deflate';
    proxy2.web(req, res);
  }else{
    proxy.web(req, res);
  }
});

console.log('Starting downgrader server...');
if(argv.ssecurity != null){
  ssecurity = argv.ssecurity;
  console.log('ssecurity: ', ssecurity);
}

if(argv.action != null || process.env.DOWNGRADE_ACTION != null){
  if(argv.action != null){
    switch (argv.action.toUpperCase()) {
      case 'BMS':
        action = 'bms';
        break;
      case 'FIRMWARE':
        action = 'firmware';
        break;
      case 'ALL':
        action = 'all';
        break;
      case 'CFW':
        action = 'cfw';
        break;
      default:
        action = 'all';
        console.log('Action not recognized.');
    }
  }else{
    // For docker environments
    switch (process.env.DOWNGRADE_ACTION.toUpperCase()) {
      case 'BMS':
        action = 'bms';
        break;
      case 'FIRMWARE':
        action = 'firmware';
        break;
      case 'ALL':
        action = 'all';
        break;
      case 'CFW':
        action = 'cfw';
        break;
      default:
        action = 'all';
        console.log('Action not recognized.');
    }
  }
  console.log('Action: downgrade', action);
}else{
  console.log('Action not found. Assuming All...');
}

https.createServer(options, app).listen(443);
