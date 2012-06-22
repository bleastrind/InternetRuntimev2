package org.internetrt.exceptions

case class AuthenticationException(msg:String) extends Exception {

}

class AuthDelayException(msg:String) extends AuthenticationException(msg){
    def this() = this("")
}

class WrongPasswordException(msg:String) extends AuthenticationException(msg){
  def this() = this("Password wrong")
}

class UserNotRegisteredException(msg:String) extends AuthenticationException(msg){
  def this() = this("User not exists")
}