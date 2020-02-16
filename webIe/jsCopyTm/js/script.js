//初始化阶段
var timeCount = null,
    index = 0,
    imgs = document.getElementById("banner").getElementsByTagName("div"),
    dots = document.getElementById("dots").getElementsByTagName("span"),
    num = imgs.length,
	prev = document.getElementById("prev"),
    next = document.getElementById("next");
	menuItems = document.getElementById("menu-content").getElementsByTagName("div"),
    subMenu = document.getElementById("sub-menu"),
    subItems = subMenu.getElementsByClassName("inner-box");

/*
 *	clearInterval() 方法可取消由 setInterval() 函数设定的定时执行操作。
 *	clearInterval() 方法的参数必须是由 setInterval() 返回的 ID 值。
 */

//停止自动播放
function stopAutoPlay(){
	if(timeCount){
		//使用 clearInterval() 来停止执行:
       clearInterval(timeCount);
	}
}

//图片轮换
function changeImg(){
   for(var i=0,len=dots.length;i<len;i++){
       dots[i].className = "";
       imgs[i].style.display = "none";
   }
   dots[index].className = "active";
   imgs[index].style.display = "block";
}

//图片自动轮播
function startAutoPlay(){
	//显示当前时间 ( setInterval() 函数会每秒执行一次函数，类似手表)。
   timeCount = setInterval(function(){
       index++;
       if(index >= num){
          index = 0;
       }
       changeImg();
   },3200)
}


//图片轮播控制
function slideImg(){
    var main = document.getElementById("main");
    var banner = document.getElementById("banner");
	// onmouseover 事件会在鼠标指针移动到指定的元素上时发生。
    main.onmouseover = function(){
    	stopAutoPlay();
    }
	//onmouseout 事件会在鼠标指针移出指定的对象时发生。
    main.onmouseout = function(){
    	startAutoPlay();
    }
    main.onmouseout();
	
	//导航圆点切换
    for(var i=0,len=dots.length;i<len;i++){
       dots[i].id = i;
       dots[i].onclick = function(){
           index = this.id;
           changeImg();
       }
    }

    //右边箭头按钮
    next.onclick = function(){
       index++;
       if(index>=num) index=0;
       changeImg();
    }

    //左边箭头按钮
    prev.onclick = function(){
       index--;
       if(index<0) index=num-1;
       changeImg();
    }
    //左拉菜单悬浮
    for(var m=0,mlen=menuItems.length;m<mlen;m++){
        menuItems[m].setAttribute("data-index",m);
        menuItems[m].onmouseover = function(){
            subMenu.className = "sub-menu";
            var idx = this.getAttribute("data-index");
            for(var j=0,jlen=subItems.length;j<jlen;j++){
               subItems[j].style.display = 'none';
               menuItems[j].style.background = "none";
            }
            subItems[idx].style.display = "block";
            menuItems[idx].style.background = "rgba(0,0,0,0.1)";
        }
    }

    subMenu.onmouseover = function(){
        this.className = "sub-menu";
    }

    subMenu.onmouseout = function(){
        this.className = "sub-menu hide";
    }

    menuContent.onmouseout = function(){
        subMenu.className = "sub-menu hide";
    }
}

// 启动轮播
slideImg();