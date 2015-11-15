var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var i =1;
var ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8,ch9,ch10;
app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket){

	socket.on('publisher',function(msg){
		console.log("Entro a publisher");
		console.log(msg);
		if (i<=10){
			io.emit("publisher",i);
			i++;
		}else{
			io.emit("publisher",0);
		}
	});

	socket.on('suscriber',function(msg){
		console.log("Entro a suscriber");
		console.log(msg);
		if (i>0){
			io.emit('suscriber',msg);
		}else{
			io.emit('suscriber',0);
		}
	});

  socket.on('chat message', function(msg){
  	console.log("ddd");
  	console.log(msg);
    io.emit('chat message', msg);
  });
});

http.listen(3000, function(){
  console.log('listening on *:3000');
}); 
