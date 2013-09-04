package org.internetrt.core.siblings
import java.net.Socket
import java.io.PrintWriter

class SocketNodeRef(ip: String, port:Int) {
	def update() {
		communicate("update")
	}
	
	private def communicate(cmd:String){
	  	  	val client=new Socket(ip,port)
		    val pw=new PrintWriter(client.getOutputStream())  
			pw.println(cmd)
			pw.flush()
			pw.close()
			client.close()
	}
}
