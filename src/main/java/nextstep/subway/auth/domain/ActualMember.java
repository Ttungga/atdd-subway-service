package nextstep.subway.auth.domain;

public class ActualMember implements LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public ActualMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
