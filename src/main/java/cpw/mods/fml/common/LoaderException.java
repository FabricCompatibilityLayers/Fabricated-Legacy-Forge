package cpw.mods.fml.common;

public class LoaderException extends RuntimeException {
    private static final long serialVersionUID = -5675297950958861378L;

    public LoaderException(Throwable wrapped) {
        super(wrapped);
    }

    public LoaderException() {
    }
}
