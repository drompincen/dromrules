package com.example.evaluator;

import com.example.javarules.Operator;
import com.example.javarules.RuleBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorJavaRulesTest {

    @Test
    public void testJavaRulesFromBuilder() throws Exception {
        // -- 1) Build the two Java rules --
        Rule<Car> r1 = RuleBuilder.rule(Car.class)
                .when("color",   Operator.EQUALS,       "red")
                .when("weight",  Operator.GREATER_THAN, "400")
                .when("type",    Operator.EQUALS,       "suv")
                .thenSet("tag",       "truck")
                .thenSet("category",  "heavy")
                .build();

        Rule<Car> r2 = RuleBuilder.rule(Car.class)
                .when("color",   Operator.EQUALS,      "yellow")
                .when("weight",  Operator.EQUALS,      "500")
                .when("type",    Operator.ANY,         "")
                .thenCopy("tag",       "color")
                .thenSet("category",   "light")
                .build();

        // -- 2) Wire them into an Evaluator via the builder --
        Evaluator<Car> evaluator = EvaluatorBuilder
                .builder(Car.class)
                .withRules(List.of(r1, r2))
                .build();

        // -- 3) Prepare inputs --
        List<Car> cars = Arrays.asList(
                new Car("red",    500, "suv"),
                new Car("yellow", 500, null),
                new Car("blue",   300, "coupe")
        );

        // -- 4) Process via single API call --
        evaluator.process(cars);

        // -- 5) Verify Rule 1 on red SUV --
        Car redCar = cars.get(0);
        assertEquals("truck", redCar.getTag());
        assertEquals("heavy", redCar.getCategory());

        // -- 6) Verify Rule 2 on yellow (type=null) --
        Car yellowCar = cars.get(1);
        assertEquals("yellow", yellowCar.getTag());
        assertEquals("light",  yellowCar.getCategory());

        // -- 7) Verify no match on blue coupe --
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

        public String getColor()                 { return color; }
        public void   setColor(String color)     { this.color = color; }

        public int    getWeight()                { return weight; }
        public void   setWeight(int weight)      { this.weight = weight; }

        public String getType()                  { return type; }
        public void   setType(String type)       { this.type = type; }

        public String getTag()                   { return tag; }
        public void   setTag(String tag)         { this.tag = tag; }

        public String getCategory()                           { return category; }
        public void   setCategory(String category)            { this.category = category; }
    }
}
