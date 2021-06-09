public class UserInfo {

    private String ID;

    private UserInfo() {
        ID = null;
    }

    private static class Holder {
        public static final UserInfo INSTANCE = new UserInfo();
    }

    public static UserInfo getInstance() {
        return Holder.INSTANCE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}