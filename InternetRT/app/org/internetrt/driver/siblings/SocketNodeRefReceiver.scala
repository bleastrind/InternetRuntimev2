package org.internetrt.driver.siblings
import java.net.ServerSocket
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.InputStreamReader
import org.internetrt.persistent._
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._
import org.internetrt.Prop
import java.net.Socket
import org.internetrt.core.siblings.TwoTierClusterManagerImpl

class SocketNodeRefReceiver {
	  def start(manager:TwoTierClusterManagerImpl)={
	    new GlobalControlSocketListener(manager).start()
	    new LocalControlSocketListener(manager).start()
	  }
}


object GlobalAddCommand{
  def unapply(str:String):Option[(String, String, String,String)] = {
    val parts = str.split(" ")
    //add domain ip ip_Alter val name
    if (parts.length==4 && parts(0).toLowerCase().compareTo("add")==0) {
      
      Some(parts(0).trim(),parts(1).trim(),parts(2).trim(),parts(3).trim())
    } else None
  }
}
object LocalAddCommand{
  def unapply(str:String):Option[(String, String)] = {
    val parts = str.split(" ")
    if (parts.length==2 && parts(0).toLowerCase().compareTo("add")==0) Some(parts(0).trim(),parts(1).trim()) else None
  }
}
object DeleteCommand{
  def unapply(str:String):Option[(String,String)] = {
    val parts = str.split(" ")
    if (parts.length==2 && parts(0).toLowerCase().compareTo("delete")==0) Some(parts(0).trim(),parts(1).trim()) else None
  } 
}

object UpdateCommand{
  def unapply(str:String) : Option[(String)] = {
    val parts = str.split(" ");
    if (parts.length==1 && parts(0).toLowerCase().compareTo("update")==0) Some(parts(0).trim()) else None
  }
}

object ExitCommand{
  def unapply(str:String): Option[(String)] = {
    val parts = str.split(" ");
    if (parts.length==1 && parts(0).toLowerCase().compareTo("exit")==0) Some(parts(0).trim()) else None
  }
}

class GlobalControlSocketListener(manager:TwoTierClusterManagerImpl) extends Thread {

  def dealCommand(str:String,wr:PrintWriter ) = {
    
    str match{
      case GlobalAddCommand(cmd,domain,ip,ipAlternative) => {
        manager.addGlobalNode(domain,ip,ipAlternative)
       true
      }
      case DeleteCommand(cmd,domain) => {
        manager.deleteGlobalNode(domain)
       true
      }
      case UpdateCommand(str) => {
    	  manager.getAllGlobalNode()
    	  true
      }
      
      case ExitCommand(str) => false
      case _ => {
        wr.write("Command error! Please check it!")
        wr.flush()
        true
      }
    }
  }
  override def run() = {
	  
    class socketHandler(socket:Socket) extends Thread{
	    override def run() = {
	      val br = new BufferedReader(new InputStreamReader(socket.getInputStream()))
	      val wr = new PrintWriter(socket.getOutputStream())
	      
	      breakable {
	
	        while (true) {
	          val str = br.readLine();
	          
	          if (!dealCommand(str,wr)) break
	          
	        }
	      }
	      br.close();
	      socket.close();
	    }
	  } 
    
    //socket
    val ss = new ServerSocket(Prop.GlobalPort);
    while (true) {
      val socket = ss.accept()
      
      new Thread(new socketHandler(socket)).start()
      
    }
  }
}

class LocalControlSocketListener(manager:TwoTierClusterManagerImpl) extends Thread {


  
  def dealCommand(str:String,wr:PrintWriter) = 
  {
    str match{
      case LocalAddCommand(cmd,ip) => {
    	  manager.addLocalNode(ip)
    	  true
      }
      case DeleteCommand(cmd,ip) => {
    	  manager.deleteLocalNode(ip)
    	  true
      }
      case UpdateCommand(str) =>{
    	  manager.getAllLocalNode() 
    	  true
      }
      
      case ExitCommand(str) => false
      case _ => {
    	  wr.write("Command error! Please check it!")
    	  wr.flush()
    	  true
      }
    }
  }
  override def run() = {

    class socketHandler(socket:Socket) extends Thread{
	    override def run() = {
	      val br = new BufferedReader(new InputStreamReader(socket.getInputStream()))
	      val wr = new PrintWriter(socket.getOutputStream())
	      
	      breakable {
	
	        while (true) {
	          val str = br.readLine();
	          
	          if (!dealCommand(str,wr)) break
	          
	        }
	      }
	      br.close();
	      socket.close();
	    }
	  } 
    //socket
    val ss = new ServerSocket(Prop.LocalPort);
    while (true) {
      val socket = ss.accept()
      
      new Thread(new socketHandler(socket)).start()
    }
  }
}
   