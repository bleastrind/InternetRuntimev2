package controllers;

import play.*;
import play.mvc.*;
import play.cache.*;

import java.io.IOException;
import java.util.*;

import org.apache.commons.httpclient.HttpException;
import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.exceptions.ServerSideException;

import config.properties;

import models.*;

public class Application extends Controller {

    public static void index() {
       render();
    }

    public static void login() {
        render();
    }

    public static void getAccount(String account, String password) {
        if (account.equals("admin") && password.equals("admin"))
            AdminController.welcome();
        else login();
    }
    
    public static void loginUser(String code,String msg) throws HttpException, IOException,ServerSideException{
		if (!msg.equals("success")) {
			render("Application/err.html");
		}
    	InternetRT irt = properties.irt;
		String accessToken = irt.setAccessTokenWithCode(code);
		session.put("token", accessToken);
		System.out.println("[Application : loginUser]: "+"Access Token:" + accessToken);
		index();
    }
    
   
}