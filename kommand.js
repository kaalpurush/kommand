var net = require('net');
var util = require('util');

var HOST = '192.168.43.128';
var PORT = 6969;

net.createServer(function(sock) {
    // We have a connection - a socket object is assigned to the connection automatically
    //console.log((new Date().toUTCString()) + ': CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);
    
    // Add a 'data' event handler to this instance of socket
    sock.on('data', function(data) {
		try{	
			var cmd=data.toString();
			console.log(cmd);			
			cmd_exec('cscript speak.vbs "'+cmd+'"');
			process_cmd(cmd);
			
		}catch(e){}
    });
    
    // Add a 'close' event handler to this instance of socket
    sock.on('close', function(data) {
        //console.log((new Date().toUTCString()) + ' disconnected.');
    });
    
}).listen(PORT, HOST);

//console.log((new Date().toUTCString()) + ': Socket server listening on ' + HOST +':'+ PORT);

function cmd_exec(cmd, cb_stdout, cb_end) {
  var exec = require('child_process').exec,
    child = exec(cmd);
}

function process_cmd(cmd){

if(cmd.indexOf('search')>=0)
	cmd_exec('start /max http://www.google.com/search?q="'+cmd.replace('search','').replace(/^\s\s*/, '').replace(/\s\s*$/, '').replace(/ /g, '+')+'"');

if(cmd.indexOf('open')>=0)
	cmd_exec('start /max http://'+cmd.replace('open','').replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
	
if(cmd.indexOf('run')>=0)
	cmd_exec('start '+cmd.replace('run','').replace(/^\s\s*/, '').replace(/\s\s*$/, ''));

if(cmd.indexOf('shutdown')>=0 && cmd.indexOf('computer')>=0)
	cmd_exec('shutdown -s -f');

if(cmd.indexOf('restart')>=0 && cmd.indexOf('computer')>=0)
	cmd_exec('shutdown -r -f');	
	
if(cmd.indexOf('logoff')>=0 || cmd.indexOf('log off')>=0 && cmd.indexOf('computer')>=0)
	cmd_exec('shutdown -l');
	
if(cmd.indexOf('lock')>=0 && cmd.indexOf('computer')>=0 && cmd.indexOf('unlock')<0)
	cmd_exec('rundll32.exe user32.dll,LockWorkStation');
	
if(cmd.indexOf('unlock')>=0 && cmd.indexOf('computer')>=0)
	setTimeout(function(){cmd_exec('cscript speak.vbs "Please enter your password."');},2000);
	
}
