// src/test/java/com/example/evaluator/EvaluatorTest.java
package com.example.evaluator;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorTest {

    @Test
    public void testMultipleActions() throws Exception {
        // point to your updated rules.csv in src/test/resources
        File rulesFile = new File("./src/test/resources/rules.csv");
        Evaluator<Car> evaluator = EvaluatorBuilder.builder(Car.class).build();

        List<Car> cars = Arrays.asList(
                new Car("red",    500, "suv"),
                new Car("yellow", 500, null),
                new Car("blue",   300, "coupe")
        );

        evaluator.process(cars, rulesFile);

        // Rule 1: red,>400,suv -> tag="truck",   category="heavy"
        Car redCar = cars.get(0);
        assertEquals("truck", redCar.getTag());
        assertEquals("heavy", redCar.getCategory());

        // Rule 2: yellow,=500,* -> tag="$color" → "yellow", category="light"
        Car yellowCar = cars.get(1);
        assertEquals("yellow", yellowCar.getTag());
        assertEquals("light",  yellowCar.getCategory());

        // No rule matches blue coupe → tag & category remain null
        Car blueCar = cars.get(2);
        assertNull(blueCar.getTag());
        assertNull(blueCar.getCategory());
    }

    // Simple POJO for testing
    public static class Car {
        private String color;
        private int    weight;
        private String type;
        private String tag;
        private String category;

        public Car(String color, int weight, String type) {
            this.color  = color;
            this.weight = weight;
            this.type   = type;
        }

        public String getColor()        { return color; }
        public void   setColor(String c){ this.color = c; }

        public int    getWeight()            { return weight; }
        public void   setWeight(int weight)  { this.weight = weight; }

        public String getType()       { return type; }
        public void   setType(String t){ this.type = t; }

        public String getTag()            { return tag; }
        public void   setTag(String tag)  { this.tag = tag; }

        public String getCategory()               { return category; }
        public void   setCategory(String category){ this.category = category; }
    }
}
