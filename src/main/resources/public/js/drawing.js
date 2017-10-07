var canvas, ctx, flag = false, prevX = 0, currX = 0, prevY = 0, currY = 0, dot_flag = false;

var x = "black", y = 10;


function init() {
	canvas = document.getElementById('can');
	ctx = canvas.getContext("2d");
	w = canvas.width;
	h = canvas.height;

	canvas.addEventListener("mousemove", function(e) {
		findxy('move', e)
	}, false);
	canvas.addEventListener("mousedown", function(e) {
		findxy('down', e)
	}, false);
	canvas.addEventListener("mouseup", function(e) {
		findxy('up', e)
	}, false);
	canvas.addEventListener("mouseout", function(e) {
		findxy('out', e)
	}, false);
}

function color(label, val, text) {
	if (val < 0.1) {
		label.style.color = "black";
	} else if (val < 0.5) {
		label.style.color = "red";
	} else if (val < 0.75) {
		label.style.color = "orange";
	} else {
		label.style.color = "green";
	}
	label.innerText = text;
}

function draw() {
	ctx.beginPath();
	ctx.moveTo(prevX, prevY);
	ctx.lineTo(currX, currY);
	ctx.strokeStyle = x;
	ctx.lineWidth = y;
	ctx.stroke();
	ctx.closePath();
}

function erase() {
	ctx.clearRect(0, 0, w, h);
	document.getElementById("canvasimg").style.display = "none";

}

function save() {
	document.getElementById("canvasimg").style.border = "2px solid";
	var dataURL = canvas.toDataURL();
	document.getElementById("canvasimg").src = dataURL;
	document.getElementById("canvasimg").style.display = "inline";
	return post_to_backend(dataURL);
}
function post_to_backend(data) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "/data", false);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(data);
	response = JSON.parse(xhttp.responseText);
	for (var i = 0; i < response.prediction.length; i++) {
		color(document.getElementById("out_"+i), response.prediction[i], i+"="+(parseFloat(Math.round(response.prediction[i]*100 * 100) / 100).toFixed(2))+"%");
	}
	
	return response;
	
}

function findxy(res, e) {
	if (res == 'down') {
		erase();
		prevX = currX;
		prevY = currY;
		currX = e.clientX - canvas.offsetLeft;
		currY = e.clientY - canvas.offsetTop;

		flag = true;
		dot_flag = true;
		if (dot_flag) {
			ctx.beginPath();
			ctx.fillStyle = x;
			ctx.fillRect(currX, currY, 2, 2);
			ctx.closePath();
			dot_flag = false;
		}
	}
	if (res == 'up' || res == "out") {
		flag = false;
		save();
	}
	if (res == 'move') {
		if (flag) {
			prevX = currX;
			prevY = currY;
			currX = e.clientX - canvas.offsetLeft;
			currY = e.clientY - canvas.offsetTop;
			draw();
		}
	}
}