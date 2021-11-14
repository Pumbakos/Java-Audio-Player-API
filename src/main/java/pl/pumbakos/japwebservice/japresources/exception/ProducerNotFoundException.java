package pl.pumbakos.japwebservice.japresources.exception;

public class ProducerNotFoundException extends RuntimeException{
    public ProducerNotFoundException(long id){
        super("Could not find producer. ID: " + id);
    }
}
