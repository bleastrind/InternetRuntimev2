package org.internetrt.util

object Debuger {
	var pattern = java.util.regex.Pattern.compile(".*");
	var ignorepattern = java.util.regex.Pattern.compile("\\[ClusterConsider.*");
	def debug(msg:String){
	  if(pattern.matcher(msg).matches() && !ignorepattern.matcher(msg).matches())
	    System.out.println(msg)
	}
	def error(msg:String){
	  System.err.println(msg);
	}
	def assert(exp: Boolean, msg:String) = {
	  if(!exp){
	    System.out.println("!!!!!!Bug:"+msg);
	    throw new Exception("Assert Fault");
	  }
	}
}