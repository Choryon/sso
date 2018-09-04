package com.sxt.sso.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sxt.sso.commons.CookieUtils;
import com.sxt.sso.commons.JWTResponseData;
import com.sxt.sso.commons.JWTResult;
import com.sxt.sso.commons.JWTSubject;
import com.sxt.sso.commons.JWTUsers;
import com.sxt.sso.commons.JWTUtils;

@Controller
public class JWTController {

	@RequestMapping("/testAll")
	@ResponseBody
	public Object testAll(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println(cookie.getName());
			System.out.println(cookie.getValue());
		}
		String token = request.getHeader("Authorization");
		JWTResult result = JWTUtils.validateJWT(token);
		
		JWTResponseData responseData = new JWTResponseData();
		
		if(result.isSuccess()){
			responseData.setCode(200);
			responseData.setData(result.getClaims().getSubject());
			// 重新生成token，就是为了重置token的有效期。
			String newToken = JWTUtils.createJWT(result.getClaims().getId(), 
					result.getClaims().getIssuer(), result.getClaims().getSubject(), 
					1*60*1000);
			responseData.setToken(newToken);
			return responseData;
		}else{
			responseData.setCode(500);
			responseData.setMsg("用户未登录");
			return responseData;
		}
		
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public Object login(String username, String password,HttpServletRequest request,
			HttpServletResponse response){
		JWTResponseData responseData = null;
		// 认证用户信息。本案例中访问静态数据。
		response.setCharacterEncoding("UTF-8");
		if(JWTUsers.isLogin(username, password)){
			JWTSubject subject = new JWTSubject(username);
			String jwtToken = JWTUtils.createJWT(UUID.randomUUID().toString(), "sxt-test-jwt", 
					JWTUtils.generalSubject(subject), 1*60*1000);
			String cookieName="global_id";
			String encodeString="UTF-8";
			CookieUtils.setCookie(request, response, cookieName, jwtToken,60, encodeString);
			responseData = new JWTResponseData();
			responseData.setCode(200);
			responseData.setData(null);
			responseData.setMsg("登录成功");
			responseData.setToken(jwtToken);
		}else{
			responseData = new JWTResponseData();
			responseData.setCode(500);
			responseData.setData(null);
			responseData.setMsg("登录失败");
			responseData.setToken(null);
		}
		
		return responseData;
	}
	
}
