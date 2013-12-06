var net = require('net');
var util = require('util');

var HOST = '192.168.43.128';
var PORT = 6969;

var connection;

net.createServer(function(sock) {
    // We have a connection - a socket object is assigned to the connection automatically
    console.log((new Date().toUTCString()) + ': CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);
    
    // Add a 'data' event handler to this instance of socket
    sock.on('data', function(data) {
		//try{	
			var cmd=data.toString();
			console.log(cmd);
			cmd_exec('cscript speak.vbs "'+cmd+'"');
		//}catch(e){}
    });
    
    // Add a 'close' event handler to this instance of socket
    sock.on('close', function(data) {
		delete(sock);
        console.log((new Date().toUTCString()) + ' disconnected.');
    });
    
}).listen(PORT, HOST);

console.log((new Date().toUTCString()) + ': Socket server listening on ' + HOST +':'+ PORT);

function cmd_exec(cmd, cb_stdout, cb_end) {
  var exec = require('child_process').exec,
    child = exec(cmd);
}