package pl.pumbakos.japwebservice.japresources;

public enum Status {
    BAD_EXTENSION(-1),
    NO_CONTENT(-2),
    INVALID_TITLE(-3);

    private long code;

    Status(int code){
        this.code = code;
    }

    public enum Message{
        NO_CONTENT("NO_CONTENT"),
        BAD_EXTENSION("BAD_EXTENSION"),
        INVALID_TITLE("INVALID_TITLE"),
        ACCEPTED("ACCEPTED"),
        OK("OK"),
        BAD_REQUEST("BAD_REQUEST"),
        NOT_FOUND("NOT_FOUND");

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
