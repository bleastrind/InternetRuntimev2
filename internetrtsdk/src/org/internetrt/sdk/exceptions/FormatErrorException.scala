package org.internetrt.sdk.exceptions
object FormatErrorException{
  def checkNonEmptyTerm(term: String, description: String){
     if (term == null || term == "")
      {
	    throw new FormatErrorException("Error in  Xml: "+description+" not set!");
      }
	}
}
class FormatErrorException(msg:String) extends Exception(msg) {

}