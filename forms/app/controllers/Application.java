package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.*;
import play.libs.F.Callback;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render());
    }
    
    public static Map<String, Out<String>> sockets = new HashMap<String, WebSocket.Out<String>>();
    public static Map<String, File> pics = new HashMap<String, File>();
    
    
    public static Result getPic(String token) {
    	File f = pics.get(token);
        return ok(pics.get(token));
    }
    
    public static WebSocket<String> refreshPic(final String token)
    {
    	return new WebSocket<String>() {
			@Override
			public void onReady(In<String> in, Out<String> out) {
				sockets.put(token, out);
			}
		};
    }
    
    public static Result upload(String token) {
    
    	System.out.println("GGGet upload!");
	  MultipartFormData body = request().body().asMultipartFormData();
	  FilePart picture = body.getFile(token);
	  if (picture != null) {
	    String fileName = picture.getFilename();
	    String contentType = picture.getContentType(); 
	    File file = picture.getFile();
	    
	    pics.put(token, file);
	    sockets.get(token).write(token);
	    System.out.println(token);
	
	    
	   
	    
	    
	    return ok("File uploaded");
	  } else {
	    flash("error", "Missing file");
	    return redirect(routes.Application.index());    
	  }
	}
	
	
  
}