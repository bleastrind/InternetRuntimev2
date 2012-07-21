package org.internetrt.exceptions

case class AuthenticationException(msg:String) extends ConsideredException(msg) {

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

class AccessTokenNotValiedException(msg:String) extends AuthenticationException(msg){
  def this() = this("AccessToken Not Valied")
}