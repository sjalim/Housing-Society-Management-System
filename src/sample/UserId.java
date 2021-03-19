package sample;

public class UserId {
    private static UserId mUserId=null;
    public String mId;
    public UserId() {
        mId = null;
    }
    public static UserId getInstance() {
        if(mUserId == null)
            mUserId = new UserId();
        return mUserId;
    }
}
