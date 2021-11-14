package pl.pumbakos.japwebservice.japresources.exception;

public class SongAlreadyExistsException extends RuntimeException {
    public SongAlreadyExistsException(){}

    public SongAlreadyExistsException(String message){
        super(message);
    }
}
