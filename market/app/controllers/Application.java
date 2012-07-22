package controllers;

import play.*;
import play.mvc.*;
import play.cache.*;

import java.io.IOException;
import java.util.*;

import org.apache.commons.httpclient.HttpException;
import org.internetrt.sdk.InternetRT;

import config.properties;

import models.*;

public class Application extends Controller {

    public static void index() {
       Controller.redirect("AppController.listAllApps");
    }

    public static void login() {
        render();
    }

    public static void getAccount(String account, String password) {
        if (account.equals("admin") && password.equals("admin"))
            AdminController.welcome();
        else login();
    }
    
    public static void loginUser(String code) throws HttpException, IOException{
		InternetRT irt = properties.irt;
		String accessToken = irt.setAccessTokenWithCode(code);
		session.put("token", accessToken);
		System.out.println("[Application : loginUser]: "+"Access Token:" + accessToken);
		index();
    }
    
   
}