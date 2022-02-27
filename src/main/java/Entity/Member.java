package Entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

import domain.Address;
import domain.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
 @Id @GeneratedValue
 private Long id;
 private String username;
 @Embedded
 private Address address;
 @OneToMany(mappedBy = "member")
 private List<Order> orders = new ArrayList<>();
}
