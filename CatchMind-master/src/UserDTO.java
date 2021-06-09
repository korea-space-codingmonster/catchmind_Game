public class UserDTO {
    private UserDTO() {}

    private static class Holder {
        public static final UserDTO INSTANCE = new UserDTO();
    }

    public static UserDTO getInstance() { return Holder.INSTANCE; }

    String userid;
    char[] password;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }
}