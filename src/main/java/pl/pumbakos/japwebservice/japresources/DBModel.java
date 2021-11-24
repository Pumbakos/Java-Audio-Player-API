package pl.pumbakos.japwebservice.japresources;

/**
 * Abstract class allowing to identify an object as a database object and to perform operations on it <br>
 * (operations that can be performed only on types of such objects)
 */
public abstract class DBModel {
    protected Long _id;

    public Long getId() {
        return _id;
    }

    public void setId(Long _id) {
        this._id = _id;
    }
}
