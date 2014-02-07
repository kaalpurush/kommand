var kommand=require('../lib/kommand');
var config = require('./config');
var path=require('path');

kommand.run(config.port, config.ip);

kommand.events.on('data',function(data){
	console.log(data);
	require('child_process').exec('cscript "'+path.resolve('../bin/speak.vbs')+'" "'+data+'"');
});