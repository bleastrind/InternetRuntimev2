package org.internetrt.util

class URI(uri:String){
  var protocal:String=""
  var content:String=""
  var query:String=""
  try{
    val parts = uri.split("://")
    
    protocal = parts(0)
    val leftparts = parts(1).split("\\?")
    
    content = leftparts(0)
    query = if (leftparts.length > 1) leftparts(1) else ""
  }catch{
    case e =>e.printStackTrace(); throw new Exception("Bad Formated URI!"+uri)
  }
  
}

class URIHelper {
	
}