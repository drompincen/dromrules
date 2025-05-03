# dromrules

A **generic CSV-driven rule evaluator** for Java objects.  
Define simple condition‑action rules in a CSV file, and apply them to any Java `Collection<T>`. Uses reflection to read/write bean properties, with a builder-style API.

## Features

- **Generic**: works with any POJO.
- **CSV-based**: author rules in a human-readable `.csv`.
- **Multiple conditions** per rule, including `==`, `>`, `<`, `>=`, `<=`, and wildcard `*`.
- **Multiple actions** per rule, setting one or more properties.
- **Dynamic values**: use `$fieldName` to copy another property’s value.
- **Builder API**: easy setup with `EvaluatorBuilder`.

## Getting Started

### Prerequisites

- Java 8+ (tested on Java 11 and 21)
- Maven 3.6+

### Installation

Clone the repo and build:

```bash
git clone https://github.com/drompincen/dromrules.git
cd dromrules
mvn clean install

Usage
Prepare your CSV (src/test/resources/rules.csv):

csv
Copy
Edit
condition,condition,condition,action,action
color,weight,type,tag,category
red,>400,suv,truck,heavy
yellow,=500,*,\$color,light
Build an evaluator and process your objects:

java
Copy
Edit
import com.example.evaluator.Evaluator;
import com.example.evaluator.EvaluatorBuilder;

import java.io.File;
import java.util.List;

// ...
File rulesFile = new File("src/test/resources/rules.csv");
Evaluator<Car> evaluator = EvaluatorBuilder.builder(Car.class).build();

List<Car> cars = List.of(
    new Car("red", 500, "suv"),
    new Car("yellow", 500, null)
);
evaluator.process(cars, rulesFile);

cars.forEach(car -> {
    System.out.println(car.getColor()
        + " → tag=" + car.getTag()
        + ", category=" + car.getCategory());
});
Run tests:

bash
Copy
Edit
mvn test
CSV Format
Header Row 0: the words condition and action repeated to indicate columns.

Header Row 1: comma‑separated bean property names (e.g. color,weight,type,tag,category).

Subsequent Rows: one rule per line:

Columns 0–N: condition values (match order of condition property names).

Columns N+1–end: action values (match order of action property names).

Use operators in conditions:

=value or value → equals

>value, <value, >=value, <=value → comparisons

* → wildcard (matches anything, including null)

In actions, prefix with $ to copy another field's current value (e.g. \$color).

Contributing
Fork the repo

Create your feature branch (git checkout -b feature/foo)

Commit your changes (git commit -am 'Add foo')

Push to the branch (git push origin feature/foo)

Open a Pull Request

License
This project is licensed under the MIT License. See LICENSE for details.