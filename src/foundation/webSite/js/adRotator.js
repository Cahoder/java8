// 第一步：实现页面加载后图片自动播放

	var NowFrame=1; 
	var MaxFrame=5;
	var timer=null;

	function showPic(d1){
		console.log(d1);
		 if(Number(d1)){
		 	NowFrame=d1;                
		 }
		for(var i=1;i<(MaxFrame+1);i++){
			if(i==NowFrame){	
				document.getElementById("Rotator").src ="images/adRotator_"+i+".jpg";   
				    
				document.getElementById("fig_"+i).className="numberOver";    
        	}
			else{
		 		document.getElementById("fig_"+i).innerHTML=i;
		 		document.getElementById("fig_"+i).className="number";
			}
		}
		if(NowFrame == MaxFrame){  
			NowFrame = 1;
		}else{
			NowFrame++;
		}
	
		timer=setTimeout("showPic()", 3000); 

	}

	function show(num){
		clearTimeout(timer);
		showPic(num);
	}

	window.onload=showPic;