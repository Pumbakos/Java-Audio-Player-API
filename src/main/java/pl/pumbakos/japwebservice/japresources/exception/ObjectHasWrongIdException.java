package pl.pumbakos.japwebservice.japresources.exception;

public class ObjectHasWrongIdException extends Throwable {
    public ObjectHasWrongIdException(){
        super();
    }

    public ObjectHasWrongIdException(String message){
        super(message);
    }
}
