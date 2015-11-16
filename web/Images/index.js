var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

var channels= new Array(10);
var users= new Array(20);
function initChannel(){
	for(i=0;i<10;i++){
		channels[i]=0; 
	}
}

function initUsers(){
	for(i=0;i<20;i++){
		users[i]=0; 
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
initUsers();

app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket){
	console.log("USER CONECTADO.");
	console.log(channels);
	console.log(users);
	socket.on('addUser',function(userName){
		socket.userName=userName;
	});
	socket.on('publisher',function(msg){
		console.log("Entro a publisher");
		var index=getFreeChannel();
		console.log(index);
		console.log(msg);
		if (index!=0){
			io.emit("publisher",index.toString()+':'+msg);
			channels[index-1]=1;
			users[(index-1)*2]=parseInt(msg);
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
		var elem=msg.split(':');
	 	var chn=parseInt(elem[0]);
	 	var message=elem[1];
		channels[chn-1]=2;
		if (thereIsChannelFree()){
			io.emit('suscriber',msg);
		}else{
			io.emit('suscriber',0);
		}
	});

	socket.on('chn1', function(msg){
		console.log(msg);
		if (msg=='killConnection'){
			console.log("Entro a chn1 killConnection");
			channels[0]=0;
		}else{
    		io.emit('chn1', msg);
    	}
  	});


	socket.on('chn2', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
	  		console.log("Entro a chn1 killConnection");
			channels[1]=0;
		}
	    io.emit('chn2', msg);
	});


	socket.on('chn3', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
	  		console.log("Entro a chn1 killConnection");
			channels[2]=0;
		}
	    io.emit('chn3', msg);
	});

	socket.on('chn4', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
			channels[3]=0;
		}
	    io.emit('chn4', msg);
	});

	socket.on('chn5', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
			channels[4]=0;
		}
	    io.emit('chn5', msg);
	});


	socket.on('chn6', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
			channels[5]=0;
		}
	    io.emit('chn6', msg);
	});

	socket.on('chn7', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
			channels[6]=0;
		}
	    io.emit('chn7', msg);
	});

	socket.on('chn8', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
			channels[7]=0;
		}
	    io.emit('chn8', msg);
	});

	socket.on('chn9', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
			channels[8]=0;
		}
	    io.emit('chn9', msg);
	});

	socket.on('chn10', function(msg){
	  	console.log(msg);
	  	if (msg=='killConnection'){
			channels[9]=0
		}
	    io.emit('chn10', msg);
	});

	socket.on('newGameConfirmed', function(user,channel){
	  	users[(channel-1)*2+1]=user;
	});

	socket.on('killConnection',function(msg){
		socket.emit('killConnection',msg);
	});
  
	 socket.on('disconnect', function(){
	  	console.log("SE HA DESCONECTADO:"+ socket.userName);
	  	for (i=0;i<20;i++){
	  		if ((users[i]==socket.userName)&&(channels[i/2]==1)){
	 			channels[i/2]=0;	
	  		}
	  		if ((users[i]==socket.userName)&&(channels[i/2]==2)){	
	  			console.log("chn"+(i/2+1).toString());
	  			console.log("ACA ENTRA");
	 			io.emit('chn'+(i/2+1).toString(),'killConnection');
	  		}
	  		
	  	}
	  	console.log(channels)
	  	console.log(users);
	 });
});



http.listen(3000, function(){
  console.log('listening on *:3000');
}); 

