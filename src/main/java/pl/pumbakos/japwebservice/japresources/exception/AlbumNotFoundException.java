package pl.pumbakos.japwebservice.japresources.exception;

public class AlbumNotFoundException extends RuntimeException{
    public AlbumNotFoundException(long id){
        super("Could not find album. ID: " + id);
    }
}
