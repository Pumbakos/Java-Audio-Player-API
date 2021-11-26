package pl.pumbakos.japwebservice.japresources.exception;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException(long id){
        super("Could not find author. ID: " + id);
    }
}