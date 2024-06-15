package dev.zspacehack.events;

public class Event {
    private final Stage stage;
    public Event(Stage stage) {
        cancel = false;
        this.stage = stage;
    }
    private boolean cancel;

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancel() {
        return cancel;
    }
    public Stage getStage() {
        return stage;
    }

    public enum Stage{
        Pre, Post
    }
}
