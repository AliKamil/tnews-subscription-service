package tnews.bot;

public enum Command {
    START("/start");


    private final String com;

    Command(String com) {
        this.com = com;
    }

    public String getCom() {
        return com;
    }

}
