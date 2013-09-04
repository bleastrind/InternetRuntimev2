package org.internetrt.exceptions

class InvalidStatusException(msg:String) extends ConsideredException(msg) {

}

class ApplicationNotInstalledException extends InvalidStatusException("You have to install the application first"){
  
}

class ConflictRequestWithOutOption extends InvalidStatusException("Missing Option, check InitOption First!"){
  
}