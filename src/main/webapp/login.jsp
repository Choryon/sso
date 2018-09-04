<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript">

function setHeader(xhr){ // XmlHttpRequest
	//xhr.setRequestHeader("Authorization",window.localStorage.token);
	var strCookie = document.cookie;    // 获取cookie字符串
	var arrCookie = strCookie.split(";");        //从分号的位置 分割字符串strCookie为字符串数组
     // alert(arrCookie);               ////  userId=828，userName=hulk     数组:["userId=828","userName=hulk"]
    var global_id;
      //遍历cookie数组，处理每个cookie对
      for(var i=0;i<arrCookie.length;i++){

          var arr = arrCookie[i].split("=");           //从 = 的位置 分割每对cookie

          if( arr[0] == "global_id" ){      //如果前面是 userId 就是找到了
        	  global_id = arr[1];         //将后面的值赋给 userId ，跳出循环
              break;
          }
      }
      alert(global_id);
      xhr.setRequestHeader("Authorization",global_id);
}

function testLocalStorage(){
	$.ajax({
		'url' : '${pageContext.request.contextPath}/testAll',
		'success' : function(data){
			if(data.code == 200){
				window.localStorage.token = data.token;
				alert(data.data);
			}else{
				alert(data.msg);
			}
		},
		'beforeSend' : setHeader
	});
}

</script>
</head>
<body >
	<input type="button" value="testLocalStorage" onclick="testLocalStorage();"/>
</body>
</html>