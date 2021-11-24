package pl.pumbakos.japwebservice.japresources;

/**
 * Represents the status codes of the operation. <br>
 * Used to unify communication between the client and the server.
 */
public enum Status {
    BAD_EXTENSION(-1),
    NO_CONTENT(-2),
    INVALID_TITLE(-3);

    private long code;

    Status(int code){
        this.code = code;
    }

    /**
     * Represents the status massages of the operation. <br>
     * Used to unify communication between the client and the server.
     */
    public enum Message{
        NO_CONTENT("NO_CONTENT"),
        BAD_EXTENSION("BAD_EXTENSION"),
        INVALID_TITLE("INVALID_TITLE"),
        ACCEPTED("ACCEPTED"),
        OK("OK"),
        UPDATED("UPDATED"),
        BAD_REQUEST("BAD_REQUEST"),
        NOT_FOUND("NOT_FOUND"),
        INTERNAL_ERROR("INTERNAL_SERVER_ERROR");

        private String name;

        Message(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }
    }

    public long getCode() {
        return code;
    }


}
