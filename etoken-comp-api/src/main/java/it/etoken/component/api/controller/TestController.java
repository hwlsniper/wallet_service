package it.etoken.component.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/test")
public class TestController {

	@RequestMapping(value = "/test")
	public String test(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		return "http://xxx.com";
	}
}
