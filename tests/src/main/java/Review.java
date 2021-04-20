import java.time.LocalDate;
import java.time.LocalDateTime;

public class Review {
    private String name;
    private String review;
    private String productCode;

    private int age;
    private LocalDate birthday;

    private LocalDateTime Time1;

    public LocalDateTime getTime1() {
        return Time1;
    }

    public void setTime1(LocalDateTime time1) {
        Time1 = time1;
    }

    public String getName() {
        return name;
    }

    public Review setName(String name) {
        this.name = name;
        return this;
    }

    public String getReview() {
        return review;
    }

    public Review setReview(String review) {
        this.review = review;
        return this;
    }

    public String getProductCode() {
        return productCode;
    }

    public Review setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
