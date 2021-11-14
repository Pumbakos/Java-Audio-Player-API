package pl.pumbakos.japwebservice.japresources;

public abstract class DBModel {
    protected Long _id;

    public Long getId() {
        return _id;
    }

    public void setId(Long _id) {
        this._id = _id;
    }
}
