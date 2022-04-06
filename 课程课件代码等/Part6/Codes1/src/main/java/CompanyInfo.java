import java.util.Date;

public class CompanyInfo {
    private String name;
    private String phoneNum;
    private String email;
    private String webSite;
    private String address;
    private String id;//统一社会信用代码
    private String legalRep;//法定代表人
    private String regDate;//注册时间
    private String registeredCapital;//注册资本（万元）
    private String paidInCapital;//实缴资本（万元）

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLegalRep() {
        return legalRep;
    }

    public void setLegalRep(String legalRep) {
        this.legalRep = legalRep;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getPaidInCapital() {
        return paidInCapital;
    }

    public void setPaidInCapital(String paidInCapital) {
        this.paidInCapital = paidInCapital;
    }

    @Override
    public String toString() {
        return "CompanyInfo{" +
                "name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", webSite='" + webSite + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", legalRep='" + legalRep + '\'' +
                ", regDate='" + regDate + '\'' +
                ", registeredCapital='" + registeredCapital + '\'' +
                ", paidInCapital='" + paidInCapital + '\'' +
                '}';
    }
}
