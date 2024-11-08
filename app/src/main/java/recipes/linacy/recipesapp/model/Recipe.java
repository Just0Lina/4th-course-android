package recipes.linacy.recipesapp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Recipe implements Serializable {
    private int id;
    private String title;
    private String image;
}