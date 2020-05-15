package ir.amindannak.movies.data;

public class NetworkState {

    public static final NetworkState LOADED = new NetworkState(Status.SUCCESS);
    public static final NetworkState LOADING = new NetworkState(Status.RUNNING);

    private Status status;
    private String msg;

    private NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private NetworkState(Status status) {
        this.status = status;
    }

    public static NetworkState error(String msg) {
        return new NetworkState(Status.FAILED, msg);
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    @FunctionalInterface
    public interface NetworkStateUpdater {
        void updateState(NetworkState state);
    }

}

