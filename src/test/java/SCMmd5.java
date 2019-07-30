import com.aoyun.scm.Md5PwdEncoder;

public class SCMmd5 {
    public static void main(String[] args) {
            String  password = Md5PwdEncoder.passwordByMd5("123456");
            System.out.println(password);
    }
}
