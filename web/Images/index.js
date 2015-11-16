var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var channels= new Array(10);

function initChannel(){
	for(i=0;i<10;i++){
		channels[i]=0; 
	}
}
	
function thereIsChannelFree(){
	var res=false;
	var i=0;
	while (i<10)
	{
		if ((channels[i])==0) {
			res=res||true;
		}
		i++;
	}
	return res;
}

function getFreeChannel(){
	var i=0;
	while (i<10)
	{
		if ((channels[i])==0) {
			break;
		}
		i++;
	}
	if (i<10){
		channels[i]=1;
		return i+1;
	}else{
		return 0;
	}
	
}
initChannel();


app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket){

	socket.on('addUser',function(userId){
		socket.userId=userId;
	});

	socket.on('publisher',function(msg){
		console.log("Entro a publisher");
		var index=getFreeChannel();
		console.log(index);
		console.log(msg);
		if (index!=0){
			io.emit("publisher",index.toString()+':'+msg);
			console.log(index.toString()+':'+msg);
		}else{
			io.emit("publisher",0);
		}
	});



	socket.on('waitingSuscriber',function(msg){
		console.log("Entro a waitingsuscriber");
		console.log(msg);
		if (thereIsChannelFree()){
			io.emit('waitingSuscriber',msg);
		}else{
			io.emit('waitingSuscriber',0);
		}
	});


	socket.on('suscriber',function(msg){
		console.log("Entro a suscriber");
		console.log(msg);
		if (thereIsChannelFree()){
			io.emit('suscriber',msg);
		}else{
			io.emit('suscriber',0);
		}
	});


socket.on('chn1', function(msg){
  	console.log(msg);
    io.emit('chn1', msg);
  });

socket.on('chn2', function(msg){
  	console.log(msg);
    io.emit('chn2', msg);
  });

socket.on('chn3', function(msg){
  	console.log(msg);
    io.emit('chn3', msg);
  });




  socket.on('chat message', function(msg){
  	console.log("ddd");
  	console.log(msg);
    io.emit('chat message', msg);
  });
  
  socket.on('disconnect', function(){
  	console.log(socket.username);
  	socket.emit('disconnect',socket.username);
  }) 
});



http.listen(3000, function(){
  console.log('listening on *:3000');
}); 
