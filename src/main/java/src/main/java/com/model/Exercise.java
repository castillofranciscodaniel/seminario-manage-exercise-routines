package src.main.java.com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    private int id;
    private String name;
    private String description;
    private int duration;
    private String type;
}
