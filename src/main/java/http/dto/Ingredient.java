package http.dto;

import lombok.Data;

@Data
public class Ingredient {
    private String _id;
    private String name;
    private String type;
    private int price;
}

