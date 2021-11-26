package pl.pumbakos.japwebservice.japresources.exception;

public class SongNotFoundException extends RuntimeException{
    public SongNotFoundException(String title){
        super("Could not find song. Title: " + title);
    }
    public SongNotFoundException(){
        super("Could not find song");
    }
}
