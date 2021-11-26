package pl.pumbakos.japwebservice.japresources;

public class EndPoint {
    public final static String AUTHORS = "/authors";
    public static final String SONGS = "/songs";
    public static final String ALBUMS = "/albums";
    public static final String PRODUCERS = "/producers";
    public final static String ALL = "/all";
    public static final String SIZE = "/size";
    public static final String INFO = "/info";

    public static class PathVariable {
        public static final String FILENAME = "/{filename}";
        public final static String ID = "/{id}";
    }
}
