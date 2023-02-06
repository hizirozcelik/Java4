
UML Class diagram for the application

+--------------+       +----------------+       +--------------+
|  Student     |-----<>|    Course      |<>-----|   Professor  |
+--------------+       +----------------+       +--------------+
| -id          |       | -id            |       | -id          |
| -name        |       | -name          |       | -name        |
| -lastName    |       | -code          |       |              |
| -courseList  |       | -professor     |       | -courseList  |
+--------------+       | -studentList   |       +--------------+
                        +----------------+
The arrows represent the relationship between classes, with the arrow pointing from the "owning" class to the "owned" class.
The <> symbol indicates a many-to-many relationship.
The - symbol in front of a property indicates it's a private property.